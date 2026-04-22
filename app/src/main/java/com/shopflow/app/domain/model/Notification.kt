package com.shopflow.app.domain.model

data class Notification(
    val id: Long = 0,
    val title: String,
    val body: String,
    val type: NotificationType,
    val referenceId: String?,
    val isRead: Boolean = false,
    val createdAt: Long
)

enum class NotificationType {
    ORDER_UPDATE,
    PROMOTION,
    WISHLIST_PRICE_DROP
}
