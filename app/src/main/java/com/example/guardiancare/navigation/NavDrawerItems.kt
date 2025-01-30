// NavDrawerItems.kt
package com.example.guardiancare.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
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
        DrawerItem(Routes.Settings, "Settings", Icons.Filled.Settings),
        DrawerItem(Routes.FallDetection, "Fall Detection", Icons.Filled.Warning)
    )

    private val memberItems = listOf(
        DrawerItem(Routes.Home, "Home", Icons.Filled.Home),
        DrawerItem(Routes.Profile, "Profile", Icons.Filled.Person),
        DrawerItem(Routes.Settings, "Settings", Icons.Filled.Settings),
        DrawerItem(Routes.FallDetection, "Fall Detection", Icons.Filled.Warning)
    )

    fun getItemsForRole(role: String): List<DrawerItem> {
        return if (role == "Member") memberItems else elderlyItems
    }
}
