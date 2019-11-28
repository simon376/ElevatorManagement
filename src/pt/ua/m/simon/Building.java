package pt.ua.m.simon;

import java.util.Random;
import java.util.logging.Logger;

public class Building {
    // manages elevators, floors, etc
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.building");

    int noPeople;
    int noFloors;
    Elevator elevator;
    Person[] people;

    public Building(int numPeople, int numFloors) {
        this.noPeople = numPeople;
        this.noFloors = numFloors;
        this.elevator = new Elevator();
        this.people = new Person[noPeople];
        for (int i = 0; i < noPeople; i++) {
//            int dest = Math.random();
            this.people[i] = new Person(3,0,("Person " + i));
        }
    }

    void runSimulation(){
        for (int i = 0; i < noPeople; i++) {
            Person p = people[i];
            while(p.getCurrentFloor() != elevator.getCurrentFloor()){
                logger.info("calling elevator to go to person at floor " + p.getCurrentFloor());
                elevator.goToFloor(p.getCurrentFloor());    // call the elevator to persons floor
                // TODO: wait for future result and check if its good
            }
            logger.info("calling elevator to go from person at " + p.getCurrentFloor() + " to floor " + p.getDestinationFloor());
            elevator.goToFloor(p.getDestinationFloor());
        }

        //elevator.terminate();
        // TODO: wait until elevators are finished working and then terminate

    }
}
