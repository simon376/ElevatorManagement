package pt.ua.m.simon;

import pt.ua.concurrent.Actor;
import pt.ua.concurrent.Future;

import java.util.logging.Logger;

public class Elevator extends Actor {

    private static Logger logger = Logger.getLogger("pt.ua.m.simon.elevator");

    static final int MIN_FLOOR = -5;
    static final int MAX_FLOOR = 5;
    int currentFloor;

    public Elevator() {
        super();
        this.currentFloor = 0;

        start();    // start Thread
    }

    public int getCurrentFloor() {
        logger.info("floor: " + currentFloor);
        return currentFloor;
    }

    void goToFloor(int floor){
        assert  (floor < MAX_FLOOR && floor > MIN_FLOOR);
        logger.info("starting routine to go from: " + currentFloor + " to " + floor);
        Routine routine = new ElevatorRoutine(floor);   // create message
        inPendingRoutine(routine);  // "send" it to the Elevator-Actor

//        assert  (currentFloor < MAX_FLOOR && currentFloor > MIN_FLOOR);
    }

    Future goToFloorFuture(int floor){
        //TODO: return future
        return new Future(false);
    }


    // Routine is a first class function [method], not a "real" object --> message glaub ich
    class ElevatorRoutine extends Actor.Routine{
        private int floor;
        protected ElevatorRoutine(int floor) {
            super();
            this.floor = floor;
        }

        @Override
        public boolean concurrentPrecondition() {
            return super.concurrentPrecondition();
        }

        @Override
        public void execute() {

            if(floor == currentFloor)
                return;
            int i = (currentFloor < floor) ? 1 : -1;
            while(currentFloor != floor){
                try {
                    sleep(1000);    //TODO bad!
                    currentFloor += i;
                    logger.info("current floor: " + currentFloor);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("finished routine to go to " + floor);
            // set future?
        }
    }
}


