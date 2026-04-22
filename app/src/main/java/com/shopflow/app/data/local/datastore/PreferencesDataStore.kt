package com.shopflow.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shopflow.app.domain.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.preferencesDataStore

    val preferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            onboardingCompleted = preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false,
            themeMode = preferences[PreferencesKeys.THEME_MODE] ?: "dark",
            languageCode = preferences[PreferencesKeys.LANGUAGE_CODE] ?: "en",
            biometricEnabled = preferences[PreferencesKeys.BIOMETRIC_ENABLED] ?: false,
            pushNotifications = preferences[PreferencesKeys.PUSH_NOTIFICATIONS] ?: true,
            emailMarketing = preferences[PreferencesKeys.EMAIL_MARKETING] ?: false
        )
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(PreferencesKeys.ONBOARDING_COMPLETED, completed)
            }
        }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(PreferencesKeys.THEME_MODE, mode)
            }
        }
    }

    suspend fun setLanguageCode(code: String) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(PreferencesKeys.LANGUAGE_CODE, code)
            }
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(PreferencesKeys.BIOMETRIC_ENABLED, enabled)
            }
        }
    }

    suspend fun setPushNotifications(enabled: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(PreferencesKeys.PUSH_NOTIFICATIONS, enabled)
            }
        }
    }

    suspend fun setEmailMarketing(enabled: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(PreferencesKeys.EMAIL_MARKETING, enabled)
            }
        }
    }
}

private object PreferencesKeys {
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val LANGUAGE_CODE = stringPreferencesKey("language_code")
    val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
    val EMAIL_MARKETING = booleanPreferencesKey("email_marketing")
}
