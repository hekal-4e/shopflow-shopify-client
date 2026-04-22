package com.shopflow.app.di

import com.shopflow.app.data.repository.AuthRepositoryImpl
import com.shopflow.app.data.repository.CheckoutRepositoryImpl
import com.shopflow.app.data.repository.CustomerRepositoryImpl
import com.shopflow.app.data.repository.NotificationRepositoryImpl
import com.shopflow.app.data.repository.OrderRepositoryImpl
import com.shopflow.app.data.repository.PreferencesRepositoryImpl
import com.shopflow.app.data.repository.ProductRepositoryImpl
import com.shopflow.app.data.repository.WishlistRepositoryImpl
import com.shopflow.app.data.repository.CartRepositoryImpl
import com.shopflow.app.domain.repository.AuthRepository
import com.shopflow.app.domain.repository.CheckoutRepository
import com.shopflow.app.domain.repository.CustomerRepository
import com.shopflow.app.domain.repository.NotificationRepository
import com.shopflow.app.domain.repository.OrderRepository
import com.shopflow.app.domain.repository.PreferencesRepository
import com.shopflow.app.domain.repository.ProductRepository
import com.shopflow.app.domain.repository.WishlistRepository
import com.shopflow.app.domain.repository.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(impl: CustomerRepositoryImpl): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindCheckoutRepository(impl: CheckoutRepositoryImpl): CheckoutRepository

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(impl: WishlistRepositoryImpl): WishlistRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}
