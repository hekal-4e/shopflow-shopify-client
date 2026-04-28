package com.shopflow.app.data.repository

import com.apollographql.apollo3.exception.ApolloException
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.repository.CheckoutLineItemInput
import com.shopflow.app.domain.repository.CheckoutRepository
import com.shopflow.app.type.CartInput
import com.shopflow.app.type.CartLineInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource
) : CheckoutRepository {
    override suspend fun createCheckout(lineItems: List<CheckoutLineItemInput>): ApiResult<String> {
        return try {
            val input = CartInput(
                lines = com.apollographql.apollo3.api.Optional.presentIfNotNull(lineItems.map {
                    CartLineInput(
                        merchandiseId = it.variantId,
                        quantity = com.apollographql.apollo3.api.Optional.presentIfNotNull(it.quantity)
                    )
                })
            )
            val response = dataSource.cartCreate(input)
            val cartPayload = response.data?.cartCreate
            val webUrl = cartPayload?.cart?.checkoutUrl?.toString()
            val cartUserErrors = cartPayload?.userErrors
                ?.mapNotNull { it.message.takeIf(String::isNotBlank) }

            when {
                webUrl != null -> ApiResult.Success(webUrl)
                !cartUserErrors.isNullOrEmpty() -> ApiResult.GraphQLError(cartUserErrors)
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }
}