package pt.ua.m.simon;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Future;
import pt.ua.m.simon.view.Direction;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
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
            int startLine = ran.nextInt(numFloors);
            this.people.push(new Person(dest,startLine, 0,alphabet[i]));
        }

        LinkedList<IObservable> observables = new LinkedList<>(this.people);
        observables.add(this.elevator);
        ElevatorGBoard ui = new ElevatorGBoard(observables, numFloors);
    }


    synchronized void runSimulationConcurrent(){
        LinkedList<CThread> threads = new LinkedList<>();
        for (int i = 0; i < people.size(); i++) {
            threads.push( new CThread(this::runSimulationConc) );
        }
        threads.forEach(CThread::start);
        threads.forEach(CThread::ajoin);
    }

    private void runSimulationConc(){
        Person p = people.pop();
            // while not at elevator
        Random ran = new Random();
        int sleepTime = ran.nextInt(200);

        while (p.getPosition().getColumn() < elevator.getPosition().getColumn()){
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            p.move(Direction.RIGHT);
        }


        if(p.getCurrentFloor() != elevator.getCurrentFloor()){
            Future f  = elevator.goToFloorFuture(p.getCurrentFloor()); // call the elevator to persons floor
            f.done();
        }
        Future f = elevator.goToFloorFuture(p);



        f.done();
        while (p.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() -1){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            p.move(Direction.RIGHT);
        }

    }


    void runSimulation(){
        LinkedList<Future> futures = new LinkedList<>();
        for(Person p : people) {

            // while not at elevator
            while (p.getPosition().getColumn() < elevator.getPosition().getColumn()){
                try {
                    sleep(200);
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
            futures.push(elevator.goToFloorFuture(p));

        }

        for (Person p : people){
            futures.pop().done();
            while (p.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() -1){
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                p.move(Direction.RIGHT);
            }
        }

        elevator.terminate();
    }

}
