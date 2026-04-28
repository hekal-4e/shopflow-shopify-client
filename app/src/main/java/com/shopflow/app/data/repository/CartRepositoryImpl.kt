package com.shopflow.app.data.repository

import com.google.gson.Gson
import com.shopflow.app.data.local.datastore.CartDataStore
import com.shopflow.app.data.remote.dto.CartWebhookRequest
import com.shopflow.app.di.WebhookClient
import com.shopflow.app.domain.model.Cart
import com.shopflow.app.domain.model.CartLineItem
import com.shopflow.app.domain.model.Money
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.ProductVariant
import com.shopflow.app.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    @WebhookClient private val webhookClient: OkHttpClient,
    private val cartDataStore: CartDataStore
) : CartRepository {

    private val gson = Gson()
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _cartState = MutableStateFlow(createEmptyCart())
    override val cartState: Flow<Cart> = _cartState.asStateFlow()

    init {
        repositoryScope.launch {
            cartDataStore.cartFlow.collect { storedCart ->
                _cartState.value = storedCart ?: createEmptyCart()
            }
        }
    }

    override suspend fun addToCart(product: Product, variant: ProductVariant, quantity: Int) {
        val updatedCart = _cartState.updateAndGet { currentCart ->
            val existingItemIndex = currentCart.lineItems.indexOfFirst { it.variant.id == variant.id }
            val newLineItems = currentCart.lineItems.toMutableList()

            if (existingItemIndex != -1) {
                val existingItem = newLineItems[existingItemIndex]
                val newQuantity = existingItem.quantity + quantity
                newLineItems[existingItemIndex] = existingItem.copy(
                    quantity = newQuantity,
                    total = Money(variant.price.amount * newQuantity, variant.price.currencyCode)
                )
            } else {
                newLineItems.add(
                    CartLineItem(
                        id = UUID.randomUUID().toString(),
                        variant = variant,
                        product = product,
                        quantity = quantity,
                        total = Money(variant.price.amount * quantity, variant.price.currencyCode)
                    )
                )
            }
            recalculateCart(currentCart.id, newLineItems)
        }
        cartDataStore.saveCart(updatedCart)
    }

    override suspend fun updateQuantity(lineItemId: String, quantity: Int) {
        val updatedCart = _cartState.updateAndGet { currentCart ->
            if (quantity <= 0) {
                val newLineItems = currentCart.lineItems.filter { it.id != lineItemId }
                recalculateCart(currentCart.id, newLineItems)
            } else {
                val newLineItems = currentCart.lineItems.map { item ->
                    if (item.id == lineItemId) {
                        item.copy(
                            quantity = quantity,
                            total = Money(item.variant.price.amount * quantity, item.variant.price.currencyCode)
                        )
                    } else item
                }
                recalculateCart(currentCart.id, newLineItems)
            }
        }
        if (updatedCart.lineItems.isEmpty()) {
            cartDataStore.clearCart()
        } else {
            cartDataStore.saveCart(updatedCart)
        }
    }

    override suspend fun removeFromCart(lineItemId: String) {
        val updatedCart = _cartState.updateAndGet { currentCart ->
            val newLineItems = currentCart.lineItems.filter { it.id != lineItemId }
            recalculateCart(currentCart.id, newLineItems)
        }
        if (updatedCart.lineItems.isEmpty()) {
            cartDataStore.clearCart()
        } else {
            cartDataStore.saveCart(updatedCart)
        }
    }

    override suspend fun clearCart() {
        _cartState.value = createEmptyCart()
        cartDataStore.clearCart()
    }

    private fun recalculateCart(cartId: String, items: List<CartLineItem>): Cart {
        var subtotalAmount = 0.0
        var currencyCode = "USD"
        var count = 0

        for (item in items) {
            subtotalAmount += item.total.amount
            currencyCode = item.total.currencyCode
            count += item.quantity
        }

        return Cart(
            id = cartId,
            lineItems = items,
            subtotal = Money(subtotalAmount, currencyCode),
            itemCount = count
        )
    }

    override suspend fun notifyCartAbandonment(email: String, productName: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val webhookRequest = CartWebhookRequest(
                    userEmail = email,
                    productName = productName
                )
                val jsonPayload = gson.toJson(webhookRequest)
                val requestBody = jsonPayload.toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url(HttpUrl.Builder()
                        .scheme("https")
                        .host("hekl.app.n8n.cloud")
                        .addPathSegment("webhook")
                        .addPathSegment("cart-abandonment")
                        .build())
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build()

                webhookClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Result.success(Unit)
                    } else {
                        Result.failure(IOException("Webhook failed with code: ${response.code}"))
                    }
                }
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(IOException("Unexpected error: ${e.message}", e))
            }
        }
    }

    private fun createEmptyCart(): Cart {
        return Cart(
            id = UUID.randomUUID().toString(),
            lineItems = emptyList(),
            subtotal = Money(0.0, "USD"),
            itemCount = 0
        )
    }
}
