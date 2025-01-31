package com.example.guardiancare

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.guardiancare.ui.theme.GuardianCareTheme
import com.example.guardiancare.utils.BleManager
import com.example.guardiancare.utils.NotificationHelper

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    private lateinit var bleManager: BleManager
    private lateinit var notificationHelper: NotificationHelper

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bleManager = BleManager.getInstance(this)
        notificationHelper = NotificationHelper.getInstance(this)

        // Request permissions immediately when the app starts
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                bleManager.startScanning(this)
            } else {
                Toast.makeText(this, "Permissions Denied. BLE Features Won't Work.", Toast.LENGTH_LONG).show()
            }
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.POST_NOTIFICATIONS
            )
        )

        // Always listen for fall detection messages
        bleManager.setDataCallback { message ->
            if (message.contains("Fall detected", ignoreCase = true)) {
                notificationHelper.showFallDetectionNotification()
            }
        }

        setContent {
            GuardianCareTheme {
                GuardianCareApp()
            }
        }
    }
}
