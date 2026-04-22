package com.shopflow.app.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // TODO: add auth/header interceptors and timeout policy.
        return TODO("Wire OkHttpClient in network phase.")
    }

    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        // TODO: wire Apollo endpoint and Shopify auth strategy.
        return TODO("Wire ApolloClient in network phase.")
    }
}
