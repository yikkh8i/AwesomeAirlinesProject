package ui;

import exceptions.FlightFullException;
import ui.exceptions.UnavailableFlightException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

//Represents a JPanel that displays buttons and allows user to make and cancel reservations, save, load and quit.

public class ButtonsGUI extends JPanel implements ActionListener {
    private ReservationAppGUI reservationAppGUI;

    //MODIFIES: this
    //EFFECTS: instantiates a panel that will print out buttons of functions, adds all buttons to the panel
    public ButtonsGUI(ReservationAppGUI reservationAppGUI) {
        this.reservationAppGUI = reservationAppGUI;
        setLayout(new GridLayout(0,1));
        addButtons(); // this adds all the tools to ButtonsGUI JPanel
    }

    // MODIFIES: this
    // EFFECTS:  a helper method which declares and instantiates all tools
    private void addButtons() {
        addButton("Make Reservation", "reserve", this);
        addButton("Cancel Reservation", "cancel", this);
        addButton("Show Manifest", "show", this);
        addButton("Save and Quit", "save", this);
        addButton("Load Reservation", "load", this);
    }

    //EFFECTS: creates a button and adds a command to it
    private void addButton(String title, String command, Container container) {
        JButton button = new JButton(title);
        button.setActionCommand(command);
        container.add(button);
        button.addActionListener(this);
    }

    //EFFECTS: assigns each action command with the corresponding method in reservationAppGUI
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "reserve":
                extractedMakeReservation();
                break;
            case "cancel":
                reservationAppGUI.doCancelReservation();
                break;
            case "show":
                extractedShowManifest();
                break;
            case "save":
                reservationAppGUI.saveAirlineAndQuit();
                break;
            case "load":
                reservationAppGUI.loadAirline();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + e.getActionCommand());
        }
    }

    //EFFECTS: calls doShowManifest in the reservationAppGUI to show passenger manifest
    private void extractedShowManifest() {
        try {
            reservationAppGUI.doShowManifest();
        } catch (UnavailableFlightException ex) {
            printUnavailableFlightMessage();
        }
    }

    //EFFECTS: calls doMakeReservation in the reservationAppGUI to make a new reservation
    private void extractedMakeReservation() {
        try {
            reservationAppGUI.doMakeReservation();
        } catch (UnavailableFlightException ex) {
            printUnavailableFlightMessage();
        } catch (FlightFullException flightFullException) {
            printFlightFullMessage();
        }
    }

    //EFFECTS: shows a warning message when user inputs a flight number that is invalid.
    private void printUnavailableFlightMessage() {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("sad.png")));
        System.out.println("Sorry, this flight is currently unavailable. \n");
        JOptionPane.showMessageDialog(this,
                "Sorry, this flight is currently unavailable.",
                "Unavailable Flight", JOptionPane.INFORMATION_MESSAGE, image);
    }

    //EFFECTS: shows a warning message when user tries to reserve a flight that is already full.
    private void printFlightFullMessage() {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("sad.png")));
        System.out.println("Sorry, this flight is already full. \n");
        JOptionPane.showMessageDialog(this,
                "Sorry, this flight is already full.",
                "Flight Full!", JOptionPane.INFORMATION_MESSAGE, image);
    }
}
