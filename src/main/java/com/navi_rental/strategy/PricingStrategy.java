package com.navi_rental.strategy;

public interface PricingStrategy {
    int calculateRentalCharge(int preDefinedPrice, int priceSortedVehicleOrderInGrp, int totalVehicleCountInCurrentCategory, int duration);
}
