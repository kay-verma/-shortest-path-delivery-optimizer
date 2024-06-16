package com.deliveryoptimizer.shortest_path_delivery.controller;

import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryRequest;
import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryResponse;
import com.deliveryoptimizer.shortest_path_delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/delivery")
    public DeliveryResponse calculateShortestPath(@RequestBody DeliveryRequest request) {
        return deliveryService.calculateShortestPath(request);
    }
}
