package com.deliveryoptimizer.shortest_path_delivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DeliveryResponse {
    private List<Location> shortestPath;
    private double totalTime;
}