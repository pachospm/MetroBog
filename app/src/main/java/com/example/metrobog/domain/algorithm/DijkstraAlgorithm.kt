package com.example.metrobog.domain.algorithm

import androidx.compose.ui.Modifier
import java.util.PriorityQueue

object DijkstraAlgorithm {

    data class Edge(val to: String, val weight: Double)

    data class PathResult(
        val path: List<String>,
        val totalCost: Double,
        val found: Boolean
    )

    fun shortesPath(
        graph: Map<String, List<Edge>>,
        start: String,
        end: String
    ): PathResult {
        if(start == end) return PathResult(listOf(start), 0.0, true)

        val distances = mutableMapOf<String, Double>().withDefault { Double.MAX_VALUE }
        val previous = mutableMapOf<String, String>()
        val visited = mutableSetOf<String>()

        distances[start] = 0.0
        val queue = PriorityQueue<Pair<Double, String>>(compareBy { it.first })
        queue.add(0.0 to start)

        while (queue.isNotEmpty()){
            val (cost, node) = queue.poll()

            if(node in visited) continue
            visited.add(node)

            if (node == end) break

            val neighbors = graph[node] ?: emptyList()
            for(edge in neighbors) {
                val newCost = cost + edge.weight
                if (newCost < distances.getValue(edge.to)){
                    distances[edge.to] = newCost
                    previous[edge.to] = node
                    queue.add(newCost to edge.to)
                }
            }
        }

        if (distances.getValue(end) == Double.MAX_VALUE){
            return PathResult(emptyList(), Double.MAX_VALUE, false)
        }

        val path = mutableListOf<String>()
        var current: String? = end
        while (current != null){
            path.add(0, current)
            current = previous[current]
        }

        return PathResult(path, distances.getValue(end), true)
    }

    fun buildGraphFromConnections(connections: Map<String, List<String>>): Map<String, List<Edge>>{
        return connections.mapValues { (_, neighbors) ->
            neighbors.map { Edge(to = it, weight = 1.0) }
        }
    }
}