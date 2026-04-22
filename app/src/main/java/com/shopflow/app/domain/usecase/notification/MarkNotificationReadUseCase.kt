package com.shopflow.app.domain.usecase.notification

import com.shopflow.app.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(id: Long) {
        notificationRepository.markAsRead(id)
    }
}
