package com.shopflow.app.domain.usecase.order

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.domain.repository.OrderRepository
import com.shopflow.app.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String): ApiResult<Order> {
        val accessToken = "mock-access-token" // In a real app, retrieve from secure storage
        return orderRepository.getOrderDetail(accessToken, orderId)
    }
}
