package com.shopflow.app.data.repository

import com.shopflow.app.domain.model.Cart
import com.shopflow.app.domain.model.CartLineItem
import com.shopflow.app.domain.model.Money
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.ProductVariant
import com.shopflow.app.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor() : CartRepository {

    private val _cartState = MutableStateFlow(Cart(id = UUID.randomUUID().toString(), lineItems = emptyList(), subtotal = Money(0.0, "USD"), itemCount = 0))
    override val cartState: Flow<Cart> = _cartState.asStateFlow()

    override suspend fun addToCart(product: Product, variant: ProductVariant, quantity: Int) {
        _cartState.update { currentCart ->
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
    }

    override suspend fun updateQuantity(lineItemId: String, quantity: Int) {
        _cartState.update { currentCart ->
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
    }

    override suspend fun removeFromCart(lineItemId: String) {
        _cartState.update { currentCart ->
            val newLineItems = currentCart.lineItems.filter { it.id != lineItemId }
            recalculateCart(currentCart.id, newLineItems)
        }
    }

    override suspend fun clearCart() {
        _cartState.update {
            Cart(id = UUID.randomUUID().toString(), lineItems = emptyList(), subtotal = Money(0.0, "USD"), itemCount = 0)
        }
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
}
