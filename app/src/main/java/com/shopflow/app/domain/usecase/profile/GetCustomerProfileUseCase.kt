package com.shopflow.app.domain.usecase.profile

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.repository.CustomerRepository
import com.shopflow.app.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetCustomerProfileUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): ApiResult<Customer> {
        // Mock token since it's not stored in preferences
        val accessToken = "mock-access-token" 
        return customerRepository.getProfile(accessToken)
    }
}
