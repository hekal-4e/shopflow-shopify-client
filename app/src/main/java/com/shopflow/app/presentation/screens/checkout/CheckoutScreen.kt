package com.shopflow.app.presentation.screens.checkout

import android.content.ActivityNotFoundException
import android.net.Uri
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
                onNavigateBack()
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
        }
    }
}

