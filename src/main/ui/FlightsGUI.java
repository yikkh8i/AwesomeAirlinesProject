package ui;

import model.Flight;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//Represents a JPanel that displays flight information

public class FlightsGUI extends JPanel {
    private ReservationAppGUI reservationAppGUI;

    //EFFECTS: instantiates a panel that will show flight information
    public FlightsGUI(ReservationAppGUI reservationAppGUI) {
        setPreferredSize(new Dimension(300,400));
        setLayout(new GridLayout(0,1));
        this.reservationAppGUI = reservationAppGUI;
    }

    //MODIFIES: this, reservationAppGUI
    //EFFECTS: clears the panel and prints out information of all flights available.
    protected void printFlights(List<Flight> flights) {
        reservationAppGUI.clearPrintedInfo();
        JLabel availableLabel = new JLabel("Available Flights: \n");
        this.add(availableLabel);
        for (Flight f : flights) {
            JLabel flightLabel = new JLabel(printFlightInfos(f));
            this.add(flightLabel);
        }
        revalidate();
    }

    //EFFECTS: prints out information for a flight, including flight number, origin and destination
    private String printFlightInfos(Flight f) {
        String s1 = "AW" + f.getFlightNum() + " " + f.getOrigin() + "->" + f.getDestination() + "\n";
        String s2 = "  Seats remaining: " + f.checkAvailability() + "\n";
        String s3 = "\n";
        return s3 + s1 + s3 + s2;
    }
}
