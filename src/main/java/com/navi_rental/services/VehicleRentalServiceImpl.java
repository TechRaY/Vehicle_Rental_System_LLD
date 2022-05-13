package com.navi_rental.services;

import com.navi_rental.api.Inventory;
import com.navi_rental.exceptions.*;
import com.navi_rental.models.Branch;
import com.navi_rental.models.Vehicle;
import com.navi_rental.models.VehicleType;
import com.navi_rental.strategy.DemandSupplyDynamicStrategy;
import com.navi_rental.util.RentalServiceUtility;
import java.util.List;

public class VehicleRentalServiceImpl implements VehicleRentaLService{

    Inventory<String,Branch> branchInventory = new Inventory<String, Branch>();

    @Override
    public void onBoardBranch(String branchName, String[] vehicleTypes) {
        branchInventory.putInventory(branchName, new Branch(branchName, vehicleTypes, new DemandSupplyDynamicStrategy()));
        System.out.println("TRUE");
    }

    @Override
    public void addVehicle(String branchName, VehicleType vehicleType, String vehicleId, int predefinedPrice) throws BranchNotExistsException, InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotSupportVehicleTypeException {
        if(branchInventory.getInventory().containsKey(branchName)) {
            Branch branch = branchInventory.getInventory().get(branchName);
            branch.addVehicleToBranch(vehicleType, vehicleId, predefinedPrice);
        } else {
            throw new BranchNotExistsException("First Add Branch and then assign Vehicle to it");
        }
    }


    @Override
    public void bookVehicleSlot(String branchName, VehicleType vehicleType, int startTime, int endTime) throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException {
        if(branchInventory.getInventory().containsKey(branchName)) {
            Branch branch = branchInventory.getInventory().get(branchName);
            branch.bookVehicle(vehicleType, startTime, endTime);
        }else {
            throw new BranchNotExistsException("Branch does not exist");
        }
    }

    @Override
    public void displayVehicle(String branchName, int startTime, int endTime) throws BranchNotExistsException{
        if(branchInventory.getInventory().containsKey(branchName)) {
            Branch branch = branchInventory.getInventory().get(branchName);
            branch.displayAvailableBranchVehicleForSlot(startTime, endTime);
        }else {
            throw new BranchNotExistsException("Branch does not exist");
        }
    }
}
