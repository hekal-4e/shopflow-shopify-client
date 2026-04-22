package com.shopflow.app.domain.model

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val descriptionHtml: String,
    val brand: String? = null,
    val productType: String,
    val images: List<ProductImage>,
    val variants: List<ProductVariant>,
    val priceRange: PriceRange,
    val rating: Float? = null,
    val reviewCount: Int = 0,
    val collections: List<String> = emptyList(),
    val isAvailable: Boolean
)
