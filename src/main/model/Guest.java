package model;

import org.json.JSONObject;
import persistence.Writable;

//Guest represents a guest that has a potential or existing flight reservation with the airline.
public class Guest implements Writable {

    private String name;
    private int reservedFlightNum;
    private Boolean reserved;

    //REQUIRES:
    //EFFECTS: constructs a Guest with given name, and bookedFlightNum of 0.
    public Guest(String name) {
        this.name = name;
        this.reservedFlightNum = 0;
        this.reserved = false;
    }

    //getters
    public String getName() {
        return name;
    }

    public int getReservedFlightNum() {
        return reservedFlightNum;
    }

    //setters
    public void setReservedFlightNum(int flightNum) {
        this.reservedFlightNum = flightNum;
        this.reserved = flightNum != 0;
    }

    //EFFECTS: return true if guest has existing reservation, false otherwise
    public boolean isGuestReserved() {
        return this.reserved;
    }

    //EFFECTS: sets guest's reserved status to false
    public void unreserveGuest() {
        this.reserved = false;
    }

    //code here is referenced from JsonSerializationDemo
    //EFFECTS: returns flight as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("reservedFlightNum", reservedFlightNum);
        json.put("reserved", reserved);
        return json;
    }
}
