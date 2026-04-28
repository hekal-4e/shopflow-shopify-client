package com.shopflow.app.presentation.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.repository.CheckoutLineItemInput
import com.shopflow.app.domain.usecase.cart.GetCartUseCase
import com.shopflow.app.domain.usecase.checkout.CreateCheckoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val isCreatingCheckout: Boolean = false,
    val checkoutUrl: String? = null,
    val checkoutOpened: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val createCheckoutUseCase: CreateCheckoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun createCheckout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingCheckout = true, error = null) }

            val cart = getCartUseCase().firstOrNull()
            if (cart == null || cart.lineItems.isEmpty()) {
                _uiState.update { it.copy(isCreatingCheckout = false, error = "Cart is empty") }
                return@launch
            }

            val inputList = cart.lineItems.map {
                CheckoutLineItemInput(variantId = it.variant.id, quantity = it.quantity)
            }
            if (inputList.any { it.variantId.isBlank() || it.quantity <= 0 }) {
                _uiState.update {
                    it.copy(
                        isCreatingCheckout = false,
                        error = "Invalid cart items. Please remove and re-add products."
                    )
                }
                return@launch
            }

            when (val result = createCheckoutUseCase(inputList)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreatingCheckout = false,
                            checkoutUrl = result.data,
                            checkoutOpened = false
                        )
                    }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update {
                        it.copy(
                            isCreatingCheckout = false,
                            error = result.exception.message ?: "Network error while creating checkout."
                        )
                    }
                }
                is ApiResult.GraphQLError -> {
                    val bestError = result.errors.firstOrNull { it.isNotBlank() }
                        ?: "Failed to create checkout."
                    _uiState.update { it.copy(isCreatingCheckout = false, error = bestError) }
                }
                is ApiResult.Empty -> {
                    _uiState.update {
                        it.copy(
                            isCreatingCheckout = false,
                            error = "Checkout could not be created. Try again."
                        )
                    }
                }
            }
        }
    }

    fun onCheckoutUrlHandled() {
        _uiState.update { it.copy(checkoutUrl = null, checkoutOpened = true) }
    }

    fun onCheckoutLaunchFailed(message: String) {
        _uiState.update { it.copy(checkoutUrl = null, checkoutOpened = false, error = message) }
    }
}
