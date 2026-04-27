package com.example.metrobog.data.local.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "stop_times",
    primaryKeys = ["tripId", "stopSequence"],
    indices = [Index("stopId"), Index("tripId")]
)
data class StopTimeEntity(
    val tripId: String,
    val arrivalTime: String,
    val departureTime: String,
    val stopId: String,
    val stopSequence: Int
)
