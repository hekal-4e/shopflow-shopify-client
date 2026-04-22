package com.shopflow.app.domain.usecase.order

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderHistoryUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(): ApiResult<List<Order>> {
        val accessToken = "mock-access-token"
        return orderRepository.getOrders(accessToken)
    }
}
