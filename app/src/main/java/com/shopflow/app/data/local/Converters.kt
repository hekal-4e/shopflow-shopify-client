package com.shopflow.app.data.local

import androidx.room.TypeConverter
import com.shopflow.app.data.local.entity.NotificationType

class Converters {
    @TypeConverter
    fun fromNotificationType(value: NotificationType): String = value.name

    @TypeConverter
    fun toNotificationType(value: String): NotificationType = NotificationType.valueOf(value)
}
