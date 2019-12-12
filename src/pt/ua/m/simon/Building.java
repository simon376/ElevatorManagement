package pt.ua.m.simon;

import pt.ua.concurrent.Future;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;


// This is the Controller
public class Building {
    // manages elevators, floors, etc
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.building");

    Elevator elevator;
    LinkedList<Person> people;
    LinkedList<IObservable> observables;

    private ElevatorGBoard ui;

    public Building(int numPeople, int numFloors) {
        this.elevator = new Elevator(3,2);
        this.people = new LinkedList<>();
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J'};
        Random ran = new Random();
        int offset = 1;
        for (int i = 0; i < numPeople; i++) {

            int dest = ran.nextInt(numFloors-offset)+1+offset;
            this.people.push(new Person(dest,numFloors, 0,alphabet[i]));
        }
        observables = new LinkedList<>();
        observables.addAll(this.people);
        observables.add(this.elevator);
        ui = new ElevatorGBoard(observables, numFloors);
    }

    void runSimulation(){
        // TODO: persons arrive concurrently

        for(Person p : people) {

            // while not at elevator
            p.moveRight();

            if(p.getCurrentFloor() != elevator.getCurrentFloor()){
                logger.info("calling elevator to go to person at floor " + p.getCurrentFloor());
                Future f = elevator.goToFloorFuture(p.getCurrentFloor());   // call the elevator to persons floor
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


//            elevator.goToFloor(p.getDestinationFloor());

            // TODO: check future and update UI
            Future<Integer> f = elevator.goToFloorFuture(p);
//
//            for (int j = 0; j < numFloors; j++) {
//                p.moveUp(); // TODO bad: i'm manually moving the user here
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            f.done();


            while (p.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() -1){
                p.moveRight();
            }
        }

        //elevator.terminate();
        // TODO: wait until elevators are finished working and then terminate

    }
}
