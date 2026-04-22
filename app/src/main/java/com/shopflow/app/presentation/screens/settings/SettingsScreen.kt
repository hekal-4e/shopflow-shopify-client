package com.shopflow.app.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shopflow.app.presentation.components.ChipSelector
import com.shopflow.app.presentation.theme.NeonMagenta
import com.shopflow.app.presentation.theme.SurfaceGlass
import com.shopflow.app.presentation.theme.TextSecondary
import com.shopflow.app.presentation.theme.TrueBlack

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
            .verticalScroll(rememberScrollState())
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
                text = "Settings",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preferences Section
        SectionTitle("PREFERENCES")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(SurfaceGlass, RoundedCornerShape(16.dp))
        ) {
            ToggleRow("Push Notifications", preferences.pushNotifications) { viewModel.togglePushNotifications(it) }
            Divider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
            ToggleRow("Dark Theme", preferences.themeMode.equals("DARK", ignoreCase = true)) { viewModel.toggleDarkTheme(it) }
            Divider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
            ToggleRow("Biometric Login", preferences.biometricEnabled) { viewModel.toggleBiometrics(it) }
            Divider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
            ToggleRow("Email Marketing", preferences.emailMarketing) { viewModel.toggleEmailMarketing(it) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Region Section
        SectionTitle("REGION")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(SurfaceGlass, RoundedCornerShape(16.dp))
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Language",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ChipSelector(
                options = listOf("EN", "ES", "FR", "DE"),
                selectedOption = preferences.languageCode.uppercase(),
                onSelect = { viewModel.setLanguage(it.lowercase()) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Account Section
        SectionTitle("ACCOUNT & SECURITY")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(SurfaceGlass, RoundedCornerShape(16.dp))
        ) {
            NavigationRow("Change Password") {}
            Divider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
            NavigationRow("Privacy Settings") {}
            Divider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
            NavigationRow("Help Center") {}
            Divider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
            NavigationRow("Log Out", isDestructive = true) {}
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = TextSecondary,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
    )
}

@Composable
private fun ToggleRow(title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 16.sp)
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeonMagenta
            )
        )
    }
}

@Composable
private fun NavigationRow(title: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = if (isDestructive) Color.Red else Color.White, fontSize = 16.sp)
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
    }
}
