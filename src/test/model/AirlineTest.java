package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AirlineTest {
    private Airline a;
    private Flight f1;
    private Flight f2;
    private Guest g1;
    private Guest g2;

    @BeforeEach
    public void setup() {
        this.a = new Airline("31DEC");
        this.f1 = new Flight(201, "YVR", "JFK");
        this.f2 = new Flight(202, "YVR", "LAX");
        this.g1 = new Guest("John");
        this.g2 = new Guest("Amy");
    }

    @Test
    public void testConstructor() {
        assertEquals("31DEC", a.getDate());
        assertEquals(0, a.getSavedGuests().size());
        assertEquals(0, a.getSavedFlights().size());
    }

    @Test
    public void testAddFlight() {
        a.addFlight(f1);
        a.addFlight(f2);
        assertEquals(f1, a.getSavedFlights().get(0));
        assertEquals(f2, a.getSavedFlights().get(1));
    }

    @Test
    public void testAddGuest() {
        a.addGuest(g1);
        a.addGuest(g2);
        assertEquals(g1, a.getSavedGuests().get(0));
        assertEquals(g2, a.getSavedGuests().get(1));
    }



}
