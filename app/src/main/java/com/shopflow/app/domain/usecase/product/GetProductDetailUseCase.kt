package com.shopflow.app.domain.usecase.product

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: String): ApiResult<Product> {
        return productRepository.getProductDetail(id)
    }
}
