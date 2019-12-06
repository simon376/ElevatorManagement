package pt.ua.m.simon;

import pt.ua.concurrent.Actor;
import pt.ua.concurrent.Future;
import pt.ua.gboard.Gelem;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Elevator extends Actor implements IObservable{

    private static Logger logger = Logger.getLogger("pt.ua.m.simon.elevator");

    static final int MIN_FLOOR = -5;
    static final int MAX_FLOOR = 5;
    int currentFloor;
    Gelem gelem;
    int[] position;
    int[] prev_position;
    private ArrayList<IObserver> observers;

    public Elevator(int l, int c) {
        super();
        this.currentFloor = 0;
        this.position = this.prev_position = new int[]{l,c};    // TODO
        this.observers = new ArrayList<>();

        start();    // start Thread
    }

    public int getCurrentFloor() {
        logger.info("floor: " + currentFloor);
        return currentFloor;
    }

    // returns current floor
    Integer goToFloor(int floor){
        assert  (floor < MAX_FLOOR && floor > MIN_FLOOR);
        logger.info("starting routine to go from: " + currentFloor + " to " + floor);

        Future<Integer> future = goToFloorFuture(floor);
        return future.result();
//        assert  (currentFloor < MAX_FLOOR && currentFloor > MIN_FLOOR);
    }

    Future<Integer> goToFloorFuture(int floor){
        final Future<Integer> result = new Future<Integer>(true);
        Routine routine = new ElevatorRoutine(floor, result);   // create message
        inPendingRoutine(routine);  // "send" it to the Elevator-Actor
        return result;
    }

    @Override
    public void registerObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (IObserver o : observers)
            o.update(this, prev_position[0], prev_position[1]);
    }

    @Override
    public Gelem getGelem() {
        return gelem;
    }

    @Override
    public int getLine() {
        return position[0];
    }

    @Override
    public int getColumn() {
        return position[1];
    }


    // Routine is a first class function [method], not a "real" object --> message glaub ich
    class ElevatorRoutine extends Actor.Routine{
        private final int floor;
        private final Future<Integer> future;

        ElevatorRoutine(int floor, Future<Integer> result) {
            super();
            this.floor = floor;
            this.future = result;
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
                    prev_position[1] = position[1];
                    position[1] += i;
                    logger.info("current floor: " + currentFloor);
                    future.setResult(currentFloor);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("finished routine to go to " + floor);
            // set future?
            futureDone(future);
        }
    }
}


