package com.shopflow.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.ShopFlowTheme
import com.shopflow.app.presentation.theme.SurfaceGlassElevated
import com.shopflow.app.presentation.theme.TextPrimary

@Composable
fun AvatarCircle(
    initials: String,
    size: Dp = 96.dp,
    modifier: Modifier = Modifier
) {
    val fontSize = if (size >= 96.dp) 28.sp else 14.sp

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(SurfaceGlassElevated)
            .border(
                width = 2.dp,
                color = NeonMagenta,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.uppercase(),
            color = TextPrimary,
            fontSize = fontSize,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF09090B)
@Composable
private fun AvatarCirclePreview() {
    ShopFlowTheme {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            AvatarCircle(initials = "JD", size = 96.dp)
            AvatarCircle(initials = "AB", size = 48.dp)
            AvatarCircle(initials = "XY", size = 64.dp)
        }
    }
}