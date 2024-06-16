package com.deliveryoptimizer.shortest_path_delivery.model;

import lombok.Getter;

@Getter
public class DeliveryRequest {
    // Getter methods
    private Location startLocation;
    private Location consumer1;
    private Location consumer2;
    private Location restaurant1;
    private Location restaurant2;
    private double preparationTime1;
    private double preparationTime2;


}
