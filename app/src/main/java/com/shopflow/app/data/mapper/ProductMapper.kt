package com.shopflow.app.data.mapper

import com.shopflow.app.FetchFeaturedProductsQuery
import com.shopflow.app.FetchProductDetailQuery
import com.shopflow.app.FetchProductsByCollectionQuery
import com.shopflow.app.SearchProductsQuery
import com.shopflow.app.domain.model.Money
import com.shopflow.app.domain.model.PriceRange
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.ProductImage
import com.shopflow.app.domain.model.ProductVariant
import com.shopflow.app.domain.model.SelectedOption

private fun Any.toAmount(): Double = this.toString().toDoubleOrNull() ?: 0.0

fun FetchFeaturedProductsQuery.Data.toDomainProducts(): List<Product> {
    return products.edges.map { edge ->
        val node = edge.node
        Product(
            id = node.id,
            title = node.title,
            description = node.description,
            descriptionHtml = node.description,
            brand = node.vendor,
            productType = node.productType,
            images = node.images.edges.map { imageEdge ->
                ProductImage(
                    url = imageEdge.node.url.toString(),
                    altText = imageEdge.node.altText,
                    width = imageEdge.node.width,
                    height = imageEdge.node.height
                )
            },
            variants = node.variants.edges.map { variantEdge ->
                val variant = variantEdge.node
                ProductVariant(
                    id = variant.id,
                    title = variant.title,
                    price = Money(
                        amount = variant.price.amount.toAmount(),
                        currencyCode = variant.price.currencyCode.name
                    ),
                    compareAtPrice = variant.compareAtPrice?.let {
                        Money(
                            amount = it.amount.toAmount(),
                            currencyCode = it.currencyCode.name
                        )
                    },
                    isAvailable = variant.availableForSale,
                    image = variant.image?.let {
                        ProductImage(
                            url = it.url.toString(),
                            altText = it.altText,
                            width = null,
                            height = null
                        )
                    },
                    selectedOptions = variant.selectedOptions.map { option ->
                        SelectedOption(name = option.name, value = option.value)
                    }
                )
            },
            priceRange = PriceRange(
                minPrice = Money(
                    amount = node.priceRange.minVariantPrice.amount.toAmount(),
                    currencyCode = node.priceRange.minVariantPrice.currencyCode.name
                ),
                maxPrice = Money(
                    amount = node.priceRange.maxVariantPrice.amount.toAmount(),
                    currencyCode = node.priceRange.maxVariantPrice.currencyCode.name
                )
            ),
            isAvailable = node.availableForSale
        )
    }
}

fun FetchProductsByCollectionQuery.Data.toDomainProducts(): List<Product> {
    val collectionNode = collection ?: return emptyList()
    return collectionNode.products.edges.map { edge ->
        val node = edge.node
        val min = node.priceRange.minVariantPrice
        Product(
            id = node.id,
            title = node.title,
            description = "",
            descriptionHtml = "",
            brand = node.vendor,
            productType = node.productType,
            images = node.images.edges.map { imageEdge ->
                ProductImage(url = imageEdge.node.url.toString(), altText = imageEdge.node.altText)
            },
            variants = node.variants.edges.map { variantEdge ->
                val variant = variantEdge.node
                ProductVariant(
                    id = variant.id,
                    title = variant.title,
                    price = Money(
                        variant.price.amount.toAmount(),
                        variant.price.currencyCode.name
                    ),
                    selectedOptions = variant.selectedOptions.map {
                        SelectedOption(it.name, it.value)
                    },
                    isAvailable = variant.availableForSale
                )
            },
            priceRange = PriceRange(
                minPrice = Money(min.amount.toAmount(), min.currencyCode.name),
                maxPrice = Money(min.amount.toAmount(), min.currencyCode.name)
            ),
            isAvailable = node.availableForSale
        )
    }
}

fun SearchProductsQuery.Data.toDomainProducts(): List<Product> {
    return products.edges.map { edge ->
        val node = edge.node
        val min = node.priceRange.minVariantPrice
        Product(
            id = node.id,
            title = node.title,
            description = "",
            descriptionHtml = "",
            brand = node.vendor,
            productType = node.productType,
            images = node.images.edges.map { ProductImage(it.node.url.toString(), it.node.altText) },
            variants = emptyList(),
            priceRange = PriceRange(
                minPrice = Money(min.amount.toAmount(), min.currencyCode.name),
                maxPrice = Money(min.amount.toAmount(), min.currencyCode.name)
            ),
            isAvailable = node.availableForSale
        )
    }
}

fun FetchProductDetailQuery.Data.toDomainProduct(): Product? {
    val node = product ?: return null
    return Product(
        id = node.id,
        title = node.title,
        description = node.description,
        descriptionHtml = node.descriptionHtml.toString(),
        brand = node.vendor,
        productType = node.productType,
        images = node.images.edges.map { imageEdge ->
            ProductImage(
                url = imageEdge.node.url.toString(),
                altText = imageEdge.node.altText,
                width = imageEdge.node.width,
                height = imageEdge.node.height
            )
        },
        variants = product.variants.edges.map { variantEdge ->
            val variant = variantEdge.node
            ProductVariant(
                id = variant.id,
                title = variant.title,
                price = Money(
                    amount = variant.price.amount.toAmount(),
                    currencyCode = variant.price.currencyCode.name
                ),
                compareAtPrice = variant.compareAtPrice?.let {
                    Money(
                        amount = it.amount.toAmount(),
                        currencyCode = it.currencyCode.name
                    )
                },
                isAvailable = variant.availableForSale,
                image = variant.image?.let {
                    ProductImage(
                        url = it.url.toString(),
                        altText = it.altText,
                        width = null,
                        height = null
                    )
                },
                selectedOptions = variant.selectedOptions.map {
                    SelectedOption(it.name, it.value)
                }
            )
        },
        priceRange = PriceRange(
            minPrice = Money(
                node.priceRange.minVariantPrice.amount.toAmount(),
                node.priceRange.minVariantPrice.currencyCode.name
            ),
            maxPrice = Money(
                node.priceRange.maxVariantPrice.amount.toAmount(),
                node.priceRange.maxVariantPrice.currencyCode.name
            )
        ),
        isAvailable = node.availableForSale
    )
}