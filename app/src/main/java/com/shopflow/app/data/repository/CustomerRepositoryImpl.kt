package com.shopflow.app.data.repository

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.api.Optional
import com.shopflow.app.data.mapper.toDomainCustomer
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.repository.CustomerRepository
import com.shopflow.app.type.CustomerUpdateInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource
) : CustomerRepository {
    override suspend fun getProfile(accessToken: String): ApiResult<Customer> {
        return try {
            val response = dataSource.fetchCustomerProfile(accessToken)
            val customer = response.data?.toDomainCustomer()
            when {
                customer != null -> ApiResult.Success(customer)
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }

    override suspend fun updateProfile(
        accessToken: String,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?
    ): ApiResult<Customer> {
        return try {
            val response = dataSource.customerUpdate(
                accessToken = accessToken,
                input = CustomerUpdateInput(
                    firstName = Optional.presentIfNotNull(firstName),
                    lastName = Optional.presentIfNotNull(lastName),
                    email = Optional.presentIfNotNull(email),
                    phone = Optional.presentIfNotNull(phone)
                )
            )
            val customer = response.data?.customerUpdate?.customer
            when {
                customer != null -> ApiResult.Success(
                    Customer(
                        id = customer.id,
                        firstName = customer.firstName.orEmpty(),
                        lastName = customer.lastName.orEmpty(),
                        email = customer.email.orEmpty(),
                        phone = customer.phone
                    )
                )
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }
}
