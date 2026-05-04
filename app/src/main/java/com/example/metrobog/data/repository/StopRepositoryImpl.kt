package com.example.metrobog.data.repository

import com.example.metrobog.data.local.dao.StopDao
import com.example.metrobog.data.local.entities.StopEntity
import com.example.metrobog.data.model.Stop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.cos

class StopRepositoryImpl(private val stopDao: StopDao) : StopRepository{
    override fun getAllStops(): Flow<List<Stop>> =
        stopDao.getAllStops().map { list -> list.map { it.toDomain() }}


    override fun searchStops(query: String): Flow<List<Stop>> =
        stopDao.searchStops(query).map { list -> list.map { it.toDomain() } }


    override suspend fun getStopById(stopId: String): Stop? =
        stopDao.getStopById(stopId)?.toDomain()


    override suspend fun getNearbyStops(
        lat: Double,
        lon: Double,
        radiuskm: Double
    ): List<Stop> {
        val deltaLat = radiuskm / 111.0
        val deltaLon = radiuskm / (111.0 * cos(Math.toRadians(lat)))
        return stopDao.getNearbyStops(lat,lon, deltaLat, deltaLon).map { it.toDomain() }
    }

    override suspend fun getStopCount(): Int = stopDao.getCount()

    private fun StopEntity.toDomain() = Stop(
        stopId = stopId,
        stopCode = stopCode,
        stopName = stopName,
        lat = stopLat,
        lon = stopLon
    )

}