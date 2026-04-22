package com.shopflow.app.domain.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class GraphQLError(val errors: List<String>) : ApiResult<Nothing>()
    data class NetworkError(val exception: Throwable) : ApiResult<Nothing>()
    data object Empty : ApiResult<Nothing>()
}
