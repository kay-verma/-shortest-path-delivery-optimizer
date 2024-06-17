package com.deliveryoptimizer.shortest_path_delivery.service.impl;

import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryRequest;
import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryResponse;
import com.deliveryoptimizer.shortest_path_delivery.model.Location;
import com.deliveryoptimizer.shortest_path_delivery.service.DeliveryService;
import com.deliveryoptimizer.shortest_path_delivery.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private final DistanceCalculator distanceCalculator;

    @Autowired
    public DeliveryServiceImpl(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public DeliveryResponse calculateShortestPath(DeliveryRequest request) {
        try {
            validateRequest(request);

            Map<Location, Map<Location, Double>> graph = createGraph(request);

            Map<Location, Double> shortestDistances = dijkstra(graph, request.getStartLocation());

            List<Location> shortestPath = findShortestPath(request, shortestDistances);

            double totalTime = calculateTotalTime(request, shortestPath);

            return new DeliveryResponse(shortestPath, totalTime);
        } catch (Exception ex) {
            logger.error("Error occurred while calculating shortest path", ex);
            throw ex;
        }
    }

    private void validateRequest(DeliveryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DeliveryRequest cannot be null");
        }

        if (request.getStartLocation() == null || request.getConsumer1() == null ||
                request.getConsumer2() == null || request.getRestaurant1() == null ||
                request.getRestaurant2() == null) {
            throw new IllegalArgumentException("All locations in the request must be non-null");
        }

        if (request.getStartLocation().equals(request.getConsumer1()) ||
                request.getStartLocation().equals(request.getConsumer2()) ||
                request.getStartLocation().equals(request.getRestaurant1()) ||
                request.getStartLocation().equals(request.getRestaurant2())) {
            throw new IllegalArgumentException("Start location cannot be the same as consumer or restaurant locations");
        }
    }

    private Map<Location, Map<Location, Double>> createGraph(DeliveryRequest request) {
        Map<Location, Map<Location, Double>> graph = new HashMap<>();

        addVertex(graph, request.getStartLocation());
        addVertex(graph, request.getConsumer1());
        addVertex(graph, request.getConsumer2());
        addVertex(graph, request.getRestaurant1());
        addVertex(graph, request.getRestaurant2());

        addEdge(graph, request.getStartLocation(), request.getRestaurant1());
        addEdge(graph, request.getStartLocation(), request.getRestaurant2());
        addEdge(graph, request.getRestaurant1(), request.getConsumer1());
        addEdge(graph, request.getRestaurant2(), request.getConsumer2());

        return graph;
    }

    private void addVertex(Map<Location, Map<Location, Double>> graph, Location location) {
        if (!graph.containsKey(location)) {
            graph.put(location, new HashMap<>());
        }
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
