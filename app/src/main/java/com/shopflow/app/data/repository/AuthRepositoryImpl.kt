package com.shopflow.app.data.repository

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.api.Optional
import com.shopflow.app.data.mapper.toDomainCustomer
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.type.CustomerAccessTokenCreateInput
import com.shopflow.app.type.CustomerCreateInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource
) : AuthRepository {
    private val authState = MutableStateFlow(false)
    private var accessToken: String? = null

    override suspend fun login(email: String, password: String): ApiResult<Customer> {
        return try {
            val response = dataSource.customerLogin(
                CustomerAccessTokenCreateInput(email = email, password = password)
            )
            val token = response.data?.customerAccessTokenCreate?.customerAccessToken?.accessToken
            when {
                token != null -> {
                    accessToken = token
                    authState.value = true
                    val profile = dataSource.fetchCustomerProfile(token).data?.toDomainCustomer()
                    if (profile != null) {
                        ApiResult.Success(profile.copy(accessToken = token))
                    } else {
                        ApiResult.Empty
                    }
                }
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ApiResult<Customer> {
        return try {
            val response = dataSource.customerRegister(
                CustomerCreateInput(
                    firstName = Optional.presentIfNotNull(firstName),
                    lastName = Optional.presentIfNotNull(lastName),
                    email = email,
                    password = password
                )
            )
            val customer = response.data?.customerCreate?.customer
            when {
                customer != null -> ApiResult.Success(
                    Customer(
                        id = customer.id,
                        firstName = customer.firstName.orEmpty(),
                        lastName = customer.lastName.orEmpty(),
                        email = customer.email
                    )
                )
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun refreshToken(accessToken: String): ApiResult<String> {
        return try {
            val response = dataSource.customerAccessTokenRenew(accessToken)
            val renewed = response.data?.customerAccessTokenRenew?.customerAccessToken?.accessToken
            when {
                renewed != null -> {
                    this.accessToken = renewed
                    authState.value = true
                    ApiResult.Success(renewed)
                }
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun logout() {
        accessToken = null
        authState.value = false
    }

    override fun getAuthState(): Flow<Boolean> = authState
}
