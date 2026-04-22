package com.shopflow.app.data.repository

import com.apollographql.apollo3.exception.ApolloException
import com.shopflow.app.data.mapper.toDomainOrders
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.repository.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource
) : OrderRepository {
    override suspend fun getOrders(accessToken: String, first: Int, after: String?): ApiResult<List<Order>> {
        return try {
            val response = dataSource.fetchCustomerOrders(accessToken, first, after)
            when {
                response.data != null -> ApiResult.Success(response.data!!.toDomainOrders())
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun getOrderDetail(accessToken: String, orderId: String): ApiResult<Order> {
        return when (val result = getOrders(accessToken, first = 50, after = null)) {
            is ApiResult.Success -> {
                result.data.firstOrNull { it.id == orderId }?.let { ApiResult.Success(it) } ?: ApiResult.Empty
            }
            is ApiResult.GraphQLError -> result
            is ApiResult.NetworkError -> result
            ApiResult.Empty -> ApiResult.Empty
        }
    }
}
