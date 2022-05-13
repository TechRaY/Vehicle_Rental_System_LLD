package com.navi_rental.strategy;

public class DefaultStrategy implements PricingStrategy{

    public int calculateRentalCharge(int preDefinedPrice, int priceSortedVehicleOrderInGrp, int totalVehicleCountInCurrentCategory, int duration) {
        return preDefinedPrice * duration;
    }
}
