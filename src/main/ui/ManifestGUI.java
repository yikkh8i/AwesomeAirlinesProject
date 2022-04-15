package ui;

import model.Flight;
import model.Guest;
import ui.exceptions.UnavailableFlightException;

import javax.swing.*;
import java.awt.*;

//Represents a JPanel that displays passenger manifests.
public class ManifestGUI extends JPanel {

    private ReservationAppGUI reservationAppGUI;

    //EFFECTS: instantiates a panel that will print out manifests
    public ManifestGUI(ReservationAppGUI reservationAppGUI) {
        setPreferredSize(new Dimension(200,200));
        setLayout(new GridLayout(0,1));
        this.reservationAppGUI = reservationAppGUI;
    }

    //MODIFIES: this
    //EFFECTS: gets user input on which flight manifest to print, then print that manifest.
    protected void printManifest() throws UnavailableFlightException {
        String flightNumString = JOptionPane.showInputDialog("Please input selected flight number (e.g. 101");
        int flightNum = Integer.parseInt(flightNumString);
        Flight selectedFlight = reservationAppGUI.selectFlight(flightNum);
        JLabel flightLabel = new JLabel("Manifest for AW" + selectedFlight.getFlightNum() + ":");
        this.add(flightLabel);
        printReservationList(selectedFlight);
    }

    //MODIFIES: this
    //EFFECTS: creates a label for each guest on the selected flight and adds it to this panel.
    private void printReservationList(Flight f) {
        int i = 0;
        for (Guest g : f.getReservationList()) {
            i++;
            JLabel guestLabel = new JLabel(i + ". " + g.getName().toUpperCase());
            this.add(guestLabel);
        }
        revalidate();
    }
}
