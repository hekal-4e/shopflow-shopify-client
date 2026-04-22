package com.shopflow.app.domain.model

data class CartLineItem(
    val id: String,
    val variant: ProductVariant,
    val product: Product,
    val quantity: Int,
    val total: Money
)

data class Cart(
    val id: String,
    val lineItems: List<CartLineItem>,
    val subtotal: Money,
    val itemCount: Int
)
