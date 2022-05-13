package com.navi_rental.models;

import com.navi_rental.exceptions.SlotNotAvailableException;
import com.navi_rental.strategy.PricingStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle {
    public String id;
    public int preDefinedPrice;
    public int availableSlotHours = 24;
    public VehicleType vehicleType;
    public List<Slot> slotsBooked = new ArrayList<>();

    public void reduceAvailableSlotHour(int hours) {
        this.availableSlotHours = this.availableSlotHours - hours;
    }

    public boolean isVehicleAvailable() {
        return availableSlotHours > 0;
    }

    public boolean isVehicleAvailableBetweenTime(int startTime, int endTime) {
        if(isVehicleAvailable()) {
            for(Slot slot: slotsBooked) {
                //complete overlap
                if((slot.startTime == startTime) && (slot.endTime == endTime))
                    return false;

                //slot in between
                if((startTime < slot.startTime && slot.startTime < endTime) || (startTime < slot.endTime && slot.endTime < endTime))
                    return false;
                //starttime endtime in between
                if((slot.startTime < startTime && startTime < slot.endTime) || (slot.startTime < endTime && endTime < slot.endTime))
                    return false;

            }
            return true;
        }
        return false;
    }

    public boolean bookSlot(int startTime, int endTime, int priceSortedVehicleOrderInGrp, int totalVehicleCountInCurrentCategory, PricingStrategy pricingStrategy) throws SlotNotAvailableException {
        if(isVehicleAvailableBetweenTime(startTime, endTime)) {
            int calculatedPrice = pricingStrategy.calculateRentalCharge(preDefinedPrice, priceSortedVehicleOrderInGrp, totalVehicleCountInCurrentCategory, endTime - startTime);
            Slot bookedSlot = new Slot(startTime, endTime, calculatedPrice);
            System.out.println("Price " + calculatedPrice + " Booked slot");
            slotsBooked.add(bookedSlot);
        } else {
            throw new SlotNotAvailableException("Slots not available");
        }
        return true;
    }
}
