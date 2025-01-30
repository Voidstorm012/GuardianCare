package com.example.guardiancare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.guardiancare.ui.theme.GuardianCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply your Material 3 theme
            GuardianCareTheme {
                // Call a top-level composable that hosts your app’s UI logic
                GuardianCareApp()
            }
        }
    }
}
