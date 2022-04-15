package persistence;

import model.Guest;
import model.Flight;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Code referenced from JsonSerializationDemo project
public class JsonTest {
    protected void checkFlight(int flightNum, String origin, String destination, int currentLoad,
                               List<Guest> reservationList, Flight f){
        assertEquals(flightNum, f.getFlightNum());
        assertEquals(origin, f.getOrigin());
        assertEquals(destination, f.getDestination());
        assertEquals(currentLoad, f.getCurrentLoad());
        assertEquals(currentLoad, f.getReservationList().size());
        for (Guest g : reservationList) {
            for (int i = 0; i < reservationList.size(); i++) {
                assertEquals(reservationList.get(i).getName(), f.getReservationList().get(i).getName());
                assertEquals(reservationList.get(i).getReservedFlightNum(), f.getFlightNum());
            }
        }
    }

    protected void checkGuest(String name, int reservedFlightNum, Boolean reserved, Guest g) {
        assertEquals(name, g.getName());
        assertEquals(reservedFlightNum, g.getReservedFlightNum());
        assertEquals(reserved, g.isGuestReserved());
    }
}
