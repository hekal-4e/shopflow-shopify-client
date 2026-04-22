package com.shopflow.app.domain.usecase.notification

import com.shopflow.app.domain.model.Notification
import com.shopflow.app.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<List<Notification>> {
        return notificationRepository.getNotifications()
    }
}
