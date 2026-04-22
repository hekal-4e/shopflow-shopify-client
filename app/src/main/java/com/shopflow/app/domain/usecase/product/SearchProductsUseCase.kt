package com.shopflow.app.domain.usecase.product

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(query: String, first: Int = 20): ApiResult<List<Product>> {
        return productRepository.searchProducts(query, first)
    }
}
