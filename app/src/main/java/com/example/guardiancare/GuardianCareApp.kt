package com.example.guardiancare

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guardiancare.navigation.Routes

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianCareApp() {
    val navController = rememberNavController()

    // Simple NavHost with two routes
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login Screen → Navigate to Main Drawer
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("drawerHome") // ✅ Loads `MainDrawerScreen.kt`
                },
//                onRegisterClick = {
//                    navController.navigate(Routes.Registration)
//                }
            )
        }

        // Main Drawer Screen (App Main UI)
        composable("drawerHome") {
            MainDrawerScreen() // ✅ This is how GuardianCareApp loads MainDrawerScreen
        }
    }

}
