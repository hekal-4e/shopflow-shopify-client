package com.shopflow.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "shopflow_tokens")

@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.tokenDataStore

    val accessTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TokenKeys.ACCESS_TOKEN]
    }

    val expiryTimestampFlow: Flow<Long> = dataStore.data.map { preferences ->
        preferences[TokenKeys.EXPIRY_TIMESTAMP] ?: 0L
    }

    suspend fun storeToken(token: String, expiryTimestamp: Long) {
        dataStore.edit { preferences ->
            preferences[TokenKeys.ACCESS_TOKEN] = token
            preferences[TokenKeys.EXPIRY_TIMESTAMP] = expiryTimestamp
        }
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.first()[TokenKeys.ACCESS_TOKEN]
    }

    suspend fun getExpiryTimestamp(): Long {
        return dataStore.data.first()[TokenKeys.EXPIRY_TIMESTAMP] ?: 0L
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TokenKeys.ACCESS_TOKEN)
            preferences.remove(TokenKeys.EXPIRY_TIMESTAMP)
        }
    }
}

private object TokenKeys {
    val ACCESS_TOKEN = stringPreferencesKey("customer_access_token")
    val EXPIRY_TIMESTAMP = longPreferencesKey("token_expiry_timestamp")
}
