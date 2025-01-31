package com.example.guardiancare.ui.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FallDetectionScreen() {
    val context = LocalContext.current
    val bleManager = remember { BleManager.getInstance(context) }
    val notificationHelper = remember { NotificationHelper.getInstance(context) }

    var statusMessage by remember { mutableStateOf("Checking Bluetooth Connection...") }

    // Check current connection state when the screen is opened
    LaunchedEffect(Unit) {
        if (bleManager.isConnected()) {
            statusMessage = "Connected to M5StickC-Plus"
        } else {
            statusMessage = "Scanning for Bluetooth Device..."
            bleManager.startScanning(context as Activity) // Start scanning if not connected
        }

        // Listen for connection changes
        bleManager.setConnectionCallback { isConnected ->
            statusMessage = if (isConnected) {
                "Connected to M5StickC-Plus"
            } else {
                "Disconnected from M5StickC-Plus"
            }
        }

        // Listen for fall detection messages
        bleManager.setDataCallback { message ->
            statusMessage = message
            if (message.contains("Fall detected", ignoreCase = true)) {
                notificationHelper.showFallDetectionNotification()
            }
        }
    }

    // UI Layout
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = statusMessage)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            bleManager.startScanning(context as Activity)
        }) {
            Text("Restart Bluetooth Scan")
        }
    }
}
