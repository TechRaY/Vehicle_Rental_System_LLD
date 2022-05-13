package com.navi_rental.models;

public class Bus extends Vehicle{
    public VehicleType vehicleType = VehicleType.BUS;

    public Bus(String id, int price) {
        this.id = id;
        this.preDefinedPrice = price;
    }
}
