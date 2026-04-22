package com.shopflow.app.domain.model

data class WishlistItem(
    val id: Long = 0,
    val productId: String,
    val variantId: String?,
    val title: String,
    val brand: String?,
    val imageUrl: String,
    val price: Double,
    val currency: String,
    val addedAt: Long
)
