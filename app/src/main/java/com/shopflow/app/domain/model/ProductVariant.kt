package com.shopflow.app.domain.model

data class ProductImage(
    val url: String,
    val altText: String? = null,
    val width: Int? = null,
    val height: Int? = null
)

data class SelectedOption(
    val name: String,
    val value: String
)

data class Money(
    val amount: Double,
    val currencyCode: String
)

data class PriceRange(
    val minPrice: Money,
    val maxPrice: Money
)

data class ProductVariant(
    val id: String,
    val title: String,
    val price: Money,
    val compareAtPrice: Money? = null,
    val selectedOptions: List<SelectedOption>,
    val isAvailable: Boolean,
    val image: ProductImage? = null
)
