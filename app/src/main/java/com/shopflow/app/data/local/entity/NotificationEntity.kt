package com.shopflow.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NotificationType {
    ORDER_UPDATE,
    PROMOTION,
    WISHLIST_PRICE_DROP
}

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val type: NotificationType,
    val referenceId: String?,
    val isRead: Boolean = false,
    val createdAt: Long
)
