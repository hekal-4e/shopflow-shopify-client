package com.shopflow.app.presentation.screens.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.ApiResult
import com.shopflow.app.domain.usecase.auth.LoginUseCase
import com.shopflow.app.domain.usecase.auth.RegisterUseCase
import com.shopflow.app.domain.usecase.auth.GetAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {

    val isAuthenticated: StateFlow<Boolean> = getAuthStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow: SharedFlow<String> = _errorFlow.asSharedFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            emitError("Email and password cannot be empty")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emitError("Invalid email format")
            return
        }

        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }
            when (val result = loginUseCase(email, pass)) {
                is ApiResult.Success -> {
                    _uiState.update { AuthUiState.Success }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update { AuthUiState.Idle }
                    emitError(result.exception.message ?: "Network error")
                }
                is ApiResult.GraphQLError -> {
                    _uiState.update { AuthUiState.Idle }
                    emitError(result.errors.firstOrNull() ?: "Login failed")
                }
                is ApiResult.Empty -> {
                    _uiState.update { AuthUiState.Idle }
                }
            }
        }
    }

    fun register(firstName: String, lastName: String, email: String, pass: String) {
        if (firstName.isBlank() || email.isBlank() || pass.isBlank()) {
            emitError("Please fill in all required fields")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emitError("Invalid email format")
            return
        }
        if (pass.length < 6) {
            emitError("Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }
            when (val result = registerUseCase(firstName, lastName, email, pass)) {
                is ApiResult.Success -> {
                    _uiState.update { AuthUiState.Success }
                }
                is ApiResult.NetworkError -> {
                    _uiState.update { AuthUiState.Idle }
                    emitError(result.exception.message ?: "Network error")
                }
                is ApiResult.GraphQLError -> {
                    _uiState.update { AuthUiState.Idle }
                    emitError(result.errors.firstOrNull() ?: "Registration failed")
                }
                is ApiResult.Empty -> {
                    _uiState.update { AuthUiState.Idle }
                }
            }
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            _errorFlow.emit(message)
        }
    }
}
