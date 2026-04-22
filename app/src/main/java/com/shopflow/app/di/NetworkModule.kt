package com.shopflow.app.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.shopflow.app.BuildConfig
import com.shopflow.app.data.remote.interceptor.ShopifyAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: ShopifyAuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        val normalizedDomain = BuildConfig.SHOPIFY_STORE_DOMAIN
            .removePrefix("https://")
            .removePrefix("http://")
            .removeSuffix("/")

        val endpoint = "https://$normalizedDomain/api/2024-10/graphql.json"

        return ApolloClient.Builder()
            .serverUrl(endpoint)
            .okHttpClient(okHttpClient)
            .build()
    }
}
