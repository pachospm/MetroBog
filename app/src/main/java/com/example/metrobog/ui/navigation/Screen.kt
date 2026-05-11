package com.example.metrobog.ui.navigation

sealed class Screen(val route: String){
    data object Map: Screen("map")
    data object Search : Screen("search")
    data object Route: Screen("route/{originId}/{destinationId}"){
        fun createRoute(originId: String, destinationId: String) =
            "route/$originId/$destinationId"
    }
}