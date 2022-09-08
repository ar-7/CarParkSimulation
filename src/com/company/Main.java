package com.company;

import java.util.Scanner;

// This class starts the car threads and contains the functionality for allowing cars in and out of the car park
public class Main {

    static int carParkSize = 15; // Declare size of car park, default value for testing
    static int carTotal = 0; // Declare variable for total number of cars logged
    static int currentSpacesAvailable; // Declare variable for current available spaces

    // Main runnable function for the program
    // Starts the threads that handle the cars coming and going
    public static void main(String[] args) {

        System.out.println("Hello, car park manager is running."); // Greeting

        Scanner scanInput = new Scanner(System.in);  // Create a Scanner object
        System.out.println("How many spaces does the car park have?"); // Prompt the user for a number
        // Validate against wrong input. Loops until correct input is given
        while (!scanInput.hasNextInt() || (carParkSize = scanInput.nextInt()) <= 0) {
            System.out.println("Sorry, I'm going to need a positive number, please try again.");
            scanInput.nextLine(); // This is important! Stuck in the loop otherwise
            scanInput.close();
        }

        reportSpaces(); // Calls the function that calculates the current spaces available

        // Start car threads
        // In thread
        CarInThread carInThread = new CarInThread(); // New instance of the thread
        carInThread.start(); // Starting the instance
        // Out thread
        CarOutThread carOutThread = new CarOutThread(); // New instance of the thread
        carOutThread.start(); // Starting the instance
    }

    // This function updates that current available spaces and reports back to the console
    synchronized static void reportSpaces() {
        currentSpacesAvailable = carParkSize - carTotal; // Calculate and update available spaces
        System.out.println("The car park has " + currentSpacesAvailable + " spaces available."); // Report the result
    }

    // This function simulates the manager allowing cars in one at a time
    // If the car park is full the car may not enter
    synchronized static void allowCarIn() {
        if (carTotal < carParkSize) { // if the car park is not full
            System.out.println("Allowing car in.");  // report
            carTotal++; // Add the car to the total
            reportSpaces(); // Call the function that updates the currently available spaces
        } else {
            System.out.println("Car park is full.");
        }
    }

    synchronized static void letCarOut() {
        if (!(Main.carTotal <= 0)) { // Only runs if the car park is not empty
            System.out.println("Letting car out."); // report
            carTotal--; // subtract the car from the total
            reportSpaces(); // Call the function that updates the currently available spaces
        } else {
            System.out.println("Waiting for cars.");
        }
    }
}

// This class simulates the arrival of cars to the car park
class CarInThread extends Thread {

    // Run function for the thread
    public void run() {
        // Infinite loop simulating the passing of time
        while (true){
            // Random wait between each car arriving
            try {
                Thread.sleep((long)(Math.random() * 6500)); // Less time between arrivals than exits
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Next car wants to park.");
            Main.allowCarIn(); // Call the manager's carIn function
        }
    }
}

// This class simulates the cars leaving the car park
class CarOutThread extends Thread {

    // Run function for the thread
    public void run() {

        // Infinite loop simulating the passing of time
        while (true) {
            // Random wait between each car leaving
            try {
                Thread.sleep((long) (Math.random() * 9000)); // More time between exits than arrivals
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Next car wants to leave.");
            Main.letCarOut();// Call the manager's carOut function
        }
    }
}

