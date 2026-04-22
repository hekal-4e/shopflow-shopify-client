package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer

interface CustomerRepository {
    suspend fun getProfile(accessToken: String): ApiResult<Customer>
    suspend fun updateProfile(
        accessToken: String,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?
    ): ApiResult<Customer>
}
