package com.example.guardiancare.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.guardiancare.utils.BleManager
import com.example.guardiancare.utils.NotificationHelper

@Composable
fun FallDetectionScreen(
    bleManager: BleManager,
    notificationHelper: NotificationHelper
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var status by remember { mutableStateOf("Scanning for device...") }

    // When this screen appears, set BLE callbacks to update Compose state
    LaunchedEffect(Unit) {
        if (activity != null) {
            // Start scanning with an Activity reference
            bleManager.startScanning(activity)
        }

        // BLE connection callback
        bleManager.setConnectionCallback { isConnected ->
            status = if (isConnected) "Connected to M5StickC-Plus"
            else "Disconnected from M5StickC-Plus"
        }

        // BLE data callback
        bleManager.setDataCallback { message ->
            status = message
        }

        // Also can check notifications:
        // notificationHelper.checkNotificationPermissions(...) if you want
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = status)
    }
}
