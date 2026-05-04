package com.example.metrobog.data.repository

import com.example.metrobog.data.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    fun getAllRoutes() : Flow<List<Route>>
    fun searchRoutes(query:String): Flow<List<Route>>
    suspend fun getRouteById(routeId: String): Route?
}