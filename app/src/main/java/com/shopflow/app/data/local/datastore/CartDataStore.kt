package com.shopflow.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.shopflow.app.domain.model.Cart
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.cartDataStore: DataStore<Preferences> by preferencesDataStore(name = "cart_state")

@Singleton
class CartDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.cartDataStore
    private val gson = Gson()

    val cartFlow: Flow<Cart?> = dataStore.data.map { preferences ->
        val json = preferences[CartKeys.CART_JSON] ?: return@map null
        runCatching { gson.fromJson(json, Cart::class.java) }.getOrNull()
    }

    suspend fun saveCart(cart: Cart) {
        dataStore.edit { preferences ->
            preferences[CartKeys.CART_JSON] = gson.toJson(cart)
        }
    }

    suspend fun clearCart() {
        dataStore.edit { preferences ->
            preferences.remove(CartKeys.CART_JSON)
        }
    }
}

private object CartKeys {
    val CART_JSON = stringPreferencesKey("cart_json")
}

