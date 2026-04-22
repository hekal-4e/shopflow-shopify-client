package com.shopflow.app.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val TrueBlack = Color(0xFF09090B)
val NeonMagenta = Color(0xFFFF2D78)
val ElectricViolet = Color(0xFF7B2FFF)
val TextPrimary = Color(0xFFF4F4F5)
val TextSecondary = Color(0xFFA1A1AA)
val SurfaceGlass = Color(0x0AFFFFFF)
val SurfaceGlassElevated = Color(0x14FFFFFF)
val StatusDelivered = Color(0xFF22C55E)
val StatusProcessing = Color(0xFFF59E0B)
val StatusShipped = Color(0xFF06B6D4)

val LightBackground = Color(0xFFF8FAFC)
val LightPrimary = Color(0xFF6A1FBF)
val LightSecondary = Color(0xFFEC4899)
val LightTextPrimary = Color(0xFF18181B)
val LightTextSecondary = Color(0xFF52525B)
val LightSurfaceGlass = Color(0x14FFFFFF)
val LightSurfaceGlassElevated = Color(0x26FFFFFF)

val ShopFlowGradient: Brush = Brush.horizontalGradient(
    colors = listOf(NeonMagenta, ElectricViolet)
)
