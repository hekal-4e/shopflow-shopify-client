package com.shopflow.app.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopflow.app.domain.model.UserPreferences
import com.shopflow.app.domain.repository.PreferencesRepository
import com.shopflow.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.getPreferences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun togglePushNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setPushNotifications(enabled)
        }
    }

    fun toggleDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(if (enabled) "DARK" else "LIGHT")
        }
    }

    fun toggleBiometrics(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setBiometricEnabled(enabled)
        }
    }

    fun toggleEmailMarketing(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setEmailMarketing(enabled)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            preferencesRepository.setLanguageCode(languageCode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
