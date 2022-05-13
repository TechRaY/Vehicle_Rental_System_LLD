package com.navi_rental;

import com.navi_rental.exceptions.*;
import com.navi_rental.models.Commands;
import com.navi_rental.models.Vehicle;
import com.navi_rental.models.VehicleType;
import com.navi_rental.services.VehicleRentalServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static  void main(String args[]) {
        VehicleRentalServiceImpl vehicleRentalService = new VehicleRentalServiceImpl();
        Scanner scanner = new Scanner(System.in);

        while(true)
        {
            try {
                System.out.println("Enter any of the commands - ADD_BRANCH, ADD_VEHICLE, BOOK_VEHICLE, DISPLAY_VEHICLE, EXIT");
                String commandLine[] = scanner.nextLine().split(" ");
                Commands command = Commands.valueOf(commandLine[0]);

                switch (command) {
                    case ADD_BRANCH:
                        String[] vehicleTypes = commandLine[2].split(",");
                        vehicleRentalService.onBoardBranch(commandLine[1], vehicleTypes);
                        break;
                    case ADD_VEHICLE:
                        vehicleRentalService.addVehicle(commandLine[1], VehicleType.valueOf(commandLine[2]), commandLine[3], Integer.parseInt(commandLine[4]));
                        break;
                    case BOOK:
                        vehicleRentalService.bookVehicleSlot(commandLine[1], VehicleType.valueOf(commandLine[2]),Integer.parseInt(commandLine[3]), Integer.parseInt(commandLine[4]));
                        break;
                    case DISPLAY_VEHICLES:
                        vehicleRentalService.displayVehicle(commandLine[1], Integer.parseInt(commandLine[2]), Integer.parseInt(commandLine[3]));
                        break;
                    case EXIT:
                        System.out.println("Shutting down system");
                        System.exit(0);
                    default: System.out.println("Invalid Command Chosen");
                }
            } catch (Exception exception) {
                System.out.println("Following error occurred with message " + exception.getMessage());
            }
        }
    }
}
