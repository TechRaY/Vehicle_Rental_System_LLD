package com.navi_rental.strategy;

import com.navi_rental.util.RentalServiceUtility;

public class DemandSupplyDynamicStrategy  implements PricingStrategy{

    int calculatePercentage(int count, int totalCount) {
        return  ((count * 100 )/ totalCount) ;
    }

    int getXPercentageOf(int x, int value) {
        return ((x * value )/100) ;
    }

    public  int calculateRentalCharge(int preDefinedPrice, int priceSortedVehicleOrderNoInGrp, int totalVehicleCountInCurrentCategory, int duration) {
        int percentage = calculatePercentage(priceSortedVehicleOrderNoInGrp, totalVehicleCountInCurrentCategory);
        return (preDefinedPrice + getXPercentageOf(RentalServiceUtility.getPercentageToIncreasePrice(percentage), preDefinedPrice)) * duration;
    }
}