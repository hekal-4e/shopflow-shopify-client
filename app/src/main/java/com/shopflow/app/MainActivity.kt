package com.shopflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.shopflow.app.presentation.navigation.Route
import com.shopflow.app.presentation.navigation.ShopFlowNavGraph
import com.shopflow.app.presentation.theme.ShopFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopFlowTheme {
                ShopFlowNavGraph(startDestination = Route.Splash.route)
            }
        }
    }
}

@Composable
private fun NavGraphPlaceholder() {
    Text(text = "ShopFlow nav graph placeholder")
}

@Preview(showBackground = true)
@Composable
private fun MainActivityPreview() {
    ShopFlowTheme {
        NavGraphPlaceholder()
    }
}
