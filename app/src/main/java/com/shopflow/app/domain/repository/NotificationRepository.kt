package com.shopflow.app.domain.repository

import com.shopflow.app.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun addNotification(notification: Notification)
    suspend fun markAsRead(id: Long)
    fun getUnreadCount(): Flow<Int>
}
