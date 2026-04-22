package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Collection
import com.shopflow.app.domain.model.Product

interface ProductRepository {
    suspend fun getCollections(first: Int = 20): ApiResult<List<Collection>>
    suspend fun getFeaturedProducts(first: Int = 20, query: String? = null): ApiResult<List<Product>>
    suspend fun getProductsByCollection(
        collectionId: String,
        first: Int = 20,
        after: String? = null
    ): ApiResult<List<Product>>

    suspend fun getProductDetail(productId: String): ApiResult<Product>
    suspend fun searchProducts(query: String, first: Int = 20): ApiResult<List<Product>>
}
