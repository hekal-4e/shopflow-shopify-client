package com.shopflow.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wishlist_items",
    indices = [Index(value = ["productId"], unique = true)]
)
data class WishlistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val variantId: String?,
    val title: String,
    val brand: String?,
    val imageUrl: String,
    val price: Double,
    val currency: String,
    val addedAt: Long
)
