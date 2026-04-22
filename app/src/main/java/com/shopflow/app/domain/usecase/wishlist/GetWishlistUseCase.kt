package com.shopflow.app.domain.usecase.wishlist

import com.shopflow.app.domain.model.WishlistItem
import com.shopflow.app.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke(): Flow<List<WishlistItem>> {
        return wishlistRepository.getWishlist()
    }
}
