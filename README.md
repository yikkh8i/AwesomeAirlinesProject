# My Personal Project

## Awesome Airlines Reservation Application




This is an application to make flight seat reservations for Awesome Airlines. In general, it would allow users to input 
their names as a guest, select their preferred flight and reserve their seat for the flight. Users should also be able
to see the availabilities of other flights, cancel or change their bookings if desired.
The application's target audience would be regular flight customers. 

I have a strong interest in airlines, air travel and the aviation industry in general, not just the consumer end, 
also the operations end. I find actual air travel systems complicated yet fascinating, simply due to the
sheer scale of information and accuracy required.

##User Stories
- As a user, I want to be able to view the seat availability for all flights.
- As a user, I want to be able to create a flight ticket reservation and have it added to the flight's list of reservations.
- As a user, I want to be able to change an existing reservation to another flight.
- As a user, I want to be able to cancel an existing reservation.
- As a user, I want my reservations to be saved in the application even after I quit it.
- As a user, I want to load my existing reservations every time I run the application.
- As a user, I want to be able to print the passenger manifest for a given flight.



*Potential user stories for future* :
- As a user, I want to be able to select my dietary preferences/requirements when booking my flight.
- Each flight has varying maximum capacities.

##Phase 4: Task 2
Sample AW Event Log:

Wed Mar 30 00:42:56 PDT 2022
John has been reserved on AW101.

Wed Mar 30 00:43:00 PDT 2022
Andy has been reserved on AW101.

Wed Mar 30 00:43:06 PDT 2022
Maggie has been reserved on AW104.

Wed Mar 30 00:43:10 PDT 2022
John's reservation for AW101 has been cancelled.


##Phase 4: Task 3

- To reduce coupling, I would make FlightsGUI an observer that observes ReservationAppGUI, such that any time a 
reservation or cancellation is made, FlightsGUI can be notified and update the information displayed. Then, the 
association between ReservationAppGUI and FlightsGUI can be removed. Apart from this, the associations between the 
four GUI classes I have, I believe they are as simple as they can be, considering I intended to create three separate 
JPanels on one JFrame.
- I think ReservationAppGUI's association with Guest and Flight classes can be removed through some refactoring with 
Airline class. In hindsight, ReservationAppGUI should be able to access Flight and Guest through its association with 
Airline.
