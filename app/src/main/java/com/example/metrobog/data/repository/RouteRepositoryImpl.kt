package com.example.metrobog.data.repository

import com.example.metrobog.data.local.dao.RouteDao
import com.example.metrobog.data.local.entities.RouteEntity
import com.example.metrobog.data.model.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RouteRepositoryImpl(private val routeDao: RouteDao): RouteRepository {
    override fun getAllRoutes(): Flow<List<Route>> =
        routeDao.getAllRoutes().map { list -> list.map { it.toDomain() } }

    override fun searchRoutes(query: String): Flow<List<Route>> =
        routeDao.searchRoutes(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getRouteById(routeId: String): Route? =
        routeDao.getRouteById(routeId)?.toDomain()

    private fun RouteEntity.toDomain() = Route(
        routeId = routeId,
        shortName = routeShortName,
        longName = routeLongName,
        color = routeColor
    )

}