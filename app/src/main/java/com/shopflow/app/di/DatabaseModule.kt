package com.shopflow.app.di

import android.content.Context
import com.shopflow.app.data.local.ShopFlowDatabase
import com.shopflow.app.data.local.dao.NotificationDao
import com.shopflow.app.data.local.dao.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShopFlowDatabase {
        // TODO: provide Room.databaseBuilder(...).build().
        return TODO("Wire ShopFlowDatabase in local data phase.")
    }

    @Provides
    fun provideWishlistDao(database: ShopFlowDatabase): WishlistDao {
        // TODO: return database.wishlistDao().
        return TODO("Wire WishlistDao in local data phase.")
    }

    @Provides
    fun provideNotificationDao(database: ShopFlowDatabase): NotificationDao {
        // TODO: return database.notificationDao().
        return TODO("Wire NotificationDao in local data phase.")
    }
}
