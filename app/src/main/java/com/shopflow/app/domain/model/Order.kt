package com.shopflow.app.domain.model

enum class FulfillmentStatus {
    UNFULFILLED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

data class OrderLineItem(
    val title: String,
    val variant: String? = null,
    val quantity: Int,
    val price: Money,
    val image: ProductImage? = null
)

data class Order(
    val id: String,
    val orderNumber: Int,
    val name: String,
    val processedAt: String,
    val fulfillmentStatus: FulfillmentStatus,
    val totalPrice: Money,
    val lineItems: List<OrderLineItem>,
    val shippingAddress: Address? = null,
    val trackingNumber: String? = null
)
