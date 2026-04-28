package com.shopflow.app.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.presentation.components.GradientButton
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.ShopFlowTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

    LaunchedEffect(hasCompletedOnboarding) {
        if (hasCompletedOnboarding) {
            onFinish()
        }
    }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val pages = listOf(
        Pair("Browse Latest Drops", "Discover the newest trends in streetwear and high fashion, updated daily."),
        Pair("Fast Checkout", "Experience our seamless checkout process with instant confirmation."),
        Pair("Track Everything", "Get real-time updates on your orders from warehouse to your doorstep.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Skip",
                color = TextSecondary,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        viewModel.completeOnboarding()
                    }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val imageUrl = when (page) {
                    0 -> "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&q=80" // Fashion / models
                    1 -> "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=600&q=80" // Checkout / Payment
                    else -> "https://images.unsplash.com/photo-1586880244406-556ebe35f282?w=600&q=80" // Delivery / Box
                }
                
                coil.compose.AsyncImage(
                    model = imageUrl,
                    contentDescription = "Onboarding Image $page",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = pages[page].first,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ShopFlowTheme.colors.textPrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pages[page].second,
                    color = TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        // Pager Indicator
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) NeonMagenta else TextSecondary.copy(alpha = 0.5f)
                val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(width = width, height = 8.dp)
                )
            }
        }

        GradientButton(
            text = if (pagerState.currentPage == pages.lastIndex) "Get Started →" else "Next",
            onClick = {
                if (pagerState.currentPage == pages.lastIndex) {
                    viewModel.completeOnboarding()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

