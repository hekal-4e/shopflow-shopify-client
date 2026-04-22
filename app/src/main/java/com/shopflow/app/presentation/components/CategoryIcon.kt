package com.shopflow.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shopflow.app.presentation.theme.ShopFlowTheme

@Composable
fun CategoryIcon(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    brush = if (isActive) {
                        ShopFlowTheme.gradient.brand
                    } else {
                        androidx.compose.ui.graphics.SolidColor(ShopFlowTheme.colors.surfaceGlass)
                    },
                    shape = CircleShape
                )
                .border(
                    width = if (isActive) 2.dp else 1.dp,
                    color = if (isActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        ShopFlowTheme.colors.textSecondary.copy(alpha = 0.5f)
                    },
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    ShopFlowTheme.colors.textSecondary
                }
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF09090B)
@Composable
private fun CategoryIconPreview() {
    ShopFlowTheme {
        RowPreview()
    }
}

@Composable
private fun RowPreview() {
    androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryIcon(
            icon = Icons.Filled.ShoppingBag,
            label = "Bags",
            isActive = true,
            onClick = {}
        )
        CategoryIcon(
            icon = Icons.Filled.ShoppingBag,
            label = "Shoes",
            isActive = false,
            onClick = {}
        )
    }
}
