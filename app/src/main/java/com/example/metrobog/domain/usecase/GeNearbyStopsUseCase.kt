package com.example.metrobog.domain.usecase

import androidx.compose.foundation.interaction.DragInteraction
import com.example.metrobog.data.model.Stop
import com.example.metrobog.data.repository.StopRepository

class GeNearbyStopsUseCase(private val stopRepository: StopRepository) {
    suspend operator fun invoke(lat: Double, lon:Double, radiuskm: Double = 0.5): List<Stop> =
        stopRepository.getNearbyStops(lat,lon, radiuskm)
}