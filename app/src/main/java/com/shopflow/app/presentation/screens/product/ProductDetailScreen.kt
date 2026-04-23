package com.shopflow.app.presentation.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shopflow.app.domain.model.Money
import com.shopflow.app.presentation.components.ChipSelector
import com.shopflow.app.presentation.components.GradientButton
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.ElectricViolet
import com.shopflow.app.presentation.theme.StatusProcessing
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.SurfaceGlassElevated
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import com.shopflow.app.presentation.theme.ShopFlowTheme
import java.text.NumberFormat
import java.util.Currency

private fun Money.format(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(this.currencyCode)
    return format.format(this.amount)
}

@Composable
fun ProductDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product

    var isWishlisted by remember { mutableStateOf(false) }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(TrueBlack), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NeonMagenta)
        }
        return
    }

    if (uiState.error != null || product == null) {
        Box(modifier = Modifier.fillMaxSize().background(TrueBlack), contentAlignment = Alignment.Center) {
            Text(uiState.error ?: "Product not found", color = ShopFlowTheme.colors.textPrimary)
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(TrueBlack)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // space for bottom bar
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // Glow effect behind image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NeonMagenta.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )
                )

                AsyncImage(
                    model = product.images.firstOrNull()?.url ?: "",
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Bar Icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceGlassElevated)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ShopFlowTheme.colors.textPrimary)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { /* Share */ },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceGlassElevated)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = ShopFlowTheme.colors.textPrimary)
                        }
                        IconButton(
                            onClick = { isWishlisted = !isWishlisted },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceGlassElevated)
                        ) {
                            Icon(
                                if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isWishlisted) NeonMagenta else ShopFlowTheme.colors.textPrimary
                            )
                        }
                    }
                }
            }

            // Product Info
            Column(modifier = Modifier.padding(24.dp)) {
                // Brand Breadcrumb
                if (product.brand != null) {
                    Text(
                        text = product.brand.uppercase(),
                        color = NeonMagenta,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = ShopFlowTheme.colors.textPrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .background(SurfaceGlassElevated, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = uiState.selectedVariant?.price?.format() ?: product.priceRange.minPrice.format(),
                            color = ShopFlowTheme.colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < 4) StatusProcessing else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("4.2 (128 reviews)", color = TextSecondary, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Dynamic Options (Size, Color)
                val allOptions = product.variants.flatMap { it.selectedOptions }.groupBy { it.name }.mapValues { it.value.map { opt -> opt.value }.distinct() }

                allOptions.forEach { (optionName, optionValues) ->
                    Text(
                        text = optionName.uppercase(),
                        color = ShopFlowTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (optionName.equals("color", ignoreCase = true)) {
                        // Color Swatches
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(optionValues) { value ->
                                val isSelected = uiState.selectedOptions[optionName] == value
                                // Mock color mapping
                                val colorMap = mapOf(
                                    "Black" to Color.Black,
                                    "White" to Color.White,
                                    "Red" to Color.Red,
                                    "Blue" to Color.Blue
                                )
                                val color = colorMap[value] ?: Color.Gray

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = 2.dp,
                                            color = if (isSelected) NeonMagenta else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { viewModel.selectOption(optionName, value) }
                                )
                            }
                        }
                    } else {
                        // Chips for Size, etc.
                        ChipSelector(
                            options = optionValues,
                            selectedOption = uiState.selectedOptions[optionName] ?: "",
                            onSelect = { viewModel.selectOption(optionName, it) }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Description
                Text("DESCRIPTION", color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
        }

        // Bottom Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, TrueBlack, TrueBlack)
                    )
                )
                .padding(24.dp)
        ) {
            GradientButton(
                text = "Add to Cart",
                onClick = {
                    viewModel.addToCart()
                    onNavigateToCart()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

