package com.shopflow.app.di

import android.content.Context
import com.shopflow.app.data.local.datastore.CartDataStore
import com.shopflow.app.data.local.datastore.PreferencesDataStore
import com.shopflow.app.data.local.datastore.TokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): PreferencesDataStore {
        return PreferencesDataStore(context)
    }

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): TokenDataStore {
        return TokenDataStore(context)
    }

    @Provides
    @Singleton
    fun provideCartDataStore(
        @ApplicationContext context: Context
    ): CartDataStore {
        return CartDataStore(context)
    }
}
