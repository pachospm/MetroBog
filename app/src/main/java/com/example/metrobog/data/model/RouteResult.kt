package com.example.metrobog.data.model

data class RouteResult(
    val stops: List<Stop>,
    val totalStops : Int,
    val estimateMinutes: Int
)
