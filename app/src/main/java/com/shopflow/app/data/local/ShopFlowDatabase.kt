package com.shopflow.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shopflow.app.data.local.dao.NotificationDao
import com.shopflow.app.data.local.dao.WishlistDao
import com.shopflow.app.data.local.entity.NotificationEntity
import com.shopflow.app.data.local.entity.WishlistItemEntity

@Database(
    entities = [WishlistItemEntity::class, NotificationEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShopFlowDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun notificationDao(): NotificationDao
}
