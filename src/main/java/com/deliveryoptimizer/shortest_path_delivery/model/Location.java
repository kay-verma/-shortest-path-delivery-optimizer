package com.deliveryoptimizer.shortest_path_delivery.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location {
    private double latitude;
    private double longitude;


    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}