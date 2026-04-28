package com.shopflow.app.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SplashDecision(
    val isReady: Boolean = false,
    val hasCompletedOnboarding: Boolean = false,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val decision: StateFlow<SplashDecision> = combine(
        preferencesRepository.getPreferences(),
        authRepository.getAuthState()
    ) { prefs, authed ->
        SplashDecision(
            isReady = true,
            hasCompletedOnboarding = prefs.onboardingCompleted,
            isAuthenticated = authed
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SplashDecision()
    )
}
