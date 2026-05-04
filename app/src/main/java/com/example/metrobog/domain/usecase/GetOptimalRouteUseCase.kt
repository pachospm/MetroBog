package com.example.metrobog.domain.usecase

import com.example.metrobog.data.local.dao.StopTimeDao
import com.example.metrobog.data.model.RouteResult
import com.example.metrobog.data.repository.StopRepository
import com.example.metrobog.domain.algorithm.DijkstraAlgorithm

class GetOptimalRouteUseCase(
    private val stopRepository: StopRepository,
    private val stopTimeDao: StopTimeDao
) {

    suspend operator fun invoke(originId: String, destinationId: String): RouteResult? {
        val graph = buildGraph()
        val result = DijkstraAlgorithm.shortesPath(graph, originId, destinationId)

        if (!result.found) return null

        val stops = result.path.mapNotNull { stopRepository.getStopById(it) }
        return RouteResult(
            stops = stops,
            totalStops = stops.size,
            estimateMinutes = (result.totalCost * 2).toInt().coerceAtLeast(1)
        )
    }

    private suspend fun buildGraph(): Map<String, List<DijkstraAlgorithm.Edge>>{
        val connection = stopTimeDao.getAllConnections()
        val edges = mutableMapOf<String, MutableList<DijkstraAlgorithm.Edge>>()

        for (conn in connection){
            edges.getOrPut(conn.fromStop) {mutableListOf()}
                .add(DijkstraAlgorithm.Edge(to = conn.toStop, weight = 1.0))
        }
        return edges
    }

}