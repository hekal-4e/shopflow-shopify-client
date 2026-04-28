package com.shopflow.app.data.repository

import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.shopflow.app.data.mapper.toDomainCustomer
import com.shopflow.app.data.local.datastore.TokenDataStore
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.type.CustomerAccessTokenCreateInput
import com.shopflow.app.type.CustomerCreateInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val authState = MutableStateFlow(false)
    private var accessToken: String? = null

    init {
        repositoryScope.launch {
            combine(
                tokenDataStore.accessTokenFlow,
                tokenDataStore.expiryTimestampFlow
            ) { storedToken, expiryTimestamp ->
                storedToken to expiryTimestamp
            }.collect { (storedToken, expiryTimestamp) ->
                val now = Instant.now().toEpochMilli()
                val isValidToken = !storedToken.isNullOrBlank() && expiryTimestamp > now
                authState.value = isValidToken
                accessToken = if (isValidToken) storedToken else null
            }
        }
    }

    override suspend fun login(email: String, password: String): ApiResult<Customer> {
        return try {
            val response = dataSource.customerLogin(
                CustomerAccessTokenCreateInput(email = email, password = password)
            )
            val tokenPayload = response.data?.customerAccessTokenCreate?.customerAccessToken
            val token = tokenPayload?.accessToken
            when {
                token != null -> {
                    val expiryTimestamp = parseExpiryTimestamp(tokenPayload.expiresAt.toString())
                    accessToken = token
                    authState.value = true
                    tokenDataStore.storeToken(token, expiryTimestamp)
                    val profile = dataSource.fetchCustomerProfile(token).data?.toDomainCustomer()
                    if (profile != null) {
                        ApiResult.Success(
                            profile.copy(
                                accessToken = token,
                                tokenExpiry = expiryTimestamp
                            )
                        )
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
                        email = customer.email.orEmpty()
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
            val tokenPayload = response.data?.customerAccessTokenRenew?.customerAccessToken
            val renewed = tokenPayload?.accessToken
            when {
                renewed != null -> {
                    val expiryTimestamp = parseExpiryTimestamp(tokenPayload.expiresAt.toString())
                    this.accessToken = renewed
                    authState.value = true
                    tokenDataStore.storeToken(renewed, expiryTimestamp)
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
        tokenDataStore.clearToken()
        authState.value = false
    }

    override suspend fun getStoredAccessToken(): String? {
        val inMemoryToken = accessToken
        if (!inMemoryToken.isNullOrBlank()) {
            return inMemoryToken
        }

        val persistedToken = tokenDataStore.getAccessToken()
        accessToken = persistedToken
        return persistedToken
    }

    override fun getAuthState(): Flow<Boolean> = authState

    private fun parseExpiryTimestamp(expiresAt: String): Long {
        return runCatching {
            Instant.parse(expiresAt).toEpochMilli()
        }.getOrElse { 0L }
    }
}
