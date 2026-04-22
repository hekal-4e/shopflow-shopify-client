package com.shopflow.app.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shopflow.app.presentation.screens.splash.SplashScreen
import com.shopflow.app.presentation.theme.TextPrimary
import com.shopflow.app.presentation.theme.TrueBlack

@Composable
fun ShopFlowNavGraph(
    startDestination: String = Route.Splash.route
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val bottomNavRoutes = setOf(
        Route.Home.route,
        Route.Search.route,
        Route.Wishlist.route,
        Route.Cart.route,
        Route.Profile.route
    )

    Scaffold(
        containerColor = TrueBlack,
        bottomBar = {
            val showBottomBar = currentDestination?.hierarchy?.any { destination ->
                destination.route in bottomNavRoutes
            } == true

            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Splash.route) {
                SplashScreen(
                    onGetStarted = {
                        navController.navigate(Route.Onboarding.route) {
                            popUpTo(Route.Splash.route) { inclusive = true }
                        }
                    },
                    onSignIn = {
                        navController.navigate(Route.Login.route) {
                            popUpTo(Route.Splash.route) { inclusive = true }
                        }
                    },
                    onAutoNavigateHome = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Onboarding.route) { PlaceholderScreen("Onboarding") }
            composable(Route.Login.route) { PlaceholderScreen("Login") }
            composable(Route.Register.route) { PlaceholderScreen("Register") }
            composable(Route.Home.route) { PlaceholderScreen("Home") }
            composable(Route.Search.route) { PlaceholderScreen("Search") }
            composable(Route.ProductDetail.route) { backStackEntry ->
                val productId =
                    backStackEntry.arguments?.getString(Route.ProductDetail.ARG_PRODUCT_ID).orEmpty()
                PlaceholderScreen("Product Detail: $productId")
            }
            composable(Route.Cart.route) { PlaceholderScreen("Cart") }
            composable(Route.Checkout.route) { PlaceholderScreen("Checkout") }
            composable(Route.OrderConfirmation.route) { backStackEntry ->
                val orderId =
                    backStackEntry.arguments?.getString(Route.OrderConfirmation.ARG_ORDER_ID).orEmpty()
                PlaceholderScreen("Order Confirmation: $orderId")
            }
            composable(Route.Profile.route) { PlaceholderScreen("Profile") }
            composable(Route.OrderHistory.route) { PlaceholderScreen("Order History") }
            composable(Route.Wishlist.route) { PlaceholderScreen("Wishlist") }
            composable(Route.Notifications.route) { PlaceholderScreen("Notifications") }
            composable(Route.Settings.route) { PlaceholderScreen("Settings") }
        }
    }
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = TextPrimary
        )
    }
}
