package com.shopflow.app.presentation.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.presentation.components.GradientButton
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import com.shopflow.app.presentation.theme.ShopFlowTheme

@Composable
fun OrderConfirmationScreen(
    onContinueShopping: () -> Unit,
    viewModel: OrderConfirmationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val order = uiState.order

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = NeonMagenta)
        } else if (uiState.error != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(uiState.error ?: "Failed to load order", color = ShopFlowTheme.colors.textPrimary)
                Spacer(modifier = Modifier.height(16.dp))
                GradientButton(text = "Go Back Home", onClick = onContinueShopping)
            }
        } else if (order != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(SurfaceGlass),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = NeonMagenta,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Order Confirmed!",
                    color = ShopFlowTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Your order #${order.orderNumber} has been placed successfully.",
                    color = TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                GradientButton(
                    text = "Continue Shopping",
                    onClick = onContinueShopping,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

