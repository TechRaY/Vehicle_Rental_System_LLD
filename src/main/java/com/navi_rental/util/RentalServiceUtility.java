package com.navi_rental.util;

import com.navi_rental.exceptions.InvalidVehicleException;
import com.navi_rental.models.*;

public class RentalServiceUtility {
    public static Vehicle getVehicle(VehicleType vehicleType, String id,int predefinedPrice) throws InvalidVehicleException {
        Vehicle vehicle;
        switch(vehicleType) {
            case CAR: vehicle = new Car(id, predefinedPrice); break;
            case BUS: vehicle = new Bus(id, predefinedPrice); break;
            case VAN: vehicle = new Van(id, predefinedPrice); break;
            case BIKE: vehicle = new Bike(id, predefinedPrice); break;
            default: throw new InvalidVehicleException("Invalid Vehicle Exception");
        }
        return vehicle;
    }

    public static int getPercentageToIncreasePrice( int bookedPercentage) {
        if (bookedPercentage >= 80)
            return 10;
        return 0;

    }
}
