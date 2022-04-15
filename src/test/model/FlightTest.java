package model;

import exceptions.FlightFullException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {
    private Flight f1;
    private Flight f2;
    private Guest g1;
    private Guest g2;

    @BeforeEach
    public void setup() {
        this.f1 = new Flight(110,"YVR","YYC");
        this.f2 = new Flight(111,"YVR", "YYZ");
        this.g1 = new Guest("Adam");
        this.g2 = new Guest("Brenda");
    }

    @Test
    public void testConstructor() {
        assertEquals(110, f1.getFlightNum());
        assertEquals("YVR", f1.getOrigin());
        assertEquals("YYC", f1.getDestination());
        assertEquals(0, f1.getCurrentLoad());
        assertEquals(0, f1.getReservationList().size());
    }

    @Test
    public void testCheckAvailability() {
        assertEquals(Flight.MAX_CAPACITY, f1.checkAvailability());
        assertEquals(Flight.MAX_CAPACITY, f2.checkAvailability());
        f1.setCurrentLoad(Flight.MAX_CAPACITY);
        assertEquals(0, f1.checkAvailability());
    }

    @Test
    public void testMakeReservationSuccess() {
        assertTrue(f2.getCurrentLoad() < Flight.MAX_CAPACITY);
        assertFalse(f2.getReservationList().contains(g1));
        try {
            f2.makeReservation(g1);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        assertEquals(111, g1.getReservedFlightNum());
        assertTrue(g1.isGuestReserved());
        assertEquals((Flight.MAX_CAPACITY - 1), f2.checkAvailability());
        assertTrue(f2.isGuestOnFlight(g1));
    }

    @Test
    public void testMakeResFull() {
        f2.setCurrentLoad(Flight.MAX_CAPACITY);
        try {
            f2.makeReservation(g1);
            fail("Should have caught exception!");
        } catch (FlightFullException e) {
            //all good!
        }
        assertEquals(0, g1.getReservedFlightNum());
        assertFalse(g1.isGuestReserved());
        assertEquals(0, f2.checkAvailability());
        assertFalse(f2.isGuestOnFlight(g1));
    }

    @Test
    public void testMakeResOverloaded() {
        f2.setCurrentLoad(Flight.MAX_CAPACITY + 1);
        try {
            f2.makeReservation(g1);
            fail("Should  have caught exception!");
        } catch (FlightFullException e) {
            //all good
        }
        assertEquals(0, g1.getReservedFlightNum());
        assertFalse(g1.isGuestReserved());
        assertEquals(0, f2.checkAvailability());
        assertFalse(f2.isGuestOnFlight(g1));
    }

    @Test
    public void testMakeResAlreadyOnAnotherFlight() {
        try {
            f1.makeReservation(g1);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        assertTrue(g1.isGuestReserved());
        try {
            f2.makeReservation(g1);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        assertFalse(f2.getReservationList().contains(g1));
        assertFalse(f2.isGuestOnFlight(g1));
        assertEquals(110, g1.getReservedFlightNum());
    }

    @Test
    public void testMakeResAlreadyOnThisFlight() {
        try {
            f1.makeReservation(g1);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        assertTrue(g1.isGuestReserved());
        assertEquals(110, g1.getReservedFlightNum());
        try {
            f1.makeReservation(g1);
        } catch (FlightFullException e) {
            fail("Should have caught exception!");
        }
        f1.getReservationList().remove(g1);
        assertFalse(f1.getReservationList().contains(g1));
    }

    @Test
    public void testCancelSuccess() {
        try {
            f2.makeReservation(g2);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        //check g2 is on 111's reservationList
        assertTrue(f2.isGuestOnFlight(g2));
        assertTrue(g2.isGuestReserved());
        //cancel it
        f2.cancelReservation(g2);
        //check g2 is no longer in 111's reservationList
        assertFalse(f2.isGuestOnFlight(g2));
        //check g2's reservation record
        assertFalse(g2.isGuestReserved());
        //check g2's reservedFlightNum is 0
        assertEquals(0, g2.getReservedFlightNum());
    }

    @Test
    public void testCancelNonExisting() {
        assertFalse(f2.isGuestOnFlight(g2));
        f2.cancelReservation(g2);
        assertFalse(g2.isGuestReserved());
        assertEquals(0, g2.getReservedFlightNum());
    }

    @Test
    public void testChangeReservationSuccess() {
        try {
            f2.makeReservation(g2);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        try {
            f2.changeReservation(g2,f1);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        //check g2 not on 111's list anymore
        assertFalse(f2.isGuestOnFlight(g2));
        //check g2 is on 110's list
        assertTrue(f1.isGuestOnFlight(g2));
        //check g2's reservedFlightNum is 110
        assertEquals(110, g2.getReservedFlightNum());
    }

    @Test
    public void testIsGuestOnFlight() {
        try {
            f1.makeReservation(g1);
        } catch (FlightFullException e) {
            fail("Should not have caught exception!");
        }
        assertTrue(f1.isGuestOnFlight(g1));
        assertFalse(f1.isGuestOnFlight(g2));
    }

    @Test
    public void testSetReservationList() {
        List<Guest> list1 = new ArrayList<>();
        list1.add(g1);
        f1.setReservationList(list1);
        assertEquals(list1, f1.getReservationList());
    }

    @Test
    public void testToJson() {
        JSONObject testJson = f1.toJson();
        assertEquals(f1.getFlightNum(), testJson.get("flightNum"));
        assertEquals(f1.getOrigin(), testJson.get("origin"));
        assertEquals(f1.getDestination(), testJson.get("destination"));
        assertEquals(f1.getCurrentLoad(), testJson.get("currentLoad"));
    }


}
