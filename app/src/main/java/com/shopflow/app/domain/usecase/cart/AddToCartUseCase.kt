package com.shopflow.app.domain.usecase.cart

import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.ProductVariant
import com.shopflow.app.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product, variant: ProductVariant, quantity: Int = 1) {
        cartRepository.addToCart(product, variant, quantity)
    }
}
