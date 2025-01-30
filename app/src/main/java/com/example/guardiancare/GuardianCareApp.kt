//package com.example.guardiancare
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.guardiancare.navigation.NavDrawerItems
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun GuardianCareApp() {
//    val navController = rememberNavController()
//
//    // Single NavHost: "login" → "drawerHome"
//    NavHost(
//        navController = navController,
//        startDestination = "login"
//    ) {
//        // 1) Login route
//        composable("login") {
//            // We'll pass a callback so the "Login" button triggers
//            // navController.navigate("drawerHome")
//            LoginScreen(
//                onLoginClick = {
//                    navController.navigate("drawerHome")
//                }
//            )
//        }
//
//        // 2) Drawer route (role-based)
//        composable("drawerHome") {
//            // This is where we inline your original commented-out code:
//            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//            val scope = rememberCoroutineScope()
//
//            // Hardcode userRole or retrieve from saved data
//            val userRole = "Elderly" // or "Member"
//
//            // Dynamically load the NavDrawer items
//            val navItems = NavDrawerItems.getItemsForRole(userRole)
//            var selectedRoute by remember { mutableStateOf(navItems.firstOrNull()?.route ?: "") }
//
//            ModalNavigationDrawer(
//                drawerState = drawerState,
//                drawerContent = {
//                    ModalDrawerSheet {
//                        Text(
//                            text = "Guardian Care Drawer",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(16.dp)
//                        )
//
//                        // Build each drawer item
//                        navItems.forEach { item ->
//                            NavigationDrawerItem(
//                                icon = { Icon(item.icon, contentDescription = item.label) },
//                                label = { Text(item.label) },
//                                selected = (selectedRoute == item.route),
//                                onClick = {
//                                    selectedRoute = item.route
//                                    // We'll not set a sub-nav for these items,
//                                    // but you could do something custom here (e.g., show a screen).
//                                    // Just remove or comment out any second navHost calls.
//
//                                    scope.launch { drawerState.close() }
//                                },
//                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                            )
//                        }
//                    }
//                }
//            ) {
//                // Your main screen (Scaffold + TopAppBar)
//                Scaffold(
//                    topBar = {
//                        TopAppBar(
//                            title = { Text("Guardian Care") },
//                            navigationIcon = {
//                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
//                                    Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
//                                }
//                            }
//                        )
//                    }
//                ) { paddingValues ->
//                    // If you want a single page or content area
//                    // We'll just show a placeholder:
//                    Text(
//                        text = "Welcome to the Drawer!",
//                        modifier = Modifier.padding(paddingValues).padding(16.dp)
//                    )
//                }
//            }
//        }
//    }
//}

package com.example.guardiancare

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuardianCareApp() {
    val navController = rememberNavController()

    // High-level routes: "login" and "main"
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 1) Show login
        composable("login") {
            // Pass a callback so the button navigates to "main"
            LoginScreen(
                onLoginClick = { navController.navigate("main") }
            )
        }

        // 2) Show the main drawer screen
        composable("main") {
            MainDrawerScreen()
        }
    }
}
