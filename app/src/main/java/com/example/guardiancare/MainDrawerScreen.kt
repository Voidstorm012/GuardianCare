package com.example.guardiancare

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guardiancare.navigation.DrawerItem
import com.example.guardiancare.navigation.Routes
import com.example.guardiancare.ui.screens.CaretakerScreen
import com.example.guardiancare.ui.screens.ChatbotScreen
import com.example.guardiancare.ui.screens.ElderlyScreen
import com.example.guardiancare.ui.screens.FallDetectionScreen
import com.example.guardiancare.ui.screens.HelpSreen
import com.example.guardiancare.ui.screens.HomeScreen
import com.example.guardiancare.ui.screens.ProfileScreen
import com.example.guardiancare.ui.screens.SettingsScreen
import com.example.guardiancare.utils.BleManager
import com.example.guardiancare.utils.NotificationHelper
import kotlinx.coroutines.launch

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
            DrawerItem(Routes.Home, "Home", Icons.Filled.Home),
            DrawerItem(Routes.Profile, "Profile", Icons.Filled.AccountCircle),
            DrawerItem(Routes.Elderly, "Eldery", Icons.Filled.Person),
            DrawerItem(Routes.FallDetection, "Fall Notifications", Icons.Filled.Warning),
            DrawerItem(Routes.Chatbot, "Chatbot AI", Icons.Filled.Star),
            DrawerItem(Routes.Help, "Help", Icons.Filled.Info),
            DrawerItem(Routes.Settings, "Settings", Icons.Filled.Settings),
        )
    } else {
        // Elderly
        listOf(
            DrawerItem(Routes.Home, "Home", Icons.Filled.Home),
            DrawerItem(Routes.Profile, "Profile", Icons.Filled.AccountCircle),
            DrawerItem(Routes.Caretaker, "Caretaker", Icons.Filled.Person),
            DrawerItem(Routes.Chatbot, "Chatbot AI", Icons.Filled.Star),
            DrawerItem(Routes.Help, "Help", Icons.Filled.Info),
            DrawerItem(Routes.Settings, "Settings", Icons.Filled.Settings),
            DrawerItem(Routes.FallDetection, "Fall Detection Test", Icons.Filled.Warning),
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
            NavHost(
                navController = childNavController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen() }
                composable("profile") { ProfileScreen() }
                composable("caretaker") { CaretakerScreen() }
                composable("elderly") { ElderlyScreen() }
                composable("chatbot") { ChatbotScreen() }
                composable("help") { HelpSreen() }
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
