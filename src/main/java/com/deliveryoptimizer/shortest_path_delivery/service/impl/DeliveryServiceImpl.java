package com.deliveryoptimizer.shortest_path_delivery.service.impl;

import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryRequest;
import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryResponse;
import com.deliveryoptimizer.shortest_path_delivery.model.Location;
import com.deliveryoptimizer.shortest_path_delivery.service.DeliveryService;
import com.deliveryoptimizer.shortest_path_delivery.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DistanceCalculator distanceCalculator;

    @Autowired
    public DeliveryServiceImpl(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public DeliveryResponse calculateShortestPath(DeliveryRequest request) {
        // Validate input
        validateRequest(request);

        // Create graph representation
        Map<Location, Map<Location, Double>> graph = createGraph(request);

        // Run Dijkstra's algorithm
        Map<Location, Double> shortestDistances = dijkstra(graph, request.getStartLocation());

        // Find shortest path
        List<Location> shortestPath = findShortestPath(request, shortestDistances);

        // Calculate total time
        double totalTime = calculateTotalTime(request, shortestPath);

        return new DeliveryResponse(shortestPath, totalTime);
    }

    private void validateRequest(DeliveryRequest request) {
        // Null checks
        Objects.requireNonNull(request, "DeliveryRequest cannot be null");
        Objects.requireNonNull(request.getStartLocation(), "Start location cannot be null");
        Objects.requireNonNull(request.getConsumer1(), "Consumer 1 cannot be null");
        Objects.requireNonNull(request.getConsumer2(), "Consumer 2 cannot be null");
        Objects.requireNonNull(request.getRestaurant1(), "Restaurant 1 cannot be null");
        Objects.requireNonNull(request.getRestaurant2(), "Restaurant 2 cannot be null");

        // Edge case: Start location same as consumer or restaurant
        if (request.getStartLocation().equals(request.getConsumer1()) ||
                request.getStartLocation().equals(request.getConsumer2()) ||
                request.getStartLocation().equals(request.getRestaurant1()) ||
                request.getStartLocation().equals(request.getRestaurant2())) {
            throw new IllegalArgumentException("Start location cannot be the same as consumer or restaurant locations");
        }
    }

    private Map<Location, Map<Location, Double>> createGraph(DeliveryRequest request) {
        Map<Location, Map<Location, Double>> graph = new HashMap<>();

        // Add vertices
        graph.put(request.getStartLocation(), new HashMap<>());
        graph.put(request.getConsumer1(), new HashMap<>());
        graph.put(request.getConsumer2(), new HashMap<>());
        graph.put(request.getRestaurant1(), new HashMap<>());
        graph.put(request.getRestaurant2(), new HashMap<>());

        // Add edges
        addEdge(graph, request.getStartLocation(), request.getRestaurant1());
        addEdge(graph, request.getStartLocation(), request.getRestaurant2());
        addEdge(graph, request.getRestaurant1(), request.getConsumer1());
        addEdge(graph, request.getRestaurant2(), request.getConsumer2());

        return graph;
    }

    private void addEdge(Map<Location, Map<Location, Double>> graph, Location source, Location destination) {
        double distance = distanceCalculator.calculateDistance(source, destination);
        graph.get(source).put(destination, distance);
        graph.get(destination).put(source, distance);
    }

    private Map<Location, Double> dijkstra(Map<Location, Map<Location, Double>> graph, Location startLocation) {
        Map<Location, Double> distances = new HashMap<>();
        for (Location location : graph.keySet()) {
            distances.put(location, Double.MAX_VALUE);
        }
        distances.put(startLocation, 0.0);

        PriorityQueue<Location> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        pq.offer(startLocation);

        while (!pq.isEmpty()) {
            Location current = pq.poll();
            for (Map.Entry<Location, Double> neighbor : graph.get(current).entrySet()) {
                Location neighborLocation = neighbor.getKey();
                double distance = neighbor.getValue();
                double newDistance = distances.get(current) + distance;
                if (newDistance < distances.get(neighborLocation)) {
                    distances.put(neighborLocation, newDistance);
                    pq.offer(neighborLocation);
                }
            }
        }

        return distances;
    }


    private List<Location> findShortestPath(DeliveryRequest request, Map<Location, Double> shortestDistances) {
        List<Location> shortestPath = new ArrayList<>();
        shortestPath.add(request.getStartLocation());

        Location restaurant1 = request.getRestaurant1();
        Location restaurant2 = request.getRestaurant2();
        Location consumer1 = request.getConsumer1();
        Location consumer2 = request.getConsumer2();

        if (shortestDistances.get(restaurant1) + request.getPreparationTime1() + shortestDistances.get(consumer1) <
                shortestDistances.get(restaurant2) + request.getPreparationTime2() + shortestDistances.get(consumer2)) {
            shortestPath.add(restaurant1);
            shortestPath.add(consumer1);
            shortestPath.add(restaurant2);
            shortestPath.add(consumer2);
        } else {
            shortestPath.add(restaurant2);
            shortestPath.add(consumer2);
            shortestPath.add(restaurant1);
            shortestPath.add(consumer1);
        }

        return shortestPath;
    }

    private double calculateTotalTime(DeliveryRequest request, List<Location> shortestPath) {
        double totalTime = 0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Location source = shortestPath.get(i);
            Location destination = shortestPath.get(i + 1);
            double distance = distanceCalculator.calculateDistance(source, destination);
            double time = distance / 20.0; // Assuming average speed of 20 km/hr
            totalTime += time;

            if (destination.equals(request.getRestaurant1())) {
                totalTime += request.getPreparationTime1();
            } else if (destination.equals(request.getRestaurant2())) {
                totalTime += request.getPreparationTime2();
            }
        }
        return totalTime;
    }
}



