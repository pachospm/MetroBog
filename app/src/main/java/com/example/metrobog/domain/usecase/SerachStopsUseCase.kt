package com.example.metrobog.domain.usecase

import com.example.metrobog.data.model.Stop
import com.example.metrobog.data.repository.StopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SerachStopsUseCase(private val stopRepository: StopRepository) {

    operator fun invoke(query:String): Flow<List<Stop>>{
        if(query.isBlank()) return stopRepository.getAllStops()
        return stopRepository.searchStops(query.trim()).map { stops ->
            stops.sortedBy { it.stopName }
        }
    }
}