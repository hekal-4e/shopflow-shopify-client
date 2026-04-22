package com.shopflow.app.presentation.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.Cart
import com.shopflow.app.domain.usecase.cart.GetCartUseCase
import com.shopflow.app.domain.usecase.cart.RemoveFromCartUseCase
import com.shopflow.app.domain.usecase.cart.UpdateCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getCartUseCase: GetCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase
) : ViewModel() {

    val cartState: StateFlow<Cart?> = getCartUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun updateQuantity(lineItemId: String, quantity: Int) {
        viewModelScope.launch {
            if (quantity <= 0) {
                removeFromCartUseCase(lineItemId)
            } else {
                updateCartUseCase(lineItemId, quantity)
            }
        }
    }

    fun removeItem(lineItemId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(lineItemId)
        }
    }
}
