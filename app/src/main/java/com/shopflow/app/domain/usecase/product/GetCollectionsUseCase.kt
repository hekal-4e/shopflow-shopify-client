package com.shopflow.app.domain.usecase.product

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Collection
import com.shopflow.app.domain.repository.ProductRepository
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(first: Int = 20): ApiResult<List<Collection>> {
        return productRepository.getCollections(first)
    }
}
