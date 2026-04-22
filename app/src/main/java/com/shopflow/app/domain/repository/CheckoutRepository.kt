package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.ApiResult

data class CheckoutLineItemInput(
    val variantId: String,
    val quantity: Int
)

interface CheckoutRepository {
    suspend fun createCheckout(lineItems: List<CheckoutLineItemInput>): ApiResult<String>
}
