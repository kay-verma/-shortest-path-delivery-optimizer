package com.deliveryoptimizer.shortest_path_delivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {
    private Location startLocation;
    private Location consumer1;
    private Location consumer2;
    private Location restaurant1;
    private Location restaurant2;
    private double preparationTime1;
    private double preparationTime2;
}
