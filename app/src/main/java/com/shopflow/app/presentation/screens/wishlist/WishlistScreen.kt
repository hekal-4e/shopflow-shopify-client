package com.shopflow.app.presentation.screens.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.presentation.components.ChipSelector
import com.shopflow.app.presentation.components.GradientButton
import com.shopflow.app.presentation.components.ProductCard
import com.shopflow.app.presentation.theme.ShopFlowTheme
import com.shopflow.app.presentation.theme.TrueBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ShopFlowTheme.colors.textPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Wishlist", color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            IconButton(onClick = { showFilterSheet = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = ShopFlowTheme.colors.textPrimary)
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your wishlist is empty.", color = ShopFlowTheme.colors.textSecondary)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.products) { item ->
                    ProductCard(
                        imageUrl = item.imageUrl,
                        name = item.title,
                        price = "$${item.price}",
                        isPremium = false,
                        isWishlisted = true,
                        onWishlistToggle = { viewModel.removeProduct(item.productId) },
                        onAddToCart = { /* Add to cart */ },
                        onClick = { onNavigateToProduct(item.productId) }
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = ShopFlowTheme.colors.surfaceGlassElevated
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Filter & Sort", color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Text("SORT BY", color = ShopFlowTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                ChipSelector(
                    options = listOf("Newest", "Price: Low to High", "Price: High to Low"),
                    selectedOption = when (uiState.sortOption) {
                        SortOption.NEWEST -> "Newest"
                        SortOption.PRICE_LOW_HIGH -> "Price: Low to High"
                        SortOption.PRICE_HIGH_LOW -> "Price: High to Low"
                    },
                    onSelect = {
                        val newSort = when (it) {
                            "Price: Low to High" -> SortOption.PRICE_LOW_HIGH
                            "Price: High to Low" -> SortOption.PRICE_HIGH_LOW
                            else -> SortOption.NEWEST
                        }
                        viewModel.setSortOption(newSort)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("PRICE RANGE", color = ShopFlowTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                // simplified for demonstration
                var sliderPosition by remember { mutableStateOf(0f..1000f) }
                RangeSlider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..1000f,
                    steps = 100
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("$${sliderPosition.start.toInt()}", color = ShopFlowTheme.colors.textPrimary)
                    Text("$${sliderPosition.endInclusive.toInt()}", color = ShopFlowTheme.colors.textPrimary)
                }

                Spacer(modifier = Modifier.height(32.dp))

                GradientButton(
                    text = "Apply Filters",
                    onClick = {
                        viewModel.setPriceRangeFilter(sliderPosition)
                        showFilterSheet = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

