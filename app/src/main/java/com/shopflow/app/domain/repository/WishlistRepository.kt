package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.WishlistItem
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<List<WishlistItem>>
    suspend fun addItem(item: WishlistItem)
    suspend fun removeItem(productId: String)
    fun isWishlisted(productId: String): Flow<Boolean>
    fun getCount(): Flow<Int>
}
