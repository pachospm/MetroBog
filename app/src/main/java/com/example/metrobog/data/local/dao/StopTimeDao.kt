package com.example.metrobog.data.local.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metrobog.data.local.entities.StopTimeEntity

data class StopConnection(
    @ColumnInfo(name = "fromStop") val fromStop: String,
    @ColumnInfo(name = "toStop") val toStop: String
)

@Dao
interface StopTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStopTime(stopTimes: List<StopTimeEntity>)

    @Query(
        """
            SELECT DISTINCT st1.stopId AS fromStop, st2.stopId AS toStop
            FROM stop_times st1
            JOIN stop_times st2
            ON st1.tripId = st2.tripId
            AND st2.stopSequence = st1.stopSequence + 1
        """
    )
    suspend fun getAllConnections(): List<StopConnection>

    @Query(
        """
            SELECT DISTINCT st2.stopId
            FROM stop_times st1
            JOIN stop_times st2 ON st1.tripId = st2.tripId
            WHERE st1.stopId = :stopId
            AND st2.stopSequence = st1.stopSequence + 1
        """
    )
    suspend fun getNextStops(stopId: String): List<String>

    @Query("SELECT COUNT(*) FROM stop_times")
    suspend fun getCount(): Int

    @Query("DELETE FROM stop_times")
    suspend fun deleteAll()

}