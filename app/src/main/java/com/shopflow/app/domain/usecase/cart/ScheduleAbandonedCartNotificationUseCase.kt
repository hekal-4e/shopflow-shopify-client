package com.shopflow.app.domain.usecase.cart

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.domain.repository.CartRepository
import com.shopflow.app.domain.usecase.profile.GetCustomerProfileUseCase
import javax.inject.Inject

class ScheduleAbandonedCartNotificationUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val getCustomerProfileUseCase: GetCustomerProfileUseCase
) {
    suspend operator fun invoke(product: Product): Result<Unit> {
        val token = authRepository.getStoredAccessToken() ?: return Result.failure(
            Exception("User not authenticated")
        )

        val profileResult = getCustomerProfileUseCase()
        if (profileResult !is ApiResult.Success) {
            return Result.failure(Exception("Failed to get customer profile"))
        }

        val email = profileResult.data.email
        if (email.isEmpty()) {
            return Result.failure(Exception("Customer email not available"))
        }

        return cartRepository.notifyCartAbandonment(email, product.title)
    }
}