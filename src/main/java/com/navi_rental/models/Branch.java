package com.navi_rental.models;

import com.navi_rental.api.Inventory;
import com.navi_rental.exceptions.*;
import com.navi_rental.strategy.DefaultStrategy;
import com.navi_rental.strategy.DemandSupplyDynamicStrategy;
import com.navi_rental.strategy.PricingStrategy;
import com.navi_rental.util.RentalServiceUtility;


import java.util.*;

public class Branch {

    public String id;
    public PricingStrategy pricingStrategy = new DefaultStrategy();
    public Inventory<VehicleType, HashMap<String,Vehicle>> branchVehicleInventory = new Inventory<>();
    public Inventory<VehicleType, TreeMap<Integer,List<Vehicle>>> branchVehiclePriceSortedInventory = new Inventory<>();

    public Branch(String id, String[] vehicleTypes) {
        this.id = id;
        for(String vehicleType1 : vehicleTypes) {
            branchVehicleInventory.putInventory(VehicleType.valueOf(vehicleType1), new HashMap<String, Vehicle>());
            branchVehiclePriceSortedInventory.putInventory(VehicleType.valueOf(vehicleType1), new TreeMap<Integer, List<Vehicle>>());
        }
    }

    public Branch(String id, String[] vehicleTypes, PricingStrategy pricingStrategy) {
        this(id, vehicleTypes);
        this.pricingStrategy = pricingStrategy;
    }

    public void addVehicleToBranch(VehicleType vehicleType, String vehicleId, int predefinedPrice) throws InvalidVehicleException, VehicleAlreadyExistsInBranchException, BranchNotSupportVehicleTypeException {
        Vehicle vehicle = RentalServiceUtility.getVehicle(vehicleType, vehicleId, predefinedPrice);
        HashMap<String, Vehicle> branchVehicleMap = branchVehicleInventory.getInventory().get(vehicleType) ;
        if(branchVehicleMap == null) {
            System.out.println("FALSE Branch " + id + " does not support vehicle of type " + vehicleType);
            throw new BranchNotSupportVehicleTypeException("Branch does not support " + vehicleType + " type vehicle");
        }

        if(!branchVehicleMap.containsKey(vehicleId)) {
            branchVehicleMap.put(vehicleId, vehicle);
            TreeMap<Integer, List<Vehicle>> branchVehiclePriceSortedMap = branchVehiclePriceSortedInventory.getInventory().get(vehicleType);
            List<Vehicle> vehicleList = branchVehiclePriceSortedMap.getOrDefault(predefinedPrice, new ArrayList<Vehicle>());
            vehicleList.add(vehicle);
            branchVehiclePriceSortedMap.put(predefinedPrice, vehicleList);
            branchVehiclePriceSortedInventory.putInventory(vehicleType, branchVehiclePriceSortedMap);
            //TODO check back logic here
        } else {
            throw new VehicleAlreadyExistsInBranchException("Vehicle with this Id already exist");
        }
        branchVehicleInventory.putInventory(vehicleType, branchVehicleMap);
        System.out.println("TRUE Vehicle with id - " + vehicleId + " added to branch " +  id );
    }


    public boolean bookVehicle(VehicleType vehicleType, int startTime, int endTime) throws BranchNotSupportVehicleTypeException, SlotNotAvailableException, VehicleNotFoundException {
        TreeMap<Integer, List<Vehicle>> vehicleMap = branchVehiclePriceSortedInventory.getInventory().get(vehicleType);
        int vehicleListSize = branchVehicleInventory.getInventory().get(vehicleType).size();
        if(vehicleMap == null)
            throw new BranchNotSupportVehicleTypeException("Branch does not have this vehicle type");

        if(vehicleMap.size() == 0) {
            System.out.println("-1");
            throw new VehicleNotFoundException("Branch does not vehicle of this type right now");
        }

        boolean success = false;
        int count = 0;

        for (Map.Entry<Integer, List<Vehicle>> entry : vehicleMap.entrySet()) {
            for (Vehicle vehicle: entry.getValue()) {
                try{
                    success = vehicle.bookSlot(startTime, endTime, count + 1, vehicleListSize, pricingStrategy);
                    vehicle.reduceAvailableSlotHour(endTime - startTime);
                    break;
                } catch (SlotNotAvailableException exception) {
                    //TODO add logger or so to debug issues in prod
                }
                count++;
            }

            if(success) {
                break;
            }
        }

        if(!success){
            System.out.println("-1 Slot not available");
            throw new SlotNotAvailableException("Slot Not available");
        }

        return false;
    }

    public void findAvailableBranchVehicleOfVehicleTypeForSlot(VehicleType vehicleType, int startTime, int endTime) {
        boolean success = false;
        TreeMap<Integer, List<Vehicle>> priceSortedVehicles = new TreeMap<>();
        TreeMap<Integer,List<Vehicle>> vehicleListPriceSortedOfType = branchVehiclePriceSortedInventory.getInventory().get(vehicleType);

        for (Map.Entry<Integer, List<Vehicle>> vehicleTypePriceSortedVehicles : vehicleListPriceSortedOfType.entrySet()) {
            for (Vehicle vehicle: vehicleTypePriceSortedVehicles.getValue()) {
                success = vehicle.isVehicleAvailableBetweenTime(startTime, endTime);
                if(success) {
                    List<Vehicle> vehicleList = priceSortedVehicles.getOrDefault(vehicle.preDefinedPrice, new ArrayList<>());
                    vehicleList.add(vehicle);
                    priceSortedVehicles.put(vehicle.preDefinedPrice, vehicleList);
                }
            }
        }

    }

    public void displayAvailableBranchVehicleForSlot(int startTime, int endTime) {
        TreeMap<Integer, List<String>> priceSortedVehicles = new TreeMap<>();
        boolean success = false;
        for (Map.Entry<VehicleType, TreeMap<Integer,List<Vehicle>>> entry : branchVehiclePriceSortedInventory.getInventory().entrySet()) {
            for (Map.Entry<Integer, List<Vehicle>> vehicleTypePriceSortedVehicles : entry.getValue().entrySet()) {
                for (Vehicle vehicle: vehicleTypePriceSortedVehicles.getValue()) {
                    success = vehicle.isVehicleAvailableBetweenTime(startTime, endTime);
                    if(success) {
                        List<String> vehicleList = priceSortedVehicles.getOrDefault(vehicle.preDefinedPrice, new ArrayList<>());
                        vehicleList.add(vehicle.id);
                        priceSortedVehicles.put(vehicle.preDefinedPrice, vehicleList);
                    }
                }
            }
        }
        StringBuffer finalAnswer = new StringBuffer();

        for (Map.Entry<Integer, List<String>> entry: priceSortedVehicles.entrySet()) {
            for(String vehicleId : entry.getValue()) {
                finalAnswer.append(vehicleId).append(",");
            }
        }
        if(finalAnswer.charAt(finalAnswer.length() - 1) == ',')
            finalAnswer.deleteCharAt(finalAnswer.length() - 1);

        System.out.println(finalAnswer);
    }
}
