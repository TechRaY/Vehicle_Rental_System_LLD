package com.navi_rental.models;

public class Car extends Vehicle{
    public VehicleType vehicleType = VehicleType.CAR;

    public Car(String id, int price) {
        this.id = id;
        this.preDefinedPrice = price;
    }
}
