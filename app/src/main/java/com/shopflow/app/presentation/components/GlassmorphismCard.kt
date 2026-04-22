package com.shopflow.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shopflow.app.presentation.theme.ShopFlowTheme

@Composable
fun GlassmorphismCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    glowBorder: Boolean = false,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val borderModifier = if (glowBorder) {
        Modifier.border(
            border = BorderStroke(1.dp, ShopFlowTheme.gradient.brand),
            shape = shape
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(borderModifier)
            .clip(shape)
            .background(
                color = if (glowBorder) {
                    ShopFlowTheme.colors.surfaceGlassElevated
                } else {
                    ShopFlowTheme.colors.surfaceGlass
                }
            )
            .padding(12.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF09090B)
@Composable
private fun GlassmorphismCardPreview() {
    ShopFlowTheme {
        GlassmorphismCard(glowBorder = true) {
            Text(
                text = "Glass card",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
