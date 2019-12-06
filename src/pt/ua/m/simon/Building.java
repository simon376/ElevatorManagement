package pt.ua.m.simon;

import pt.ua.concurrent.Future;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.util.LinkedList;
import java.util.logging.Logger;


// This is the Controller
public class Building {
    // manages elevators, floors, etc
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.building");

    int noPeople;
    int noFloors;
    Elevator elevator;
    LinkedList<IObservable> people;

    private ElevatorGBoard ui;

    public Building(int numPeople, int numFloors) {
        this.noPeople = numPeople;
        this.noFloors = numFloors;
        this.elevator = new Elevator();
        this.people = new LinkedList<>();
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J'};
        for (int i = 0; i < noPeople; i++) {
//            int dest = Math.random();
            this.people.push(new Person(3,0,alphabet[i]));
        }

        ui = new ElevatorGBoard(this.people);
    }

    void runSimulation(){
        for (int i = 0; i < noPeople; i++) {
            Person p = (Person) people.pop();

            // while not at elevator
            p.moveRight();
            ui.moveToElevator(p);

            if(p.getCurrentFloor() != elevator.getCurrentFloor()){
                logger.info("calling elevator to go to person at floor " + p.getCurrentFloor());
//                Future<Integer> f = elevator.goToFloor(p.getCurrentFloor());    // call the elevator to persons floor
                Future f = elevator.goToFloorFuture(p.getCurrentFloor());
                f.done();
            }
//            while(p.getCurrentFloor() != elevator.getCurrentFloor()){
//                logger.info("calling elevator to go to person at floor " + p.getCurrentFloor());
//                elevator.goToFloor(p.getCurrentFloor());    // call the elevator to persons floor
//                // TODO: wait for future result and check if its good
//                //  - why is the elevator called thousands of times? should be once and then waiting
//            }
            logger.info("calling elevator to go from person at " + p.getCurrentFloor() + " to floor " + p.getDestinationFloor());
            int numFloors = p.getDestinationFloor() - p.getCurrentFloor();
            elevator.goToFloor(p.getDestinationFloor());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: check future and update UI
            for (int j = 0; j < numFloors; j++) {
                ui.moveUp(p);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ui.moveToEnd(p);
        }

        //elevator.terminate();
        // TODO: wait until elevators are finished working and then terminate

    }
}
