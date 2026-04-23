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
import com.shopflow.app.presentation.screens.home.HomeScreen
import com.shopflow.app.presentation.screens.product.ProductDetailScreen
import com.shopflow.app.presentation.screens.cart.CartScreen
import com.shopflow.app.presentation.screens.checkout.CheckoutScreen
import com.shopflow.app.presentation.screens.order.OrderConfirmationScreen
import com.shopflow.app.presentation.screens.wishlist.WishlistScreen
import com.shopflow.app.presentation.screens.profile.ProfileScreen
import com.shopflow.app.presentation.screens.settings.SettingsScreen
import com.shopflow.app.presentation.screens.notifications.NotificationScreen
import com.shopflow.app.presentation.screens.onboarding.OnboardingScreen
import com.shopflow.app.presentation.screens.splash.SplashScreen
import com.shopflow.app.presentation.screens.auth.AuthViewModel
import com.shopflow.app.presentation.screens.auth.LoginScreen
import com.shopflow.app.presentation.screens.auth.RegisterScreen
import com.shopflow.app.presentation.theme.TextPrimary
import com.shopflow.app.presentation.theme.TrueBlack
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ShopFlowNavGraph(
    startDestination: String = Route.Splash.route
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val authViewModel: AuthViewModel = hiltViewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

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
            composable(Route.Onboarding.route) {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Login.route) { 
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Route.Register.route)
                    },
                    onLoginSuccess = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Register.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Home.route) {
                HomeScreen(
                    onNavigateToProductDetail = { productId ->
                        navController.navigate(Route.ProductDetail.createRoute(productId))
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Route.Notifications.route)
                    }
                )
            }
            composable(Route.Search.route) {
                HomeScreen(
                    onNavigateToProductDetail = { productId ->
                        navController.navigate(Route.ProductDetail.createRoute(productId))
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Route.Notifications.route)
                    }
                )
            }
            composable(Route.ProductDetail.route) { backStackEntry ->
                val productId =
                    backStackEntry.arguments?.getString(Route.ProductDetail.ARG_PRODUCT_ID).orEmpty()
                ProductDetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Route.Cart.route) }
                )
            }
            composable(Route.Cart.route) {
                CartScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onProceedToCheckout = { navController.navigate(Route.Checkout.route) }
                )
            }
            composable(Route.Checkout.route) {
                LaunchedEffect(isAuthenticated) {
                    if (!isAuthenticated) {
                        navController.navigate(Route.Login.route)
                    }
                }
                if (isAuthenticated) {
                    CheckoutScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
            composable(Route.OrderConfirmation.route) { backStackEntry ->
                val orderId =
                    backStackEntry.arguments?.getString(Route.OrderConfirmation.ARG_ORDER_ID).orEmpty()
                OrderConfirmationScreen(
                    onContinueShopping = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Home.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Route.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Route.Settings.route) },
                    onNavigateToOrders = { navController.navigate(Route.OrderHistory.route) },
                    onNavigateToWishlist = { navController.navigate(Route.Wishlist.route) }
                )
            }
            composable(Route.OrderHistory.route) {
                com.shopflow.app.presentation.screens.orders.OrderHistoryScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToOrderDetails = { orderId ->
                        navController.navigate(Route.OrderConfirmation.createRoute(orderId))
                    }
                )
            }
            composable(Route.Wishlist.route) {
                WishlistScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProduct = { id ->
                        navController.navigate(Route.ProductDetail.createRoute(id))
                    }
                )
            }
            composable(Route.Notifications.route) {
                NotificationScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDeepLink = { deepLink ->
                        // Handle deep link navigation
                        if (deepLink?.startsWith("shopflow://order/") == true) {
                            val orderId = deepLink.substringAfterLast("/")
                            navController.navigate(Route.OrderConfirmation.createRoute(orderId))
                        }
                    }
                )
            }
            composable(Route.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
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
