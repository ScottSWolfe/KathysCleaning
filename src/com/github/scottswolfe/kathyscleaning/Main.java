package com.github.scottswolfe.kathyscleaning;

import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;

/**
 * This class contains the main method and launches the application.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationCoordinator.getInstance().startApplication();
    }
}
