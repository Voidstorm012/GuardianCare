package com.example.guardiancare

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.guardiancare.ui.theme.GuardianCareTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
