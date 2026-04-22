package com.shopflow.app.domain.usecase.wishlist

import com.shopflow.app.domain.repository.WishlistRepository
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(productId: String) {
        wishlistRepository.removeItem(productId)
    }
}
