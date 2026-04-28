package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<Customer>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ApiResult<Customer>

    suspend fun refreshToken(accessToken: String): ApiResult<String>
    suspend fun logout()
    suspend fun getStoredAccessToken(): String?
    fun getAuthState(): Flow<Boolean>
}
