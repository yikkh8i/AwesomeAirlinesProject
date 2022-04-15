package persistence;

import model.Airline;
import model.Flight;
import model.Guest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//Represents a reader that reads airline from JSON data stored in file
//Code referenced from JsonSerializationDemo project
public class JsonReader {
    private String source;

    //EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads airline from file and returns it
    //throws IOException if an error occurs when reading data from file
    public Airline read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAirline(jsonObject);
    }

    //EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    //EFFECTS: parses airline from JSON object and returns it
    private Airline parseAirline(JSONObject jsonObject) {
        String name = jsonObject.getString("date");
        Airline a = new Airline(name);
        addGuests(a, jsonObject);
        addFlights(a, jsonObject);
        return a;
    }

    //MODIFIES: a
    //EFFECTS: parses flights from JSON object and adds them to airline
    private void addFlights(Airline a, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("flights");
        for (Object json : jsonArray) {
            JSONObject nextFlight = (JSONObject) json;
            addFlight(a, nextFlight);
        }
    }

    //MODIFIES: a
    //EFFECTS: parses guests from JSON object and adds them to airline
    private void addGuests(Airline a, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("guests");
        for (Object json : jsonArray) {
            JSONObject nextGuest = (JSONObject) json;
            addGuest(a, nextGuest);
        }
    }

    //MODIFIES: a
    //EFFECTS: parses guest from JSON object and adds it to airline.
    private void addGuest(Airline a, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int reservedFlightNum = jsonObject.getInt("reservedFlightNum");
        Guest guest = new Guest(name);
        guest.setReservedFlightNum(reservedFlightNum);

        a.addGuest(guest);
    }


    //MODIFIES: a
    //EFFECTS: parses flight from JSON object and adds it to airline.
    private void addFlight(Airline a, JSONObject jsonObject) {
        int flightNum = jsonObject.getInt("flightNum");
        String origin = jsonObject.getString("origin");
        String destination = jsonObject.getString("destination");
        int currentLoad = jsonObject.getInt("currentLoad");
        List<Guest> reservationList = new ArrayList<>();

        Flight flight = new Flight(flightNum, origin, destination);
        flight.setCurrentLoad(currentLoad);
        for (Guest g : a.getSavedGuests()) {
            if (g.getReservedFlightNum() == flightNum) {
                reservationList.add(g);
            }
        }
        flight.setReservationList(reservationList);

        a.addFlight(flight);
    }
}
