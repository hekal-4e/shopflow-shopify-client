package com.shopflow.app.domain.usecase.profile

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.repository.CustomerRepository
import com.shopflow.app.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateCustomerProfileUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(firstName: String, lastName: String, phone: String?): ApiResult<Customer> {
        val accessToken = "mock-access-token"
        return customerRepository.updateProfile(accessToken, firstName, lastName, null, phone)
    }
}
