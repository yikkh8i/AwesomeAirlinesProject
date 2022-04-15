package persistence;

import model.Airline;
import model.Flight;
import model.Guest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Code referenced from JsonSerializationDemo project
public class JsonReaderTest extends JsonTest{

    @Test
    public void testReaderFileNotExist() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Airline a = reader.read();
            fail("Should have caught IOException!");
        } catch (IOException e) {
            //all good
        }
    }

    @Test
    public void testReaderEmptyAirline() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAirline.json");
        try {
            Airline a = reader.read();
            assertEquals("31DEC", a.getDate());
            assertEquals(0, a.getSavedFlights().size());
            assertEquals(0, a.getSavedGuests().size());
        } catch (IOException e) {
            fail("Couldn't read from file.");
        }
    }

    @Test
    public void testReaderGeneralAirline() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAirline.json");
        try {
            Airline a = reader.read();
            assertEquals("31DEC", a.getDate());

            List<Flight> savedFlights = a.getSavedFlights();
            List<Guest> savedGuests = a.getSavedGuests();
            assertEquals(2, savedFlights.size());
            assertEquals(1, savedGuests.size());
            List<Guest> testResList1 = new ArrayList<>();
            List<Guest> testResList2 = new ArrayList<>();
            checkGuest("John", 201, true, savedGuests.get(0));
            checkFlight(201,"YVR", "JFK", 1, testResList1, savedFlights.get(0));
            checkFlight(202,"YVR", "LAX", 0, testResList2, savedFlights.get(1));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

        }

