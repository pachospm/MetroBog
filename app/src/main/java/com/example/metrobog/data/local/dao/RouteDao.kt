package com.example.metrobog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metrobog.data.local.entities.RouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<RouteEntity>)

    @Query("SELECT * FROM routes ORDER BY routeShortName")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routes WHERE routeId = :routeId")
    suspend fun getRouteById(routeId: String): RouteEntity?

    @Query(
        """
            SELECT * FROM routes
            WHERE routeShortName LIKE '%' || :query || '%'
            OR routeLongName LIKE '%' || :query || '%'
            ORDER BY routeShortName ASC
        """
    )
    fun searchRoutes(query: String): Flow<List<RouteEntity>>

    @Query("SELECT COUNT(*) FROM routes")
    suspend fun getCount(): Int

    @Query("DELETE FROM routes")
    suspend fun deleteAll()
}