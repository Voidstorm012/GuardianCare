package com.example.guardiancare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.guardiancare.RegisterScreen
import com.example.guardiancare.ui.screens.HomeScreen
import com.example.guardiancare.ui.screens.ProfileScreen
import com.example.guardiancare.ui.screens.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        modifier = modifier
    ) {
        composable(Routes.Home) {
            HomeScreen()
        }
        composable(Routes.Profile) {
            ProfileScreen()
        }
        composable(Routes.Settings) {
            SettingsScreen()
        }
        // Add more composable(...) calls for additional routes/screens

        composable(Routes.Registration) {
            RegisterScreen()
        }
    }
}
