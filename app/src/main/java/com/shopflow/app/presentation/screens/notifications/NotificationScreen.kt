package com.shopflow.app.presentation.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.domain.model.Notification
import com.shopflow.app.presentation.components.GlassmorphismCard
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import com.shopflow.app.presentation.theme.ShopFlowTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDeepLink: (String?) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ShopFlowTheme.colors.textPrimary)
            }
            Text(
                text = "Notifications",
                color = ShopFlowTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications", color = TextSecondary)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = {
                            viewModel.markAsRead(notification.id)
                            val deepLink = when (notification.type) {
                                com.shopflow.app.domain.model.NotificationType.ORDER_UPDATE -> "shopflow://order/${notification.referenceId}"
                                else -> null
                            }
                            onNavigateToDeepLink(deepLink)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCard(notification: Notification, onClick: () -> Unit) {
    val format = SimpleDateFormat("MMM dd, HH:mm", Locale.US)
    val dateStr = format.format(Date(notification.createdAt))

    GlassmorphismCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (notification.isRead) Color.Transparent else NeonMagenta)
                    .align(Alignment.CenterVertically)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    color = ShopFlowTheme.colors.textPrimary,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.body,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dateStr,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

