package com.example.metrobog.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.selects.select

private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val bottomItems = listOf(
        BottomNavItem(Screen.Map, "Mapa"){
            Icon(Icons.Default.LocationOn, contentDescription = "Mapa")
        },
        BottomNavItem(Screen.Search, "Buscar ruta"){
            Icon(Icons.Default.DirectionsBus, contentDescription = "Buscar ruta")
        }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any{ dest ->
                        dest.route == item.screen.route  ||
                                (item.screen == Screen.Search && dest.route?.startsWith("route/") == true)
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.screen.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = item.icon,
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPaddin ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.route,
            modifier = Modifier.padding(innerPaddin)
        ){
            composable(Screen.Map.route){
                MapScreen(onStopSelect = {
                    navController.navigate(Screen.Search.route){
                        launchSingleTop = true
                    }
                })
            }

            composable(Screen.Map.route){
                SearchScreen(onNavigateToRoute = { originId, destId ->
                    navController.navigate(Screen.Route.createRoute(originId, destId))

                })
            }

            composable(Screen.Map.route){ backStackEntry ->
                val originId = backStackEntry.arguments?.getString("originId") ?: ""
                val destId = backStackEntry.arguments?.getString("destinationId") ?: ""
                RouteScreen(
                    originId = originId,
                    destinationId = destId,
                    onBack = {navController.popBackStack()}
                )
            }
        }
    }
}