package com.shopflow.app.data.repository

import com.apollographql.apollo3.exception.ApolloException
import com.shopflow.app.data.remote.ShopifyDataSource
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.repository.CheckoutLineItemInput
import com.shopflow.app.domain.repository.CheckoutRepository
import com.shopflow.app.type.CheckoutCreateInput
import com.shopflow.app.type.CheckoutLineItemInput as ApolloCheckoutLineItemInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutRepositoryImpl @Inject constructor(
    private val dataSource: ShopifyDataSource
) : CheckoutRepository {
    override suspend fun createCheckout(lineItems: List<CheckoutLineItemInput>): ApiResult<String> {
        return try {
            val input = CheckoutCreateInput(
                lineItems = lineItems.map { ApolloCheckoutLineItemInput(it.variantId, it.quantity) }
            )
            val response = dataSource.checkoutCreate(input)
            val checkoutPayload = response.data?.checkoutCreate
            val webUrl = checkoutPayload?.checkout?.webUrl
            val checkoutUserErrors = checkoutPayload?.checkoutUserErrors
                ?.mapNotNull { it.message.takeIf(String::isNotBlank) }
            when {
                webUrl != null -> ApiResult.Success(webUrl)
                !checkoutUserErrors.isNullOrEmpty() -> ApiResult.GraphQLError(checkoutUserErrors)
                response.errors?.isNotEmpty() == true -> ApiResult.GraphQLError(response.errors!!.map { it.message })
                else -> ApiResult.Empty
            }
        } catch (e: ApolloException) {
            ApiResult.NetworkError(e)
        }
    }
}
