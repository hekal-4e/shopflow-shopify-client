package com.shopflow.app.domain.usecase.profile

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.domain.repository.CustomerRepository
import javax.inject.Inject

class UpdateCustomerProfileUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(firstName: String, lastName: String, phone: String?): ApiResult<Customer> {
        val accessToken = authRepository.getStoredAccessToken()
            ?: return ApiResult.GraphQLError(listOf("No authenticated customer session found"))
        return customerRepository.updateProfile(accessToken, firstName, lastName, null, phone)
    }
}
