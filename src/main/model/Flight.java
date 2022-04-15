package model;

import exceptions.FlightFullException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

//Flight represents one flight that is operated by the airline, with a max capacity of 100 pax.
public class Flight implements Writable {
    public static final int MAX_CAPACITY = 2;
    private int flightNum;
    private String origin;
    private String destination;
    private int currentLoad;
    private List<Guest> reservationList;


    //REQUIRES: origin and destination are 3 letter airport code, flightNum > 0, currentCapacity >= 0.
    //EFFECTS: Constructs a flight with a flight number, an origin and destination, with zero currrent capacity
    //         and an empty passenger manifest.
    public Flight(int flightNum, String origin, String destination) {
        this.flightNum = flightNum;
        this.origin = origin;
        this.destination = destination;
        this.currentLoad = 0;
        this.reservationList = new ArrayList<>();
    }

    //getters
    public int getFlightNum() {
        return flightNum;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public List<Guest> getReservationList() {
        return reservationList;
    }

    //setters
    public void setCurrentLoad(int x) {
        this.currentLoad = x;
    }

    public void setReservationList(List<Guest> list) {
        this.reservationList = list;
    }


    //EFFECTS: check empty seats remaining on given flight.
    public int checkAvailability() {
        int sum = MAX_CAPACITY - this.currentLoad;
        return Math.max(sum, 0);
    }

    //MODIFIES: this and Guest
    //EFFECTS: makes a flight reservation for the guest for the given flight if there is availability, and add guest
    //         into the corresponding flight's reservationList. Print out a reservation confirmation.
    //         Assign guest the corresponding flight number.
    //         Otherwise, tell customer flight is full.
    public void makeReservation(Guest g) throws FlightFullException {
        if (currentLoad < MAX_CAPACITY && !(this.reservationList.contains(g)) && !g.isGuestReserved()) {
            g.setReservedFlightNum(flightNum);
            this.reservationList.add(g);
            this.currentLoad += 1;
            //System.out.println(g.getName() + " has been reserved on AW" + getFlightNum() + "!");
            //System.out.println("Get ready for your trip to " + getDestination() + "! \n");
            EventLog.getInstance().logEvent(new Event(g.getName() + " has been reserved on AW"
                    + getFlightNum() + "."));
        } else if (currentLoad >= MAX_CAPACITY) {
            throw new FlightFullException();
        }
    }

    //REQUIRES: guest already has an existing flight reservation
    //MODIFIES: this, Guest
    //EFFECTS: removes the guest's existing reservation from the corresponding flight's reservationList.
    //         print out a confirmation of cancellation.
    //         sets the guest's reservedFlightNum to 0
    public void cancelReservation(Guest g) {
        if (reservationList.contains(g)) {
            reservationList.remove(g);
            g.unreserveGuest();
            this.currentLoad -= 1;
            g.setReservedFlightNum(0);
            //System.out.println(g.getName() + "'s reservation for AW" + getFlightNum() + " has been cancelled. \n");
            EventLog.getInstance().logEvent(new Event(g.getName() + "'s reservation for AW" + getFlightNum()
                    + " has been cancelled."));
        }
    }

    //REQUIRES: guest already has an existing flight reservation on current flight
    //MODIFIES: this, Guest
    //EFFECTS: removes the guest's existing reservation from the corresponding flight's reservationList.
    //         changes to a new flight reservation and confirms the change.
    public void changeReservation(Guest g, Flight newFlight) throws FlightFullException {
        this.reservationList.remove(g);
        g.unreserveGuest();
        this.currentLoad -= 1;
        newFlight.makeReservation(g);
        newFlight.reservationList.add(g);
        System.out.println(g.getName() + "'s reservation for AW" + this.getFlightNum() + " has been changed to ");
        System.out.println("AW" + newFlight.getFlightNum());
        System.out.println("from " + newFlight.getOrigin() + " to " + newFlight.getDestination() + ".");
    }

    //EFFECTS: returns true of guest is in the flight's reservation list
    public boolean isGuestOnFlight(Guest g) {
        return (reservationList.contains(g));
    }

    //code here is referenced from JsonSerializationDemo
    //EFFECTS: returns flight as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("flightNum", flightNum);
        json.put("origin", origin);
        json.put("destination", destination);
        json.put("currentLoad", currentLoad);
        json.put("reservationList", reservationList);
        return json;
    }
}
