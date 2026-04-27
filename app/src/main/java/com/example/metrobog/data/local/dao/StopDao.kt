package com.example.metrobog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metrobog.data.local.entities.StopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<StopEntity>)

    @Query("SELECT * FROM stops")
    fun getAllStops(): Flow<List<StopEntity>>

    @Query("SELECT * FROM stops WHERE stopId = :stopId")
    suspend fun getStopById(stopId: String): StopEntity?

    @Query("SELECT * FROM stops WHERE stopName LIKE '%' || :query || '%' OR stopCode LIKE '%' || :query || '%' ORDER BY stopName ASC")
    fun searchStops(query: String): Flow<List<StopEntity>>

    @Query(
        """
            SELECT * FROM stops
            WHERE ABS(stopLat - :lat) < :deltaLat
            AND ABS(stopLon - :lon) < :deltaLon
            ORDER BY ((stopLat - :lat) * (stopLat - :lat) + (stopLon - :lon) * (stopLon - :lon)) ASC
        """
    )
    suspend fun getNearbyStops(
        lat: Double,
        lon: Double,
        deltaLat:Double,
        deltaLon:Double
    ): List<StopEntity>

    @Query("SELECT COUNT(*) FROM stops")
    suspend fun getCount(): Int

    @Query("DELETE FROM stops")
    suspend fun deleteAll()
}