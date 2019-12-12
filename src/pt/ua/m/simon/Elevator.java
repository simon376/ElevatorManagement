package pt.ua.m.simon;

import pt.ua.concurrent.Actor;
import pt.ua.concurrent.Future;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.StripedGelem;
import pt.ua.m.simon.view.BuildingPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Elevator extends Actor implements IObservable{

    private static Logger logger = Logger.getLogger("pt.ua.m.simon.elevator");

    static final int MIN_FLOOR = -5;
    static final int MAX_FLOOR = 5;
    Gelem gelem;
    BuildingPosition position;
    BuildingPosition prev_position;
    private ArrayList<IObserver> observers;

    public Elevator(int l, int c) {
        super();
        this.position =  new BuildingPosition(l,c);    // TODO
        this.prev_position =  new BuildingPosition(l,c);    // TODO
        this.observers = new ArrayList<>();
        this.gelem = new StripedGelem(Color.blue,4,1.0,1.0,false);
        this.position.setLayer(1);
        start();    // start Thread
    }

    public int getCurrentFloor() {
        logger.info("floor: " + position.getFloor());
        return position.getFloor();
    }

    // returns current floor
    Integer goToFloor(int floor){
        assert  (floor < MAX_FLOOR && floor > MIN_FLOOR);
        logger.info("starting routine to go from: " + position.getFloor() + " to " + floor);

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
            o.update(this, prev_position);
    }

    @Override
    public Gelem getGelem() {
        return gelem;
    }

    @Override
    public int getLine() {
        return position.getLine();
    }

    @Override
    public int getColumn() {
        return position.getColumn();
    }

    @Override
    public int getLayer() {
        return position.getLayer();
    }

    public void move(boolean down)
    {
        this.prev_position.setFloor(this.position.getFloor());
//        this.prev_position.setLine(this.position.getLine());
        int i = down ? 1: -1;
        position.setFloor(this.position.getFloor() + i);
//        position.setLine(this.getLine() + i);
        notifyObservers();
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

            if(floor == position.getFloor())
                return;
            boolean down = position.getFloor() > floor;
            while(position.getFloor() != floor){
                try {
                    sleep(1000);    //TODO
                    move(down);

                    logger.info("current floor: " + position.getFloor());
                    future.setResult(position.getFloor());

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


