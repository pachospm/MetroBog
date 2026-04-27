package com.example.metrobog.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class StopEntity(
    @PrimaryKey val stopId: String,
    val stopCode: String,
    val stopName: String,
    val stopLat: Double,
    val stopLon: Double,
    val locationType: Int = 0,
    val parentStation: String? = null
)