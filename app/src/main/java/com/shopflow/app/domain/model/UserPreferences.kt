package com.shopflow.app.domain.model

data class UserPreferences(
    val onboardingCompleted: Boolean = false,
    val themeMode: String = "dark",
    val languageCode: String = "en",
    val biometricEnabled: Boolean = false,
    val pushNotifications: Boolean = true,
    val emailMarketing: Boolean = false
)
