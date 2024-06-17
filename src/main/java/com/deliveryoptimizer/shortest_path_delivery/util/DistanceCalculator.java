package com.deliveryoptimizer.shortest_path_delivery.util;


import com.deliveryoptimizer.shortest_path_delivery.model.Location;

public interface DistanceCalculator {
    double calculateDistance(Location location1, Location location2);
}