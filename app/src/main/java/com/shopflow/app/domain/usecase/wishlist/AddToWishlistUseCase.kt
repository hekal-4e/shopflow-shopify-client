package com.shopflow.app.domain.usecase.wishlist

import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.repository.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(product: Product) {
        val item = com.shopflow.app.domain.model.WishlistItem(
            productId = product.id,
            variantId = product.variants.firstOrNull()?.id,
            title = product.title,
            brand = product.brand,
            imageUrl = product.images.firstOrNull()?.url ?: "",
            price = product.priceRange.minPrice.amount.toDouble(),
            currency = product.priceRange.minPrice.currencyCode,
            addedAt = System.currentTimeMillis()
        )
        wishlistRepository.addItem(item)
    }
}
