package com.shopflow.app.domain.usecase.cart

import com.shopflow.app.domain.model.Cart
import com.shopflow.app.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<Cart> {
        return cartRepository.cartState
    }
}
