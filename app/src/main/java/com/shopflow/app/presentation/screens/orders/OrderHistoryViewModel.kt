package com.shopflow.app.presentation.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Order
import com.shopflow.app.domain.usecase.order.GetOrderHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OrderFilter {
    ALL, ACTIVE, DELIVERED, CANCELLED
}

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true,
    val filter: OrderFilter = OrderFilter.ALL,
    val error: String? = null
)

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrderHistoryUseCase: GetOrderHistoryUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val _filter = MutableStateFlow(OrderFilter.ALL)

    val uiState: StateFlow<OrderHistoryUiState> = combine(
        _orders, _isLoading, _filter, _error
    ) { orders, isLoading, filter, error ->
        val filteredOrders = when (filter) {
            OrderFilter.ALL -> orders
            OrderFilter.ACTIVE -> orders.filter { it.fulfillmentStatus == com.shopflow.app.domain.model.FulfillmentStatus.UNFULFILLED || it.fulfillmentStatus == com.shopflow.app.domain.model.FulfillmentStatus.PROCESSING || it.fulfillmentStatus == com.shopflow.app.domain.model.FulfillmentStatus.SHIPPED }
            OrderFilter.DELIVERED -> orders.filter { it.fulfillmentStatus == com.shopflow.app.domain.model.FulfillmentStatus.DELIVERED }
            OrderFilter.CANCELLED -> orders.filter { it.fulfillmentStatus == com.shopflow.app.domain.model.FulfillmentStatus.CANCELLED }
        }

        OrderHistoryUiState(
            orders = filteredOrders,
            isLoading = isLoading,
            filter = filter,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OrderHistoryUiState()
    )

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val result = getOrderHistoryUseCase()) {
                is ApiResult.Success -> {
                    _orders.value = result.data
                    _isLoading.value = false
                }
                is ApiResult.NetworkError -> {
                    _error.value = result.exception.message
                    _isLoading.value = false
                }
                is ApiResult.GraphQLError -> {
                    _error.value = "Failed to load order history"
                    _isLoading.value = false
                }
                is ApiResult.Empty -> {
                    _error.value = "No orders found"
                    _isLoading.value = false
                }
            }
        }
    }

    fun setFilter(filter: OrderFilter) {
        _filter.value = filter
    }
}
