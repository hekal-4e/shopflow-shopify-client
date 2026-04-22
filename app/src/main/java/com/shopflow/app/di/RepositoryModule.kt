package com.shopflow.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // TODO: add @Binds mappings for:
    // ProductRepository, CartRepository, AuthRepository, CheckoutRepository,
    // WishlistRepository, ProfileRepository, OrdersRepository, SettingsRepository.
}
