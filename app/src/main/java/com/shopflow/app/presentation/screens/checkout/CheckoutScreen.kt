package com.shopflow.app.presentation.screens.checkout

import android.content.ActivityNotFoundException
import android.net.Uri
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.presentation.components.GradientButton
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.TrueBlack
import com.shopflow.app.presentation.theme.ShopFlowTheme

@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.createCheckout()
    }

    LaunchedEffect(uiState.checkoutUrl) {
        val url = uiState.checkoutUrl
        if (url != null) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                }
                context.startActivity(intent)
                viewModel.onCheckoutUrlHandled()
            } catch (_: ActivityNotFoundException) {
                viewModel.onCheckoutLaunchFailed("No browser app found to open checkout.")
            } catch (e: Exception) {
                viewModel.onCheckoutLaunchFailed(
                    e.message ?: "Could not open checkout. Please try again."
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isCreatingCheckout) {
            CircularProgressIndicator(color = NeonMagenta)
        } else if (uiState.error != null) {
            Text(text = uiState.error ?: "Error", color = ShopFlowTheme.colors.textPrimary)
        } else if (uiState.checkoutOpened) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Checkout opened in your browser.",
                    color = ShopFlowTheme.colors.textPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Complete payment, then return to the app. If Shopify redirects to app deep link, order confirmation will open automatically.",
                    color = ShopFlowTheme.colors.textSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                GradientButton(
                    text = "Back to Cart",
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

