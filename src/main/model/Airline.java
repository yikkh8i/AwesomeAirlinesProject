package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

//An airline that contains a collection of flights and their corresponding dates
//Code referenced from JsonSerializationDemo project
public class Airline implements Writable {
    private String date;
    protected List<Flight> savedFlights;
    protected List<Guest> savedGuests;

    //EFFECTS: constructs an airline with a date and empty list of flights
    public Airline(String date) {
        this.date = date;
        this.savedFlights = new ArrayList<>();
        this.savedGuests = new ArrayList<>();
    }

    //getter
    public String getDate() {
        return date;
    }

    //MODIFIES: this
    //EFFECTS: adds flight to this airline
    public void addFlight(Flight flight) {
        savedFlights.add(flight);
    }

    //MODIFIES: this
    //EFFECTS: adds guest to this airline
    public void addGuest(Guest guest) {
        savedGuests.add(guest);
    }

    //EFFECTS: returns an unmodifiable list of flights in this airline
    public List<Flight> getSavedFlights() {
        return Collections.unmodifiableList(savedFlights);
    }

    //EFFECTS: returns an unmodifiable list of flights in this airline
    public List<Guest> getSavedGuests() {
        return Collections.unmodifiableList(savedGuests);
    }

    @Override
    //EFFECTS: returns airline as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date", date);
        json.put("flights", flightsToJson());
        json.put("guests", guestsToJson());
        return json;
    }

    //EFFECTS: returns flights in this airline as a JSON array
    private JSONArray flightsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Flight f : savedFlights) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }

    //EFFECTS: returns guests in each flight as a JSON array
    private JSONArray guestsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Flight f : savedFlights) {
            for (Guest g : f.getReservationList()) {
                jsonArray.put(g.toJson());
            }
        }
        return jsonArray;
    }
}

