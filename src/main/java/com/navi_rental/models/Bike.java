package com.navi_rental.models;

public class Bike extends Vehicle{
    public VehicleType vehicleType = VehicleType.BIKE;

    public Bike(String id, int price) {
        this.id = id;
        this.preDefinedPrice = price;
    }
}
