package ui;

import exceptions.FlightFullException;
import model.Airline;
import model.EventLog;
import model.Flight;
import model.Guest;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.AlreadyReservedException;
import ui.exceptions.UnavailableFlightException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Represents a JFrame that contains panels for buttons, flight information and passenger manifest.
public class ReservationAppGUI extends JFrame {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;

    //flights
    protected Flight f1;
    protected Flight f2;
    protected Flight f3;
    protected Flight f4;
    protected java.util.List<Flight> allFlights;
    protected List<Guest> allGuests;
    private Airline airline;

    //panels
    private ButtonsGUI toolsArea;
    protected FlightsGUI flightsArea;
    protected ManifestGUI manifestArea;

    //JSON components
    private static final String JSON_STORE = "./data/workroom.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //EFFECTS: runs the reservation application
    public ReservationAppGUI() throws FileNotFoundException {
        super("Awesome Airlines Reservation App");
        this.airline = new Airline("01JAN");
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonReader(JSON_STORE);
        welcomeMessage();
        initializeAirline();
        initializeGraphics();
    }

    //EFFECTS: shows a welcome message when the application is started, as a JOptionPane
    private void welcomeMessage() {
        ImageIcon image1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("Logo2.png")));
        JOptionPane.showMessageDialog(this, "Welcome to Awesome Airlines!",
                "Awesome Airlines", JOptionPane.INFORMATION_MESSAGE, image1);
    }

    //MODIFIES: this
    //EFFECTS: initializes all flights, instantiates the allFlights and allGuests list.
    private void initializeAirline() {
        f1 = new Flight(101, "YVR", "YYZ");
        f2 = new Flight(102, "YVR", "YYC");
        f3 = new Flight(103, "YVR", "YOW");
        f4 = new Flight(104, "YVR", "YUL");
        this.allFlights = new ArrayList<>();
        allFlights.add(f1);
        allFlights.add(f2);
        allFlights.add(f3);
        allFlights.add(f4);
        this.allGuests = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: draws the JFrame window where the ReservationApp will operate and initializes 3 internal panels.
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitApp();
            }
        });

        createToolsGUI();
        createFlightsGUI();
        createManifestGUI();

        pack();
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: creates a panel for buttons, initializes the buttons and adds the panel onto this JFrame.
    private void createToolsGUI() {
        this.toolsArea = new ButtonsGUI(this);
        add(toolsArea, BorderLayout.WEST);
    }

    //MODIFIES: this
    //EFFECTS: creates a panel for displaying flight info and adds the panel onto this JFrame.
    private void createFlightsGUI() {
        this.flightsArea = new FlightsGUI(this);
        add(flightsArea, BorderLayout.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: creates a panel for showing flight manifest and adds the panel onto this JFrame.
    private void createManifestGUI() {
        this.manifestArea = new ManifestGUI(this);
        add(manifestArea, BorderLayout. EAST);
    }

    //EFFECTS: helper method to process user input to create new guest or return existing guest in guest list.
    private Guest inputGuest(String guestName) {
        for (Guest g: getCurrentGuests()) {
            if (guestName.equals(g.getName())) {
                return g;
            }
        }
        return new Guest(guestName);
    }

    //EFFECTS: helper method to process user input to return a flight in flight list.
    protected Flight selectFlight(int flightNum) throws UnavailableFlightException {
        for (Flight f: getCurrentFlights()) {
            if (flightNum == (f.getFlightNum())) {
                return f;
            }
        }
        throw new UnavailableFlightException();
    }

    //REQUIRES: input must be 3 digit integers that correspond to a flight number
    //EFFECTS: returns a flight in airline's list of flights that has this flight number.
    private Flight lookupFlight(int flightNum) {
        for (Flight f : getCurrentFlights()) {
            if (f.getFlightNum() == flightNum) {
                return f;
            }
        }
        return null;
    }

    //MODIFIES: this, flightsArea
    //EFFECTS: prints all available flights, then makes a reservation and adds the guest to guest list,
    //          according to user input.
    protected void doMakeReservation() throws UnavailableFlightException, FlightFullException {
        flightsArea.printFlights(getCurrentFlights());
        String guestName = JOptionPane.showInputDialog("Please input guest name: ");
        Guest selectedGuest = inputGuest(guestName.toUpperCase());
        if (guestInAllGuests(selectedGuest.getName())) {
            try {
                throw new AlreadyReservedException();
            } catch (AlreadyReservedException e) {
                printAlreadyReservedError(selectedGuest.getName(), selectedGuest.getReservedFlightNum());
            }
        } else {
            String flightNumString = JOptionPane.showInputDialog("Please input selected flight number (e.g. 101)");
            int flightNum = Integer.parseInt(flightNumString);
            Flight selectedFlight = selectFlight(flightNum);
            if (getCurrentFlights().contains(selectedFlight)) {
                selectedFlight.makeReservation(selectedGuest);
                getCurrentGuests().add(selectedGuest);
                printReservationConfirmation(selectedGuest, selectedFlight);
            } else {
                throw new UnavailableFlightException();
            }
        }
    }

    //EFFECTS: shows a confirmation message once the reservation has been made, as a JOptionPane
    private void printReservationConfirmation(Guest g, Flight f) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("excited.png")));
        JOptionPane.showMessageDialog(this,
                g.getName().toUpperCase() + " is now reserved on AW" + f.getFlightNum()
                        + ". \n Get ready for your trip to " + f.getDestination() + "!",
                "Reservation Success!", JOptionPane.INFORMATION_MESSAGE, image);
        clearPrintedInfo();
    }

    //EFFECTS: shows an error message when user reserves a guest that has already been reserved, as a JOptionPane
    private void printAlreadyReservedError(String name, int flightNum) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("sad.png")));
        JOptionPane.showMessageDialog(this,
                name.toUpperCase() + " is already reserved on AW" + flightNum + ".",
                "Awesome Airlines", JOptionPane.INFORMATION_MESSAGE, image);
    }

    //EFFECTS: returns list of Guests with a reservation regardless if it is loaded from file or newly initialised app.
    protected List<Guest> getCurrentGuests() {
        if (airline.getSavedFlights().size() == 0) {
            return allGuests;
        } else {
            List<Guest> savedGuests = new ArrayList<>();
            for (Flight f : airline.getSavedFlights()) {
                savedGuests.addAll(f.getReservationList());
            }
            return savedGuests;
        }
    }

    //EFFECTS: checks whether guest is already in list of guests using guest name, return true if so.
    protected Boolean guestInAllGuests(String guestName) {
        for (Guest g : getCurrentGuests()) {
            if (g.getName().equals(guestName)) {
                return true;
            }
        }
        return false;
    }

    //EFFECTS: returns current list of Flights, regardless if it is loaded from file or newly initialised app.
    protected List<Flight> getCurrentFlights() {
        if (airline.getSavedFlights().size() == 0) {
            return allFlights;
        } else {
            return airline.getSavedFlights();
        }
    }

    //EFFECTS: searches and returns the guest if found in the airlines guests list.
    private Guest lookupGuest(String guestName) {
        for (Guest g : getCurrentGuests()) {
            if (g.getName().equals(guestName)) {
                return g;
            }
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: allows user to input guest name and cancel the guest's existing reservation, if there is one.
    protected void doCancelReservation() {
        String guestName = JOptionPane.showInputDialog("Please input guest name: ");
        Guest selectedGuest = lookupGuest(guestName.toUpperCase());

        if (selectedGuest == null || !(getCurrentGuests().contains(selectedGuest))) {
            printGuestNotExistMessage();
            return;
        }

        Flight f = lookupFlight(selectedGuest.getReservedFlightNum());
        assert f != null;
        String message = selectedGuest.getName() + " is reserved on AW" + f.getFlightNum()
                + ", would you like to cancel this reservation?";
        int confirmation = JOptionPane.showConfirmDialog(this, message, "Cancellation",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); //yes = 0, no = 1
        if (confirmation == 0) {
            f.cancelReservation(selectedGuest);
            getCurrentGuests().remove(selectedGuest);
            printCancellationMessage(selectedGuest);
        }
    }

    //EFFECTS: shows a message dialog with the cancellation confirmation message
    private void printCancellationMessage(Guest g) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("excited.png")));
        JOptionPane.showMessageDialog(this,
                g.getName().toUpperCase() + "'s reservation has been cancelled.",
                "Cancellation Success!", JOptionPane.INFORMATION_MESSAGE, image);
    }

    //EFFECTS: shows an error message dialog when user attempts to cancel a non-existing reservation
    private void printGuestNotExistMessage() {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("sad.png")));
        JOptionPane.showMessageDialog(this,
                "OOPS! This guest is not reserved on any flight.",
                "Uh-Oh!", JOptionPane.INFORMATION_MESSAGE, image);
    }

    //MODIFIES: this
    //EFFECTS: loads airline from file
    protected void loadAirline() {
        try {
            airline = jsonReader.read();
            System.out.println("Loaded flight information of " + airline.getDate() + " from " + JSON_STORE);
            JOptionPane.showMessageDialog(this,
                    "Loaded flight information of " + airline.getDate() + " from " + JSON_STORE,
                    "Load Success!", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            JOptionPane.showMessageDialog(this,
                    "Unable to read from file: " + JSON_STORE,
                    "Load Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    //EFFECTS: saves the airline information to file
    protected void saveAirlineAndQuit() {
        for (Flight f : getCurrentFlights()) {
            airline.addFlight(f);
        }
        try {
            jsonWriter.open();
            jsonWriter.write(airline);
            jsonWriter.close();
            System.out.println("Saved flight information of " + airline.getDate() + " to " + JSON_STORE + "\n");
            JOptionPane.showMessageDialog(this,
                    "Saved flight information of " + airline.getDate() + " to " + JSON_STORE
                            + ". The app will quit now.",
                    "Save Success!", JOptionPane.PLAIN_MESSAGE);
            this.dispose();
            printLog(EventLog.getInstance());
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
            JOptionPane.showMessageDialog(this,
                    "Unable to write to file: " + JSON_STORE,
                    "Save Failure!", JOptionPane.ERROR_MESSAGE);
        }
    }

    //MODIFIES: this, manifestArea, flightsArea
    //EFFECTS: clears manifestArea and flightsArea, then prints list of available flights and the requested manifest.
    protected void doShowManifest() throws UnavailableFlightException {
        clearPrintedInfo();
        flightsArea.printFlights(getCurrentFlights());
        manifestArea.printManifest();
    }

    //MODIFIES: this, flightsArea, manifestArea
    //EFFECTS: clears all labels from flightsArea and manifestArea
    protected void clearPrintedInfo() {
        flightsArea.removeAll();
        flightsArea.revalidate();
        manifestArea.removeAll();
        manifestArea.revalidate();
    }

    //EFFECTS: prints the event log when the application quits.
    public void printLog(EventLog el) {
        System.out.println();
        System.out.println("AW Event Log:");
        for (Event e : el) {
            System.out.println(e.toString());
        }
    }

    //MODIFIES: this
    //EFFECTS: when called, prints the event log to the console and disposes the GUI
    private void exitApp() {
        printLog(EventLog.getInstance());
        this.dispose();
    }
}
