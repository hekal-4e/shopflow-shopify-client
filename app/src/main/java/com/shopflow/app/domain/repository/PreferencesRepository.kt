package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getPreferences(): Flow<UserPreferences>
    suspend fun setThemeMode(mode: String)
    suspend fun setLanguageCode(code: String)
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setBiometricEnabled(enabled: Boolean)
    suspend fun setPushNotifications(enabled: Boolean)
    suspend fun setEmailMarketing(enabled: Boolean)
}
