package ui;

import exceptions.FlightFullException;
import model.Airline;
import model.Guest;

import model.Flight;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.AlreadyReservedException;
import ui.exceptions.UnavailableFlightException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

//ReservationApp represents an interface that allows guests to reserve, cancel and change flight reservations
//Contains references from FitLifeGym, HairSalon, JsonSerializationDemo and TellerApp projects

public class ReservationApp {
    //flights
    private Flight f1;
    private Flight f2;
    private Flight f3;
    private Flight f4;
    private List<Flight> allFlights;
    private List<Guest> allGuests;

    //commands
    private static final String MAKE_RESERVATION_COMMAND = "r";
    private static final String CANCEL_RESERVATION_COMMAND = "x";
    private static final String CHANGE_RESERVATION_COMMAND = "c";
    private static final String SAVE_COMMAND = "s";
    private static final String LOAD_COMMAND = "l";
    private static final String QUIT_COMMAND = "q";
    private static final String ERROR_MESSAGE = "This guest is not reserved on any flight. Please try again! \n";

    //JSON components
    private static final String JSON_STORE = "./data/workroom.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private Airline airline;
    private Boolean keepRunning;
    private Scanner input;


    //EFFECTS: runs the reservation application
    public ReservationApp() throws FileNotFoundException, FlightFullException {
        input = new Scanner(System.in);
        airline = new Airline("01JAN");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runReservationApp();
    }

