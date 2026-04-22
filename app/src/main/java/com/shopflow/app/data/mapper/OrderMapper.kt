package com.shopflow.app.data.mapper

import com.shopflow.app.FetchCustomerOrdersQuery
import com.shopflow.app.domain.model.Address
import com.shopflow.app.domain.model.FulfillmentStatus
import com.shopflow.app.domain.model.Money
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.model.OrderLineItem
import com.shopflow.app.domain.model.ProductImage

private fun String.toAmount(): Double = toDoubleOrNull() ?: 0.0

private fun mapStatus(status: com.shopflow.app.type.FulfillmentStatus?): FulfillmentStatus {
    return when (status?.name) {
        FulfillmentStatus.PROCESSING.name -> FulfillmentStatus.PROCESSING
        FulfillmentStatus.SHIPPED.name -> FulfillmentStatus.SHIPPED
        FulfillmentStatus.DELIVERED.name -> FulfillmentStatus.DELIVERED
        FulfillmentStatus.CANCELLED.name -> FulfillmentStatus.CANCELLED
        else -> FulfillmentStatus.UNFULFILLED
    }
}

fun FetchCustomerOrdersQuery.Data.toDomainOrders(): List<Order> {
    val orderEdges = customer?.orders?.edges.orEmpty()
    return orderEdges.map { edge ->
        val node = edge.node
        Order(
            id = node.id,
            orderNumber = node.orderNumber,
            name = node.name,
            processedAt = node.processedAt,
            fulfillmentStatus = mapStatus(node.fulfillmentStatus),
            totalPrice = Money(
                amount = node.totalPrice.amount.toAmount(),
                currencyCode = node.totalPrice.currencyCode
            ),
            lineItems = node.lineItems.edges.map { itemEdge ->
                val item = itemEdge.node
                OrderLineItem(
                    title = item.title,
                    variant = item.variant?.title,
                    quantity = item.quantity,
                    price = Money(
                        amount = item.variant?.price?.amount?.toAmount() ?: 0.0,
                        currencyCode = item.variant?.price?.currencyCode.orEmpty()
                    ),
                    image = item.variant?.image?.let {
                        ProductImage(url = it.url, altText = it.altText)
                    }
                )
            },
            shippingAddress = node.shippingAddress?.let {
                Address(
                    id = "",
                    address1 = it.address1.orEmpty(),
                    address2 = it.address2,
                    city = it.city.orEmpty(),
                    province = it.province,
                    country = it.country.orEmpty(),
                    zip = it.zip.orEmpty()
                )
            }
        )
    }
}
