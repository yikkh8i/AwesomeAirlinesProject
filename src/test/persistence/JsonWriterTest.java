package persistence;

import exceptions.FlightFullException;
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
public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            Airline a = new Airline("31DEC");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("Should have caught IOException");
        } catch (IOException e) {
            //all good
        }
    }

    @Test
    public void testWriterEmptyAirline() {
        try {
            Airline a = new Airline("31DEC");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyAirline.json");
            writer.open();
            writer.write(a);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAirline.json");
            a = reader.read();
            assertEquals("31DEC", a.getDate());
            assertEquals(0, a.getSavedFlights().size());
            assertEquals(0, a.getSavedGuests().size());
        } catch (IOException e) {
            fail("Caught IOException when we shouldn't.");
        }
    }

    @Test
    public void testWriterGeneralAirline() {
        try {
            Airline a = new Airline("31DEC");
            Flight testFlight1 = new Flight(201, "YVR", "JFK");
            Flight testFlight2 = new Flight(202, "YVR", "LAX");
            Guest testGuest = new Guest("John");
            try {
                testFlight1.makeReservation(testGuest);
            } catch (FlightFullException e) {
                fail("Should not have caught exception!");
            }
            a.addFlight(testFlight1);
            a.addFlight(testFlight2);
            a.addGuest(testGuest);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAirline.json");
            writer.open();
            writer.write(a);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAirline.json");
            a = reader.read();
            assertEquals("31DEC", a.getDate());
            List<Flight> savedFlights = a.getSavedFlights();
            List<Guest> savedGuests = a.getSavedGuests();
            assertEquals(2, savedFlights.size());
            assertEquals(1, savedGuests.size());
            List<Guest> testResList1 = new ArrayList<>();
            List<Guest> testResList2 = new ArrayList<>();
            testResList1.add(testGuest);
            checkGuest("John", 201, true, savedGuests.get(0));
            checkFlight(201,"YVR", "JFK", 1, testResList1, savedFlights.get(0));
            checkFlight(202,"YVR", "LAX", 0, testResList2, savedFlights.get(1));
        } catch (IOException e) {
            fail("Caught IOException when we shouldn't!");
        }
    }

}
