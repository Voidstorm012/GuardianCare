package com.example.guardiancare.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

object NavDrawerItems {

    private val elderlyItems = listOf(
        DrawerItem(Routes.Home, "Home", Icons.Filled.Home),
        DrawerItem(Routes.Profile, "Profile", Icons.Filled.Person),
        DrawerItem(Routes.Settings, "Settings", Icons.Filled.Settings)
        // Could add Chatbot, Caretaker, etc.
    )

    private val memberItems = listOf(
        DrawerItem(Routes.Home, "Home", Icons.Filled.Home),
        DrawerItem(Routes.Profile, "Profile", Icons.Filled.Person),
        DrawerItem(Routes.Settings, "Settings", Icons.Filled.Settings)
        // Additional items like ElderlyList, FallNotifications, etc.
    )

    fun getItemsForRole(role: String): List<DrawerItem> {
        return if (role == "Member") memberItems else elderlyItems
    }
}
