package com.shopflow.app.domain.usecase.product

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.repository.ProductRepository
import javax.inject.Inject

class GetFeaturedProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(first: Int = 20, query: String? = null): ApiResult<List<Product>> {
        return productRepository.getFeaturedProducts(first, query)
    }
}
