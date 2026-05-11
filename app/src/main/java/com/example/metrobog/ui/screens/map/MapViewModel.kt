package com.example.metrobog.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.metrobog.data.model.Stop
import com.example.metrobog.data.repository.StopRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.math.cos

data class MapUiState(
    val nearbyStops: List<Stop> = emptyList(),
    val selectedStop : Stop? = null,
    val errorMessage : String? = null,
    val cameraLat: Double = 4.711,
    val cameraLon: Double = -74.0721,
    val cameraZoom: Float = 13f
)

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModel(
    private val stopRepository: StopRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState

    private val cameraPosition = MutableStateFlow(
        Pair(_uiState.value.cameraLat, _uiState.value.cameraLon)
    )

    init {
        cameraPosition
            .flatMapLatest { (lat, lon) ->
                stopRepository.getAllStops().map { allStops ->
                    filterNearby(allStops, lat,lon, radiusKm = 1.5)
                }
            }
            .onEach { nearbyStops ->
                _uiState.update { it.copy(nearbyStops = nearbyStops) }
            }
            .launchIn(viewModelScope)
    }

    fun moveCamera(lat: Double, lon:Double, zoom:Float){
        _uiState.update { it.copy(cameraLat = lat, cameraLon = lon, cameraZoom = zoom) }
        cameraPosition.value = Pair(lat, lon)
    }

    fun selectStop( stop: Stop){
        _uiState.update { it.copy( selectedStop = stop)}
    }

    fun clearSelectedStop(){
        _uiState.update { it.copy(selectedStop = null) }
    }

    private fun filterNearby(
        stops: List<Stop>,
        lat: Double,
        lon: Double,
        radiusKm: Double
    ): List<Stop>{
        val deltaLat = radiusKm / 111.0
        val deltaLon = radiusKm / (111.0 * cos(Math.toRadians(lat)))
        return stops.filter { stop ->
            kotlin.math.abs(stop.lat - lat) < deltaLat &&
                    kotlin.math.abs(stop.lon - lon) < deltaLon
        }
    }

    class Factory(private val stopRepository: StopRepository) :  ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MapViewModel(stopRepository) as T
    }

}