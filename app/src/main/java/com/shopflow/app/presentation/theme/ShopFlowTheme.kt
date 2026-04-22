package com.shopflow.app.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private val DarkColorScheme = darkColorScheme(
    primary = NeonMagenta,
    secondary = ElectricViolet,
    background = TrueBlack,
    surface = TrueBlack,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary
)

@Immutable
data class ShopFlowColorTokens(
    val textPrimary: Color,
    val textSecondary: Color,
    val surfaceGlass: Color,
    val surfaceGlassElevated: Color,
    val statusDelivered: Color,
    val statusProcessing: Color,
    val statusShipped: Color
)

@Immutable
data class ShopFlowGradientTokens(
    val brand: Brush
)

private val LocalShopFlowColors = staticCompositionLocalOf {
    ShopFlowColorTokens(
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        surfaceGlass = SurfaceGlass,
        surfaceGlassElevated = SurfaceGlassElevated,
        statusDelivered = StatusDelivered,
        statusProcessing = StatusProcessing,
        statusShipped = StatusShipped
    )
}

private val LocalShopFlowGradients = staticCompositionLocalOf {
    ShopFlowGradientTokens(
        brand = ShopFlowGradient
    )
}

object ShopFlowTheme {
    val colors: ShopFlowColorTokens
        @Composable get() = LocalShopFlowColors.current

    val gradient: ShopFlowGradientTokens
        @Composable get() = LocalShopFlowGradients.current
}

@Composable
fun ShopFlowTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val materialColors = if (darkTheme) DarkColorScheme else LightColorScheme
    val customColors = if (darkTheme) {
        ShopFlowColorTokens(
            textPrimary = TextPrimary,
            textSecondary = TextSecondary,
            surfaceGlass = SurfaceGlass,
            surfaceGlassElevated = SurfaceGlassElevated,
            statusDelivered = StatusDelivered,
            statusProcessing = StatusProcessing,
            statusShipped = StatusShipped
        )
    } else {
        ShopFlowColorTokens(
            textPrimary = LightTextPrimary,
            textSecondary = LightTextSecondary,
            surfaceGlass = LightSurfaceGlass,
            surfaceGlassElevated = LightSurfaceGlassElevated,
            statusDelivered = StatusDelivered,
            statusProcessing = StatusProcessing,
            statusShipped = StatusShipped
        )
    }

    CompositionLocalProvider(
        LocalShopFlowColors provides customColors,
        LocalShopFlowGradients provides ShopFlowGradientTokens(brand = ShopFlowGradient)
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = ShopFlowTypography,
            shapes = ShopFlowMaterialShapes,
            content = content
        )
    }
}

@Preview
@Composable
private fun ShopFlowThemePreview() {
    ShopFlowTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TrueBlack),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ShopFlow",
                color = NeonMagenta,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
