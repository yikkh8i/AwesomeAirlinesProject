package model;

import model.Guest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {

    private Guest g1;

    @BeforeEach
    public void setup() {
        this.g1 = new Guest("Adam");
    }

    @Test
    public void testConstructor() {
        assertEquals("Adam", g1.getName());
        assertEquals(0, g1.getReservedFlightNum());
    }

    @Test
    public void testIsGuestReserved() {
        assertFalse(g1.isGuestReserved());
    }

    @Test
    public void testToJson() {
        JSONObject testJson = g1.toJson();
        assertEquals(g1.getName(), testJson.get("name"));
        assertFalse((Boolean) testJson.get("reserved"));
        assertEquals(g1.getReservedFlightNum(), testJson.get("reservedFlightNum"));
    }

}
