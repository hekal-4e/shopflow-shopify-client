package com.shopflow.app.data.repository

import com.shopflow.app.data.local.datastore.PreferencesDataStore
import com.shopflow.app.domain.model.UserPreferences
import com.shopflow.app.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : PreferencesRepository {

    override fun getPreferences(): Flow<UserPreferences> {
        return preferencesDataStore.preferencesFlow
    }

    override suspend fun setThemeMode(mode: String) {
        preferencesDataStore.setThemeMode(mode)
    }

    override suspend fun setLanguageCode(code: String) {
        preferencesDataStore.setLanguageCode(code)
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        preferencesDataStore.setOnboardingCompleted(completed)
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        preferencesDataStore.setBiometricEnabled(enabled)
    }

    override suspend fun setPushNotifications(enabled: Boolean) {
        preferencesDataStore.setPushNotifications(enabled)
    }

    override suspend fun setEmailMarketing(enabled: Boolean) {
        preferencesDataStore.setEmailMarketing(enabled)
    }
}
