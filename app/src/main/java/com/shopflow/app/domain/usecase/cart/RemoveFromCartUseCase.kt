package com.shopflow.app.domain.usecase.cart

import com.shopflow.app.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(lineItemId: String) {
        cartRepository.removeFromCart(lineItemId)
    }
}
