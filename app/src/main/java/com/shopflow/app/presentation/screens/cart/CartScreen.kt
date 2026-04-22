package com.shopflow.app.presentation.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.shopflow.app.domain.model.Money
import com.shopflow.app.presentation.components.GradientButton
import com.shopflow.app.presentation.components.QuantityStepper
import com.shopflow.app.presentation.components.StepIndicator
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.SurfaceGlassElevated
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import java.text.NumberFormat
import java.util.Currency

private fun Money.format(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(this.currencyCode)
    return format.format(this.amount)
}

@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onProceedToCheckout: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val cart by viewModel.cartState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Shopping Cart",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        if (cart == null || cart?.lineItems.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty.", color = TextSecondary, fontSize = 16.sp)
            }
            return
        }

        // Step Indicator
        StepIndicator(
            steps = listOf("Cart", "Address", "Payment"),
            currentStep = 0,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cart!!.lineItems) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceGlass)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.variant.image?.url ?: item.product.images.firstOrNull()?.url ?: "",
                            contentDescription = item.product.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceGlassElevated)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.variant.title,
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.total.format(),
                                color = NeonMagenta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.height(80.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.removeItem(item.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = TextSecondary)
                            }
                            QuantityStepper(
                                quantity = item.quantity,
                                onIncrement = { viewModel.updateQuantity(item.id, item.quantity + 1) },
                                onDecrement = { viewModel.updateQuantity(item.id, item.quantity - 1) }
                            )
                        }
                    }
                }
            }
        }

        // Order Summary
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceGlass)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", color = TextSecondary, fontSize = 14.sp)
                Text(cart!!.subtotal.format(), color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Taxes & Fees", color = TextSecondary, fontSize = 14.sp)
                Text("Calculated at checkout", color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = SurfaceGlassElevated)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(cart!!.subtotal.format(), color = NeonMagenta, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            GradientButton(
                text = "Proceed to Checkout",
                onClick = onProceedToCheckout,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
