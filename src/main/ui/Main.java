package ui;

import java.io.FileNotFoundException;

//Calls a new ReservationAppGUI to start the application
public class Main {
    public static void main(String[] args) {
        try {
            new ReservationAppGUI();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
