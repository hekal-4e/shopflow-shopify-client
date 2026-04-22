package com.shopflow.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.model.Customer
import com.shopflow.app.domain.usecase.profile.GetCustomerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val customer: Customer? = null,
    val error: String? = null,
    val orderCount: Int = 12,
    val wishlistCount: Int = 8,
    val points: Int = 450
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCustomerProfileUseCase: GetCustomerProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCustomerProfileUseCase()) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, customer = result.data) }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.message) }
                }
                is ApiResult.GraphQLError -> {
                    _uiState.update { it.copy(isLoading = false, error = "Failed to load profile") }
                }
                is ApiResult.Empty -> {
                    _uiState.update { it.copy(isLoading = false, error = "Profile not found") }
                }
            }
        }
    }
}
