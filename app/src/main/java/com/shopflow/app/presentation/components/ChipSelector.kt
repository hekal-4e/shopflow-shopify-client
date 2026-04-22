package com.shopflow.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shopflow.app.presentation.theme.ShopFlowTheme

@Composable
fun ChipSelector(
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(options) { option ->
            val selected = option == selectedOption
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        brush = if (selected) {
                            ShopFlowTheme.gradient.brand
                        } else {
                            androidx.compose.ui.graphics.SolidColor(ShopFlowTheme.colors.surfaceGlassElevated)
                        },
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .clickable { onSelect(option) }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        ShopFlowTheme.colors.textSecondary
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF09090B)
@Composable
private fun ChipSelectorPreview() {
    ShopFlowTheme {
        ChipSelector(
            options = listOf("Popular", "Newest", "Price ↑", "Price ↓"),
            selectedOption = "Popular",
            onSelect = {}
        )
    }
}
