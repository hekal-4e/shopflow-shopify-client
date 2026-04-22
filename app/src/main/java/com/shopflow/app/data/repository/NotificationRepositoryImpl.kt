package com.shopflow.app.data.repository

import com.shopflow.app.data.local.dao.NotificationDao
import com.shopflow.app.data.local.entity.NotificationEntity
import com.shopflow.app.data.local.entity.NotificationType as EntityNotificationType
import com.shopflow.app.domain.model.Notification
import com.shopflow.app.domain.model.NotificationType
import com.shopflow.app.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getNotifications(): Flow<List<Notification>> {
        return notificationDao.getAll().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addNotification(notification: Notification) {
        notificationDao.insert(notification.toEntity())
    }

    override suspend fun markAsRead(id: Long) {
        notificationDao.markAsRead(id)
    }

    override fun getUnreadCount(): Flow<Int> {
        return notificationDao.getUnreadCount()
    }
}

private fun NotificationEntity.toDomainModel(): Notification = Notification(
    id = id,
    title = title,
    body = body,
    type = type.toDomainType(),
    referenceId = referenceId,
    isRead = isRead,
    createdAt = createdAt
)

private fun NotificationType.toEntityType(): EntityNotificationType = when (this) {
    NotificationType.ORDER_UPDATE -> EntityNotificationType.ORDER_UPDATE
    NotificationType.PROMOTION -> EntityNotificationType.PROMOTION
    NotificationType.WISHLIST_PRICE_DROP -> EntityNotificationType.WISHLIST_PRICE_DROP
}

private fun EntityNotificationType.toDomainType(): NotificationType = when (this) {
    EntityNotificationType.ORDER_UPDATE -> NotificationType.ORDER_UPDATE
    EntityNotificationType.PROMOTION -> NotificationType.PROMOTION
    EntityNotificationType.WISHLIST_PRICE_DROP -> NotificationType.WISHLIST_PRICE_DROP
}

private fun Notification.toEntity(): NotificationEntity = NotificationEntity(
    id = id,
    title = title,
    body = body,
    type = type.toEntityType(),
    referenceId = referenceId,
    isRead = isRead,
    createdAt = createdAt
)
