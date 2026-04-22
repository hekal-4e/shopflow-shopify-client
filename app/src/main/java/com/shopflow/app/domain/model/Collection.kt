package com.shopflow.app.domain.model

data class Collection(
    val id: String,
    val title: String,
    val description: String? = null,
    val image: ProductImage? = null,
    val handle: String
)
