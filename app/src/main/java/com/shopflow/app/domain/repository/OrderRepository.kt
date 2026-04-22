package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(accessToken: String, first: Int = 20, after: String? = null): ApiResult<List<Order>>
    suspend fun getOrderDetail(accessToken: String, orderId: String): ApiResult<Order>
}
