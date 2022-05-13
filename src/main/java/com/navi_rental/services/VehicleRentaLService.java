package com.navi_rental.services;

import com.navi_rental.exceptions.*;
import com.navi_rental.models.VehicleType;

public interface VehicleRentaLService {
    void onBoardBranch(String branchName, String[] vehicleTypes);
    void addVehicle(String branchName, VehicleType vehicleType, String vehicleId, int predefinedPrice) throws BranchNotExistsException, InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotSupportVehicleTypeException;
    void bookVehicleSlot(String branchName, VehicleType vehicleType, int startTime, int endTime) throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException;
    void displayVehicle(String branchName, int startTime, int endTime) throws BranchNotExistsException;
}
