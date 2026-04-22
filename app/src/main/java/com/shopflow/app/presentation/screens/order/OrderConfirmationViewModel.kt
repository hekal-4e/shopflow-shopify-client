package com.shopflow.app.presentation.screens.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.repository.CartRepository
import com.shopflow.app.domain.usecase.order.GetOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderConfirmationUiState(
    val isLoading: Boolean = true,
    val order: Order? = null,
    val error: String? = null
)

@HiltViewModel
class OrderConfirmationViewModel @Inject constructor(
    private val getOrderUseCase: GetOrderUseCase,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    private val _uiState = MutableStateFlow(OrderConfirmationUiState())
    val uiState: StateFlow<OrderConfirmationUiState> = _uiState.asStateFlow()

    init {
        loadOrder()
    }

    private fun loadOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getOrderUseCase(orderId)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, order = result.data) }
                    cartRepository.clearCart() // Clear cart on successful order
                }
                is ApiResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.message) }
                }
                is ApiResult.GraphQLError -> {
                    _uiState.update { it.copy(isLoading = false, error = "Failed to load order") }
                }
                is ApiResult.Empty -> {
                    _uiState.update { it.copy(isLoading = false, error = "Order not found") }
                }
            }
        }
    }
}
