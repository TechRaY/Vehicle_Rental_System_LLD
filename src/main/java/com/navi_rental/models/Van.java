package com.navi_rental.models;

public class Van extends Vehicle{
    public VehicleType vehicleType = VehicleType.VAN;

    public Van(String id, int price) {
        this.id = id;
        this.preDefinedPrice = price;
    }
}
