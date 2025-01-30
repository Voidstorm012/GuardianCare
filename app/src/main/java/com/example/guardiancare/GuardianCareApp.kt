package com.example.guardiancare

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.guardiancare.navigation.NavDrawerItems
import com.example.guardiancare.navigation.NavGraph
import androidx.compose.material3.NavigationDrawerItemDefaults
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianCareApp() {
    val navController = rememberNavController()

    // Simple NavHost with two routes
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Route #1: Login Screen
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    // Hardcode navigation to drawer page
                    navController.navigate("drawerHome")
                }
            )
        }
        // Route #2: Drawer Page
        composable("drawerHome") {
            DrawerPage()
        }
    }

//    val navController = rememberNavController()
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    // Example: Hardcode user role. You can replace this with real logic from login, etc.
//    val userRole = "Elderly" // or "Member"
//
//    // Dynamically load drawer items based on role
//    val navItems = NavDrawerItems.getItemsForRole(userRole)
//    var selectedRoute by remember { mutableStateOf(navItems.firstOrNull()?.route ?: "") }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet {
//                Text(
//                    text = "Guardian Care Drawer",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(16.dp)
//                )
//
//                // Drawer items
//                navItems.forEach { item ->
//                    NavigationDrawerItem(
//                        icon = { Icon(item.icon, contentDescription = item.label) },
//                        label = { Text(item.label) },
//                        selected = (selectedRoute == item.route),
//                        onClick = {
//                            selectedRoute = item.route
//                            navController.navigate(item.route) {
//                                popUpTo(navController.graph.startDestinationId) { saveState = true }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                            scope.launch { drawerState.close() }
//                        },
//                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                    )
//                }
//            }
//        }
//    ) {
//        // Main screen scaffold
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Guardian Care") },
//                    navigationIcon = {
//                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
//                            Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
//                        }
//                    }
//                )
//            }
//        ) { paddingValues ->
//            NavGraph(
//                navController = navController,
//                modifier = Modifier.padding(paddingValues)
//            )
//        }
//    }
}
