package com.shopflow.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.TextPrimary
import com.shopflow.app.presentation.theme.TextSecondary

@Composable
fun NotificationBadge(
    unreadCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Notifications",
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )

        if (unreadCount > 0) {
            val displayCount = if (unreadCount > 9) "9+" else "$unreadCount"
            val badgeSize = if (unreadCount > 9) 18.dp else 16.dp

            Box(
                modifier = Modifier
                    .offset(x = 8.dp, y = (-6).dp)
                    .size(badgeSize)
                    .clip(CircleShape)
                    .background(NeonMagenta)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayCount,
                    color = TextPrimary,
                    fontSize = 8.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF09090B)
@Composable
private fun NotificationBadgePreview() {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        NotificationBadge(
            unreadCount = 0,
            onClick = {}
        )
        NotificationBadge(
            unreadCount = 5,
            onClick = {}
        )
        NotificationBadge(
            unreadCount = 99,
            onClick = {}
        )
    }
}