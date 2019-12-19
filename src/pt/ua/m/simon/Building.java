package pt.ua.m.simon;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Future;
import pt.ua.m.simon.view.Direction;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;



// This is the Controller
class Building {
    // manages elevators, floors, etc
    private static final Logger logger = Logger.getLogger("pt.ua.m.simon.building");

    private final Elevator elevator;
    private final LinkedList<Person> people;

    Building(int numPeople, int numFloors) {
        this.elevator = new Elevator(3,2);
        this.people = new LinkedList<>();
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        Random ran = new Random();
        int offset = 1;
        // create people at random positions
        for (int i = 0; i < numPeople; i++) {
            int dest = ran.nextInt(numFloors-offset)+1+offset;
            int startLine = ran.nextInt(numFloors);
            this.people.push(new Person(dest,startLine, 0,alphabet[i%alphabet.length]));
        }

        // setup UI
        LinkedList<IObservable> observables = new LinkedList<>(this.people);
        observables.add(this.elevator);
        ElevatorGBoard ui = new ElevatorGBoard(observables, numFloors);
    }

    /** creates a new Thread for each Person and starts the simulation **/
    synchronized void runSimulationConcurrent(){
        LinkedList<CThread> threads = new LinkedList<>();
        for (int i = 0; i < people.size(); i++) {
            threads.push( new CThread(this::runSimulationConc) );
        }
        threads.forEach(CThread::start);
        threads.forEach(CThread::ajoin);
    }

    /** moves person to elevator, calls elevator to himself and then to destination, leaves at his floor **/
    private void runSimulationConc(){
        if(people.size() <= 0)
            return;
        Person p = people.pop();

        Random ran = new Random();
        int sleepTime = ran.nextInt(200);

        // move to elevator
        while (p.getPosition().getColumn() < elevator.getPosition().getColumn()){
            CThread.pause(sleepTime);
            p.move(Direction.RIGHT);
        }

        // call the elevator to persons floor
        if(p.getCurrentFloor() != elevator.getCurrentFloor()){
            Future f  = elevator.goToFloorFuture(p.getCurrentFloor());
            f.done();
        }

        // call the elevator to destination floor
        Future f = elevator.goToFloorFuture(p);
        f.done();

        // move to end of the floor
        while (p.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() -1){
            CThread.pause(200);
            p.move(Direction.RIGHT);
        }

    }


    void runSimulation(){
        LinkedList<Future> futures = new LinkedList<>();
        for(Person p : people) {

            // while not at elevator
            while (p.getPosition().getColumn() < elevator.getPosition().getColumn()){
                CThread.pause(200);
                p.move(Direction.RIGHT);
            }


            if(p.getCurrentFloor() != elevator.getCurrentFloor()){
                logger.info("calling elevator to go to person at floor " + p.getCurrentFloor());
                futures.push(elevator.goToFloorFuture(p.getCurrentFloor())); // call the elevator to persons floor
            }
            logger.info("calling elevator to go from person at " + p.getCurrentFloor() + " to floor " + p.getDestinationFloor());
            futures.push(elevator.goToFloorFuture(p));

        }

        for (Person p : people){
            futures.pop().done();
            while (p.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() -1){
                CThread.pause(200);
                p.move(Direction.RIGHT);
            }
        }

        elevator.terminate();
    }

}
