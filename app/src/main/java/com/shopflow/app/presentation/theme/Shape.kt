package com.shopflow.app.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class ShopFlowShapeTokens(
    val cardShape: Shape,
    val heroShape: Shape,
    val pillShape: Shape,
    val chipShape: Shape,
    val inputShape: Shape
)

val ShopFlowShapes = ShopFlowShapeTokens(
    cardShape = RoundedCornerShape(16.dp),
    heroShape = RoundedCornerShape(20.dp),
    pillShape = RoundedCornerShape(28.dp),
    chipShape = RoundedCornerShape(12.dp),
    inputShape = RoundedCornerShape(14.dp)
)

val ShopFlowMaterialShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
