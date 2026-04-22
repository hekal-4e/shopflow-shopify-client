package com.shopflow.app.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shopflow.app.presentation.theme.ShopFlowGradient
import com.shopflow.app.presentation.theme.ShopFlowTheme
import com.shopflow.app.presentation.theme.TextPrimary
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack

private data class BottomTab(
    val route: String,
    val icon: ImageVector
)

private val tabs = listOf(
    BottomTab(Route.Home.route, Icons.Default.Home),
    BottomTab(Route.Search.route, Icons.Default.Search),
    BottomTab(Route.Wishlist.route, Icons.Default.Favorite),
    BottomTab(Route.Cart.route, Icons.Default.ShoppingBag),
    BottomTab(Route.Profile.route, Icons.Default.Person)
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = TrueBlack
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(tab.route) },
                icon = {
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(ShopFlowGradient)
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null
                            )
                        }
                    } else {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null
                        )
                    }
                },
                label = null,
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TextPrimary,
                    unselectedIconColor = TextSecondary,
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    ShopFlowTheme {
        BottomNavBar(
            currentRoute = Route.Home.route,
            onNavigate = {}
        )
    }
}
