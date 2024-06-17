package com.deliveryoptimizer.shortest_path_delivery.controller;

import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryRequest;
import com.deliveryoptimizer.shortest_path_delivery.model.DeliveryResponse;
import com.deliveryoptimizer.shortest_path_delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryResponse> calculateShortestPath(@RequestBody DeliveryRequest request) {
        try {
            DeliveryResponse response = deliveryService.calculateShortestPath(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}