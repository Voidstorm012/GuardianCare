package com.example.guardiancare

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guardiancare.navigation.DrawerItem
import com.example.guardiancare.navigation.Routes
import com.example.guardiancare.ui.screens.FallDetectionScreen
import kotlinx.coroutines.launch
import com.example.guardiancare.ui.screens.HomeScreen
import com.example.guardiancare.ui.screens.ProfileScreen
import com.example.guardiancare.ui.screens.SettingsScreen
import com.example.guardiancare.utils.BleManager
import com.example.guardiancare.utils.NotificationHelper

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDrawerScreen() {
    // The drawer's state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // A child navController for the screens inside the drawer
    val childNavController = rememberNavController()

    // **Set up BleManager & NotificationHelper** here or in a parent
    val context = LocalContext.current
    val bleManager = remember { BleManager.getInstance(context) }
    val notificationHelper = remember { NotificationHelper.getInstance(context) }

    // Suppose we define some role-based items.
    // Hardcode “Elderly” or “Member,” or fetch from user session.
    val userRole = "Elderly"

    val navItems = if (userRole == "Member") {
        listOf(
            DrawerItem("home", "Home", Icons.Default.Home),
            DrawerItem("profile", "Profile", Icons.Default.Person),
            DrawerItem("settings", "Settings", Icons.Default.Settings),
            DrawerItem("fall_detection", "Fall Detection", Icons.Default.Warning),
            // Add more for members
        )
    } else {
        // Elderly
        listOf(
            DrawerItem("home", "Home", Icons.Default.Home),
            DrawerItem("profile", "Profile", Icons.Default.Person),
            DrawerItem("settings", "Settings", Icons.Default.Settings),
            DrawerItem("fall_detection", "Fall Detection", Icons.Default.Warning),
            // Add more for elderly
        )
    }

    // Track which item is “selected”
    var selectedRoute by remember { mutableStateOf("home") }

    // Navigation drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Guardian Care Drawer",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                navItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        selected = (selectedRoute == item.route),
                        onClick = {
                            selectedRoute = item.route
                            // Navigate in the child nav controller
                            childNavController.navigate(item.route)
                            // Close the drawer
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        // Main page content
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Guardian Care") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // The CHILD NavHost: “home”, “profile”, “settings”
            NavHost(
                navController = childNavController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen() }
                composable("profile") { ProfileScreen() }
                composable("settings") { SettingsScreen() }
                composable(Routes.FallDetection) {
                    FallDetectionScreen(
                        bleManager = bleManager,
                        notificationHelper = notificationHelper
                    )
                }
            }
        }
    }
}
