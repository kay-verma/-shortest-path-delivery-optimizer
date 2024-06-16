package com.deliveryoptimizer.shortest_path_delivery.service;


import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryRequest;
import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryResponse;

public interface DeliveryService {
    DeliveryResponse calculateShortestPath(DeliveryRequest request);
}