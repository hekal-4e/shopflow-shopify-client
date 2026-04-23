package com.shopflow.app.domain.usecase.order

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderHistoryUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResult<List<Order>> {
        val accessToken = authRepository.getStoredAccessToken()
            ?: return ApiResult.GraphQLError(listOf("No authenticated customer session found"))
        return orderRepository.getOrders(accessToken)
    }
}
