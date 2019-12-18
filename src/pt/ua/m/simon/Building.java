package pt.ua.m.simon;

import pt.ua.concurrent.Future;
import pt.ua.m.simon.view.Direction;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;


// This is the Controller
class Building {
    // manages elevators, floors, etc
    private static final Logger logger = Logger.getLogger("pt.ua.m.simon.building");

    private final Elevator elevator;
    private final LinkedList<Person> people;

    Building(int numPeople, int numFloors) {
        this.elevator = new Elevator(3,2);
        this.people = new LinkedList<>();
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J'};
        Random ran = new Random();
        int offset = 1;
        for (int i = 0; i < numPeople; i++) {

            int dest = ran.nextInt(numFloors-offset)+1+offset;
            this.people.push(new Person(dest,numFloors, 0,alphabet[i]));
        }

        LinkedList<IObservable> observables = new LinkedList<>(this.people);
        observables.add(this.elevator);
        ElevatorGBoard ui = new ElevatorGBoard(observables, numFloors);
    }

    void runSimulation(){
        // TODO: persons arrive concurrently
        LinkedList<Future> futures = new LinkedList();
        for(Person p : people) {

            // while not at elevator
            while (p.getPosition().getColumn() < elevator.getPosition().getColumn()){
                try {
                    sleep(500);    //TODO
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                p.move(Direction.RIGHT);
            }


            if(p.getCurrentFloor() != elevator.getCurrentFloor()){
                logger.info("calling elevator to go to person at floor " + p.getCurrentFloor());
                futures.push(elevator.goToFloorFuture(p.getCurrentFloor())); // call the elevator to persons floor
            }
            logger.info("calling elevator to go from person at " + p.getCurrentFloor() + " to floor " + p.getDestinationFloor());
            // TODO: check future and update UI
            futures.push(elevator.goToFloorFuture(p));

        }

        for (Person p : people){
            futures.pop().done();
            while (p.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() -1){
                try {
                    sleep(500);    //TODO
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                p.move(Direction.RIGHT);
            }
        }

        elevator.terminate();
        // TODO: wait until elevators are finished working and then terminate

    }
}