    //Code from this method is referenced from Teller App poroject
    //MODIFIES: this
    //EFFECTS: processes user input
    private void runReservationApp() throws FlightFullException {
        this.keepRunning = true;
        String command;
        initializeAirline();

        while (keepRunning) {
            printMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals(QUIT_COMMAND)) {
                keepRunning = false;
            } else {
                try {
                    processCommand(command);
                } catch (UnavailableFlightException e) {
                    System.out.println("Sorry, this flight is currently unavailable. \n");
                }
            }
        }
        System.out.println("\n Thank you for choosing Awesome Airlines, goodbye!");
    }

    //EFFECTS: processes the command that is inputted
    private void processCommand(String s) throws UnavailableFlightException, FlightFullException {
        switch (s) {
            case LOAD_COMMAND:
                loadAirline();
                break;
            case SAVE_COMMAND:
                saveAirline();
                break;
            case QUIT_COMMAND:
                quitApp();
                break;
            case MAKE_RESERVATION_COMMAND:
                doMakeReservation();
                break;
            case CANCEL_RESERVATION_COMMAND:
                doCancelReservation();
                break;
            case CHANGE_RESERVATION_COMMAND:
                doChangeReservation();
                break;
            default:
                System.out.println("Invalid command.");
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: initializes flights
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
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    //EFFECTS: prints a list of possible options available for user
    private void printMainMenu() {
        System.out.println("\n -------------------WELCOME TO AWESOME AIRLINES!---------------------------------");
        System.out.println("Please select an option below:");
        System.out.println("Enter '" + MAKE_RESERVATION_COMMAND + "' to make a flight reservation.");
        System.out.println("Enter '" + CANCEL_RESERVATION_COMMAND + "' to cancel an existing flight reservation.");
        System.out.println("Enter '" + CHANGE_RESERVATION_COMMAND + "' to change an existing flight reservation.");
        System.out.println("Enter '" + SAVE_COMMAND + "' to save today's flights information.");
        System.out.println("Enter '" + LOAD_COMMAND + "' to load a previous day's flights information.");
        System.out.println("Enter '" + QUIT_COMMAND + "' to quit.");
    }

    //EFFECTS: returns current list of Flights, regardless if it is loaded from file or newly initialised app.
    private List<Flight> getCurrentFlights() {
        if (airline.getSavedFlights().size() == 0) {
            return allFlights;
        } else {
            return airline.getSavedFlights();
        }
    }

    //EFFECTS: returns list of Guests with a reservation regardless if it is loaded from file or newly initialised app.
    private List<Guest> getCurrentGuests() {
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
    private Boolean guestInAllGuests(String guestName) {
        for (Guest g : getCurrentGuests()) {
            if (g.getName().equals(guestName)) {
                return true;
            }
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: makes a reservation for the user, adds the guest to guest list
    private void doMakeReservation() throws UnavailableFlightException, FlightFullException {
        Guest selected = inputGuest();
        if (guestInAllGuests(selected.getName())) {
            try {
                throw new AlreadyReservedException();
            } catch (AlreadyReservedException e) {
                for (Guest g : getCurrentGuests()) {
                    System.out.println("This guest is already reserved on AW" + g.getReservedFlightNum() + ".");
                }
            }
        } else {
            printFlights();
            System.out.println("Please input selected flight number (e.g. input '101' for flight AW101): ");
            Flight selectedFlight = selectFlight();
            if (getCurrentFlights().contains(selectedFlight)) {
                if (selectedFlight != null) {
                    selectedFlight.makeReservation(selected);
                    getCurrentGuests().add(selected);
                }
            } else {
                throw new UnavailableFlightException();
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: cancels an existing flight
    private void doCancelReservation() throws UnavailableFlightException, FlightFullException {
        //System.out.println("Please input guest name: ");
        Guest g = lookupGuest();

        if (!(getCurrentGuests().contains(g))) {
            System.out.println(ERROR_MESSAGE);
        } else {
            assert g != null;
            System.out.println(g.getName() + " is reserved on AA" + g.getReservedFlightNum() + ".");
            Flight f = lookupFlight(g.getReservedFlightNum());
            System.out.println(g.getName() + ", would you like to cancel this reservation?");
            System.out.println("Input Y for yes, Q to quit.");
            String selection = input.next();
            selection = selection.toLowerCase();
            if (selection.equals("y")) {
                if (f != null) {
                    f.cancelReservation(g);
                    getCurrentGuests().remove(g);
                }
            } else if (selection.equals("q")) {
                processCommand(QUIT_COMMAND);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: changes an existing reservation to one with a new flight.
    private void doChangeReservation() throws UnavailableFlightException, FlightFullException {
        Guest g = lookupGuest();
        if (g == null) {
            System.out.println(ERROR_MESSAGE);
        } else {
            for (Flight f : getCurrentFlights()) {
                if ((g.getReservedFlightNum() != 0) && (f.getFlightNum() == g.getReservedFlightNum())) {
                    System.out.println(g.getName() + " is currently reserved on AW" + g.getReservedFlightNum() + ".");
                    System.out.println("Would you like to change this reservation? Input Y for yes, any key to return");
                    String selection = input.next();
                    selection = selection.toLowerCase();
                    if (selection.equals("y")) {
                        printFlights();
                        System.out.println("Please input new flight number: ");
                        Flight selectedFlight = selectFlight();
                        if (selectedFlight != null) {
                            f.changeReservation(g, selectedFlight);
                        }
                    }
                }
            }
        }
    }


    //REQUIRES: user input must be a string
    //EFFECTS: prompts user to input guest name
    private Guest inputGuest() {
        System.out.println("Please input guest name: ");
        String guestName = input.next();
        for (Guest g : getCurrentGuests()) {
            if (guestName.equals(g.getName())) {
                return g;
            }
        }
        return new Guest(guestName);
    }

    //EFFECTS: searches and returns the guest if found in the airlines guests list.
    private Guest lookupGuest() {
        System.out.println("Please input guest name: ");
        String s = input.next();
        for (Guest g : getCurrentGuests()) {
            if (g.getName().equals(s)) {
                return g;
            }
        }
        return null;
    }

    //REQUIRES: flight number inputted must be of those flights shown in menu, i.e. available flights only.
    //EFFECTS: prompts user to select a flight by inputting its flight number.
    private Flight selectFlight() throws UnavailableFlightException {
        int selectedFlight;
        selectedFlight = input.nextInt();

        for (Flight f: getCurrentFlights()) {
            if (selectedFlight == (f.getFlightNum())) {
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

    //EFFECTS: prints out all flights available (including flight number, origin and destination)
    private void printFlights() {
        System.out.println("Available flights:");
        for (Flight f: getCurrentFlights()) {
            System.out.println("AW" + f.getFlightNum() + " " + f.getOrigin() + "->" + f.getDestination());
            System.out.println("  Seats remaining: " + f.checkAvailability());
        }
    }

    //EFFECTS: saves the airline information to file
    private void saveAirline() {
        for (Flight f : getCurrentFlights()) {
            airline.addFlight(f);
        }
        try {
            jsonWriter.open();
            jsonWriter.write(airline);
            jsonWriter.close();
            System.out.println("Saved flight information of " + airline.getDate() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: loads airline from file
    private void loadAirline() {
        try {
            airline = jsonReader.read();
            System.out.println("Loaded flight information of " + airline.getDate() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //EFFECTS: quits the reservation app
    private void quitApp() {
        this.keepRunning = false;
    }
}

