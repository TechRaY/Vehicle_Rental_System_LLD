package com.navi_rental.exceptions;

public class VehicleAlreadyExistsInBranchException extends Exception{
    public VehicleAlreadyExistsInBranchException(String message) {
        super(message);
    }
}
