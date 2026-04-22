package com.shopflow.app.domain.usecase.checkout

import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.repository.CheckoutLineItemInput
import com.shopflow.app.domain.repository.CheckoutRepository
import javax.inject.Inject

class CreateCheckoutUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) {
    suspend operator fun invoke(lineItems: List<CheckoutLineItemInput>): ApiResult<String> {
        return checkoutRepository.createCheckout(lineItems)
    }
}
