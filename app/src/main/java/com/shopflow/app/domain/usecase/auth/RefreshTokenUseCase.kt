package com.shopflow.app.domain.usecase.auth

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String): ApiResult<String> {
        return authRepository.refreshToken(accessToken)
    }
}
