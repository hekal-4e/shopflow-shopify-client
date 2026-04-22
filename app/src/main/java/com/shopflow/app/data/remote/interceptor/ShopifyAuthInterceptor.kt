package com.shopflow.app.data.remote.interceptor

import com.shopflow.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ShopifyAuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Shopify-Storefront-Access-Token", BuildConfig.SHOPIFY_STOREFRONT_ACCESS_TOKEN)
            .build()
        return chain.proceed(request)
    }
}
