package com.shopflow.app.domain.usecase.cart

import com.shopflow.app.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(lineItemId: String, quantity: Int) {
        cartRepository.updateQuantity(lineItemId, quantity)
    }
}
