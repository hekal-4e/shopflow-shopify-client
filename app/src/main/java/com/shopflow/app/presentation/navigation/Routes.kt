package com.shopflow.app.presentation.navigation

import java.net.URLEncoder

sealed interface Route {
    val route: String

    data object Splash : Route { override val route: String = "splash" }
    data object Onboarding : Route { override val route: String = "onboarding" }
    data object Login : Route { override val route: String = "login" }
    data object Register : Route { override val route: String = "register" }
    data object Home : Route { override val route: String = "home" }
    data object Search : Route { override val route: String = "search" }

    data object ProductDetail : Route {
        const val ARG_PRODUCT_ID = "productId"
        override val route: String = "product/{$ARG_PRODUCT_ID}"
        fun createRoute(productId: String): String = "product/${URLEncoder.encode(productId, "UTF-8")}"
    }

    data object Cart : Route { override val route: String = "cart" }
    data object Checkout : Route { override val route: String = "checkout" }

    data object OrderConfirmation : Route {
        const val ARG_ORDER_ID = "orderId"
        override val route: String = "order-confirmation/{$ARG_ORDER_ID}"
        fun createRoute(orderId: String): String = "order-confirmation/${URLEncoder.encode(orderId, "UTF-8")}"
    }

    data object Profile : Route { override val route: String = "profile" }
    data object OrderHistory : Route { override val route: String = "order-history" }
    data object Wishlist : Route { override val route: String = "wishlist" }
    data object Notifications : Route { override val route: String = "notifications" }
    data object Settings : Route { override val route: String = "settings" }
}
