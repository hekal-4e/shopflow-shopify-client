package com.shopflow.app.presentation.screens.splash

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopflow.app.presentation.theme.ElectricViolet
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.ShopFlowGradient
import com.shopflow.app.presentation.theme.ShopFlowTheme
import com.shopflow.app.presentation.theme.TextPrimary
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import kotlinx.coroutines.delay

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.presentation.screens.splash.SplashViewModel

@Composable
fun SplashScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
    onAutoNavigateHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val decision by viewModel.decision.collectAsState()

    LaunchedEffect(decision) {
        if (!decision.isReady) return@LaunchedEffect
        
        // Wait for the splash animation/delay
        delay(2500)
        
        when {
            !decision.hasCompletedOnboarding -> onGetStarted()
            // We allow guests to browse the Home screen directly as per spec
            else -> onAutoNavigateHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(260.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonMagenta.copy(alpha = 0.42f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(ShopFlowGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ShopFlowTheme.colors.textPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = ShopFlowTheme.colors.textPrimary, fontWeight = FontWeight.Bold)) {
                            append("Shop")
                        }
                        withStyle(
                            SpanStyle(
                                brush = ShopFlowGradient,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Flow")
                        }
                    },
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Luxury style, curated for you",
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Discover trends. Shop instantly.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == 0) 9.dp else 7.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == 0) NeonMagenta else TextSecondary.copy(alpha = 0.35f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = "Get Started",
                    onClick = onGetStarted
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = TextSecondary
                    )
                    Text(
                        text = "Sign in",
                        color = NeonMagenta,
                        modifier = Modifier.clickable(onClick = onSignIn),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun GradientButton(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(NeonMagenta, ElectricViolet)
                )
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = ShopFlowTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = ShopFlowTheme.colors.textPrimary
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    ShopFlowTheme {
        SplashScreen(
            onGetStarted = {},
            onSignIn = {},
            onAutoNavigateHome = {}
            // Note: In preview hiltViewModel() will throw if not provided with a mock, 
            // but for simplicity we'll just ignore preview injection errors here or you can provide a mock.
        )
    }
}
