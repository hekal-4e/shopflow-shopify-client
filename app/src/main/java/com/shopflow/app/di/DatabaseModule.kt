package com.shopflow.app.di

import android.content.Context
import androidx.room.Room
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
        return Room.databaseBuilder(
            context,
            ShopFlowDatabase::class.java,
            "shopflow_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(database: ShopFlowDatabase): WishlistDao {
        return database.wishlistDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: ShopFlowDatabase): NotificationDao {
        return database.notificationDao()
    }
}
