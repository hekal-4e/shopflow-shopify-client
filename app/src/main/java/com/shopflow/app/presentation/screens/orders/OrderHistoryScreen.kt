package com.shopflow.app.presentation.screens.orders

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.domain.model.Money
import com.shopflow.app.domain.model.Order
import com.shopflow.app.presentation.components.ChipSelector
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.StatusProcessing
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private fun Money.format(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(this.currencyCode)
    return format.format(this.amount)
}

@Composable
fun OrderHistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetails: (String) -> Unit,
    viewModel: OrderHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Order History",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        ChipSelector(
            options = listOf("All", "Active", "Delivered", "Cancelled"),
            selectedOption = when (uiState.filter) {
                OrderFilter.ALL -> "All"
                OrderFilter.ACTIVE -> "Active"
                OrderFilter.DELIVERED -> "Delivered"
                OrderFilter.CANCELLED -> "Cancelled"
            },
            onSelect = {
                val newFilter = when (it) {
                    "Active" -> OrderFilter.ACTIVE
                    "Delivered" -> OrderFilter.DELIVERED
                    "Cancelled" -> OrderFilter.CANCELLED
                    else -> OrderFilter.ALL
                }
                viewModel.setFilter(newFilter)
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonMagenta)
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error ?: "Error loading orders", color = Color.White)
            }
        } else if (uiState.orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No orders found.", color = TextSecondary)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.orders) { order ->
                    OrderCard(order = order, onClick = { onNavigateToOrderDetails(order.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    val dateStr = try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val date = format.parse(order.processedAt)
        SimpleDateFormat("MMM dd, yyyy", Locale.US).format(date ?: Date())
    } catch (e: Exception) {
        order.processedAt
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SurfaceGlass),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.orderNumber}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = dateStr,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (order.fulfillmentStatus.name) {
                        "DELIVERED" -> NeonMagenta
                        "CANCELLED" -> Color.Red
                        else -> StatusProcessing
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(statusColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.fulfillmentStatus.name,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = order.totalPrice.format(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
