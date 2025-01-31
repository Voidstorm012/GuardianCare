package com.example.guardiancare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.guardiancare.ui.theme.GuardianCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            // Apply your Material 3 theme
            GuardianCareTheme {
                // Default to showing the LoginScreen
                // In a real app, you might have navigation to switch to RegisterScreen
                //LoginScreen(modifier = Modifier.padding())
                // Call a top-level composable that hosts your app’s UI logic

                GuardianCareApp()
            }
        }
    }
}
