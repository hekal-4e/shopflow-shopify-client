package com.shopflow.app.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shopflow.app.domain.model.Product
import com.shopflow.app.domain.model.Money
import java.text.NumberFormat
import java.util.Currency
import com.shopflow.app.presentation.components.CategoryIcon
import com.shopflow.app.presentation.components.ProductCard
import com.shopflow.app.presentation.components.SearchBar
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import com.shopflow.app.presentation.theme.ShopFlowTheme

private fun Money.format(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(this.currencyCode)
    return format.format(this.amount)
}

@Composable
fun HomeScreen(
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
            .padding(top = 16.dp)
    ) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonMagenta)
            }
        }
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SurfaceGlass)
                ) {
                    AsyncImage(
                        model = uiState.customerAvatarUrl ?: "https://i.pravatar.cc/150",
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Good morning,", color = TextSecondary, fontSize = 12.sp)
                    Text(if (uiState.customerName.isNotEmpty()) uiState.customerName else "Guest", color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // Notification Badge
            IconButton(onClick = onNavigateToNotifications) {
                BadgedBox(
                    badge = {
                        if (uiState.unreadNotificationCount > 0) {
                            Badge(containerColor = NeonMagenta, contentColor = ShopFlowTheme.colors.textPrimary) {
                                Text("${uiState.unreadNotificationCount}")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = ShopFlowTheme.colors.textPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Search products...",
                onMicClick = { },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isSearchMode) {
            // Search Results
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.searchResults) { product ->
                    ProductCard(
                        imageUrl = product.images.firstOrNull()?.url ?: "",
                        name = product.title,
                        price = product.priceRange.minPrice.format(),
                        isPremium = false,
                        isWishlisted = uiState.wishlistedProductIds.contains(product.id),
                        onWishlistToggle = { viewModel.toggleWishlist(product) },
                        onAddToCart = { viewModel.addToCart(product) },
                        onClick = { onNavigateToProductDetail(product.id) }
                    )
                }
            }
        } else {
            // Normal Home
            val pagerState = rememberPagerState(pageCount = { uiState.banners.size.coerceAtLeast(1) })
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // We use spans for full width items in grid
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    Column {
                        // Hero Banners
                        if (uiState.banners.isNotEmpty()) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) { page ->
                                AsyncImage(
                                    model = uiState.banners[page],
                                    contentDescription = "Banner",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Categories
                        if (uiState.collections.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Categories", color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("See All", color = NeonMagenta, fontSize = 14.sp, modifier = Modifier.clickable { })
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(uiState.collections) { collection ->
                                    CategoryIcon(
                                        icon = Icons.Default.ShoppingBag,
                                        label = collection.title,
                                        isActive = false,
                                        onClick = { /* Navigate to collection */ }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        // Featured Products Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Featured", color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                items(uiState.featuredProducts) { product ->
                    ProductCard(
                        imageUrl = product.images.firstOrNull()?.url ?: "",
                        name = product.title,
                        price = product.priceRange.minPrice.format(),
                        isPremium = false,
                        isWishlisted = uiState.wishlistedProductIds.contains(product.id),
                        onWishlistToggle = { viewModel.toggleWishlist(product) },
                        onAddToCart = { viewModel.addToCart(product) },
                        onClick = { onNavigateToProductDetail(product.id) }
                    )
                }
            }
        }
    }
}

