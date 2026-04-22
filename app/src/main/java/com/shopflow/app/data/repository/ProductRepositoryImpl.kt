package com.shopflow.app.data.repository

import com.apollographql.apollo3.exception.ApolloException
import com.shopflow.app.data.mapper.toDomainCollections
import com.shopflow.app.data.mapper.toDomainProduct
import com.shopflow.app.data.mapper.toDomainProducts
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Collection
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource
) : ProductRepository {
    override suspend fun getCollections(first: Int): ApiResult<List<Collection>> {
        return try {
            val response = dataSource.fetchCollections(first)
            when {
                response.data != null -> ApiResult.Success(response.data!!.toDomainCollections())
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun getFeaturedProducts(first: Int, query: String?): ApiResult<List<Product>> {
        return try {
            val response = dataSource.fetchFeaturedProducts(first, query)
            when {
                response.data != null -> ApiResult.Success(response.data!!.toDomainProducts())
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun getProductsByCollection(
        collectionId: String,
        first: Int,
        after: String?
    ): ApiResult<List<Product>> {
        return try {
            val response = dataSource.fetchProductsByCollection(collectionId, first, after)
            when {
                response.data != null -> ApiResult.Success(response.data!!.toDomainProducts())
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun getProductDetail(productId: String): ApiResult<Product> {
        return try {
            val response = dataSource.fetchProductDetail(productId)
            when {
                response.data?.toDomainProduct() != null -> ApiResult.Success(response.data!!.toDomainProduct()!!)
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun searchProducts(query: String, first: Int): ApiResult<List<Product>> {
        return try {
            val response = dataSource.searchProducts(query, first)
            when {
                response.data != null -> ApiResult.Success(response.data!!.toDomainProducts())
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }
}
