package com.navi_rental.services;

import com.navi_rental.api.Inventory;
import com.navi_rental.exceptions.*;
import com.navi_rental.models.Branch;
import com.navi_rental.models.Vehicle;
import com.navi_rental.models.VehicleType;
import com.navi_rental.util.RentalServiceUtility;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class VehicleRentalServiceImplTest {

    VehicleRentalServiceImpl vehicleRentalServiceImpl = new VehicleRentalServiceImpl();
    String branchName = "B1";
    String[] vehicleList = {"CAR", "BUS"};

    void addBranch(String branchName, String[] vehicleList) {
        this.branchName = branchName;
        this.vehicleList = vehicleList;
        vehicleRentalServiceImpl.onBoardBranch(branchName, vehicleList);
    }
    @Test
    public void testOnBoardBranch() {
        assertEquals(vehicleRentalServiceImpl.branchInventory.getInventory().get(branchName),null );
        addBranch(branchName, vehicleList);
        Branch branch = vehicleRentalServiceImpl.branchInventory.getInventory().get(branchName);
        assertEquals(branch.id,branchName );
    }

    @Test
    public void testAddVehicle() throws InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotExistsException, BranchNotSupportVehicleTypeException {
        addBranch(branchName, vehicleList);
        Branch branch = vehicleRentalServiceImpl.branchInventory.getInventory().get(branchName);
        HashMap<String,Vehicle> vehicleMap = branch.branchVehicleInventory.getInventory().get(VehicleType.CAR);
        assertEquals(vehicleMap.size(),0);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
        assertEquals(vehicleMap.size(),1);
    }

    @Test(expected = BranchNotSupportVehicleTypeException.class)
    public void testAddVehicleInValidException() throws InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotExistsException, BranchNotSupportVehicleTypeException {
        addBranch(branchName, vehicleList);
        Branch branch = vehicleRentalServiceImpl.branchInventory.getInventory().get(branchName);
        HashMap<String,Vehicle> vehicleMap = branch.branchVehicleInventory.getInventory().get(VehicleType.CAR);
        assertEquals(vehicleMap.size(),0);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.VAN, "V1" , 100);
    }

    @Test(expected = VehicleAlreadyExistsInBranchException.class)
    public void testAddVehicleVehicleAlreadyExistsInBranchException() throws InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotExistsException, BranchNotSupportVehicleTypeException {
        addBranch(branchName, vehicleList);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
    }

    @Test(expected = BranchNotExistsException.class)
    public void testAddVehicleException() throws InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotExistsException, BranchNotSupportVehicleTypeException {
        vehicleRentalServiceImpl.addVehicle("B2",VehicleType.VAN, "V3" , 100);
    }

    @Test
    public void testBookVehicleSlot() throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException, InvalidVehicleException, VehicleAlreadyExistsInBranchException {
        addBranch(branchName, vehicleList);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.CAR, 2,5);
    }

    @Test(expected = SlotNotAvailableException.class)
    public void testBookVehicleSlotSlotNotAvailableException() throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException, InvalidVehicleException, VehicleAlreadyExistsInBranchException {
        addBranch(branchName,vehicleList);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.CAR, 2,5);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.CAR, 3,5);
    }

    @Test(expected = BranchNotExistsException.class)
    public void testBookVehicleSlotBranchNotExistsException() throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException, InvalidVehicleException, VehicleAlreadyExistsInBranchException {
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.CAR, 2,5);
    }

    @Test(expected = BranchNotSupportVehicleTypeException.class)
    public void testBookVehicleSlotBranchNotSupportVehicleTypeException() throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException, InvalidVehicleException, VehicleAlreadyExistsInBranchException {
        addBranch(branchName,vehicleList);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.VAN, "V1" , 100);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.CAR, 2,5);
    }

    @Test(expected = VehicleNotFoundException.class)
    public void testBookVehicleSlotVehicleNotFoundException() throws BranchNotExistsException, BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException, InvalidVehicleException, VehicleAlreadyExistsInBranchException {
        addBranch(branchName,vehicleList);
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.CAR, "V1" , 100);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.BUS, 2,5);
    }

    @Test
    public void testDisplayVehicle() throws BranchNotExistsException, VehicleNotFoundException, SlotNotAvailableException, InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotSupportVehicleTypeException {
        testBookVehicleSlot();
        vehicleRentalServiceImpl.addVehicle(branchName,VehicleType.BUS, "BS1" , 100);
        vehicleRentalServiceImpl.bookVehicleSlot(branchName, VehicleType.BUS, 7,9);
        vehicleRentalServiceImpl.displayVehicle(branchName,1,5);
    }

    @Test(expected = BranchNotExistsException.class)
    public void testDisplayVehicleBranchNotExistsException() throws BranchNotExistsException, VehicleNotFoundException, SlotNotAvailableException, InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotSupportVehicleTypeException {
        testBookVehicleSlot();
        vehicleRentalServiceImpl.displayVehicle("B2",1,5);
    }

}
