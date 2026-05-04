package com.example.metrobog.data.repository

import com.example.metrobog.data.model.Stop
import kotlinx.coroutines.flow.Flow

interface StopRepository {
    fun getAllStops(): Flow<List<Stop>>
    fun searchStops(query: String): Flow<List<Stop>>
    suspend fun getStopById(stopId: String): Stop?
    suspend fun getNearbyStops(lat:Double, lon:Double, radiuskm: Double): List<Stop>
    suspend fun getStopCount(): Int
}