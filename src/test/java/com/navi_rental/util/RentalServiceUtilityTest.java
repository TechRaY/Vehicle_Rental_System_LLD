package com.navi_rental.util;

import com.navi_rental.exceptions.InvalidVehicleException;
import com.navi_rental.models.*;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class RentalServiceUtilityTest {

    @Test
    public static void testGetVehicle() throws InvalidVehicleException {
        Vehicle vehicle = RentalServiceUtility.getVehicle(VehicleType.CAR,"V1", 200 );
        assertEquals(vehicle.vehicleType, VehicleType.CAR);

        vehicle = RentalServiceUtility.getVehicle(VehicleType.BUS,"V2", 300 );
        assertEquals(vehicle.vehicleType, VehicleType.BUS);
        assertEquals(vehicle.preDefinedPrice, 300);
    }
}
