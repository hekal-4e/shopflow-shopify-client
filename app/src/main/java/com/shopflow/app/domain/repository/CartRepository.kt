package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.Cart
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.ProductVariant
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val cartState: Flow<Cart>
    
    suspend fun addToCart(product: Product, variant: ProductVariant, quantity: Int)
    suspend fun updateQuantity(lineItemId: String, quantity: Int)
    suspend fun removeFromCart(lineItemId: String)
    suspend fun clearCart()
}
