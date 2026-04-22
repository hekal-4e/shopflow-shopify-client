package com.shopflow.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.ShopFlowTheme
import com.shopflow.app.presentation.theme.StatusDelivered
import com.shopflow.app.presentation.theme.StatusProcessing
import com.shopflow.app.presentation.theme.StatusShipped
import com.shopflow.app.presentation.theme.TextPrimary

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val statusColor = when (status.uppercase()) {
        "DELIVERED" -> StatusDelivered
        "PROCESSING" -> StatusProcessing
        "SHIPPED" -> StatusShipped
        "CANCELLED" -> Color(0xFFEF4444)
        else -> ShopFlowTheme.colors.textSecondary
    }

    Text(
        text = status.uppercase(),
        color = TextPrimary,
        fontSize = 10.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(statusColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF09090B)
@Composable
private fun StatusBadgePreview() {
    ShopFlowTheme {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            StatusBadge(status = "DELIVERED")
            StatusBadge(status = "PROCESSING")
            StatusBadge(status = "SHIPPED")
            StatusBadge(status = "CANCELLED")
            StatusBadge(status = "UNKNOWN")
        }
    }
}