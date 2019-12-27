package pt.ua.m.simon;

import pt.ua.concurrent.Actor;
import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Future;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.StripedGelem;
import pt.ua.m.simon.view.BuildingPosition;
import pt.ua.m.simon.view.Direction;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Elevator extends Actor implements IObservable{

    private static Logger logger = Logger.getLogger("pt.ua.m.simon.elevator");

    private static final int MIN_FLOOR = -5;
    private static final int MAX_FLOOR = 5;
    private Gelem gelem;
    private BuildingPosition position;
    private BuildingPosition prev_position;
    private ArrayList<IObserver> observers; // UI

    private ArrayList<Person> occupants;


    Elevator(int l, int c) {
        super();
        this.position =  new BuildingPosition(l,c);
        this.prev_position =  new BuildingPosition(l,c);
        this.observers = new ArrayList<>();
        this.gelem = new StripedGelem(Color.blue,4,1.0,1.0,false);
        this.position.setLayer(1);
        this.occupants = new ArrayList<>();
        start();    // start Thread
    }

    synchronized int getCurrentFloor() {
        return position.getFloor();
    }

    // returns current floor
    Integer goToFloor(int floor){
        assert  (floor < MAX_FLOOR && floor > MIN_FLOOR);
        logger.info(this.getName() + ": starting routine to go from: " + position.getFloor() + " to " + floor);

        Future<Integer> future = goToFloorFuture(floor);
        return future.result();
//        assert  (currentFloor < MAX_FLOOR && currentFloor > MIN_FLOOR);
    }

    /** add a new Routine (Message) to queue, to move elevator to given destination floor **/
    Future<Integer> goToFloorFuture(int floor){
        final Future<Integer> result = new Future<>(true);
        Routine routine = new EmptyElevatorRoutine(floor, result);   // create message
        logger.info(this.getName() + ": received call to floor " + floor);
        inPendingRoutine(routine);  // "send" it to the Elevator-Actor
        return result;
    }

    /** add a new Routine (Message) to queue, to move elevator to person's destination floor **/
    Future<Integer> goToFloorFuture(Person person){
        final Future<Integer> result = new Future<>(true);
        Routine routine = new ElevatorRoutine(person, result);   // create message
        logger.info(this.getName() + ": received call " + person.toString());
        inPendingRoutine(routine);  // "send" it to the Elevator-Actor
        return result;
    }

    private void logInfo(){
        logger.info(this.getName() + ": pending messages: " + this.pending.size() + " | occupants left: " + occupants.size());
    }

    @Override
    public void registerObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (IObserver o : observers)
            o.update(this);
    }

    @Override
    public Gelem getGelem() {
        return gelem;
    }

    @Override
    public synchronized BuildingPosition getPosition() {
        return position;
    }

    @Override
    public synchronized BuildingPosition getPrevPosition() {
        return prev_position;
    }

    /** move the elevator and all its occupants in given Direction**/
    private synchronized void move(Direction direction)
    {
        int currLine = this.position.getLine();
        prev_position.setLine(currLine);
        int currCol = this.position.getColumn();
        prev_position.setColumn(currCol);

        switch (direction){
            case UP:
                if(position.getLine() > 0){
                    position.setLine(currLine-1);
                    for (Person p : occupants)
                        p.move(Direction.UP);
                }
                break;
            case DOWN:
                if(position.getLine() < ElevatorGBoard.getBoard().numberOfLines() -1){
                    position.setLine(currLine+1);
                    for (Person p : occupants)
                        p.move(Direction.DOWN);
                }
                break;
        }

        notifyObservers();
    }


    class EmptyElevatorRoutine extends Actor.Routine{
        private final int destination;
        private final Future<Integer> future;

        EmptyElevatorRoutine(int floor, Future<Integer> result) {
            super();
            this.destination = floor;
            this.future = result;
        }

        @Override
        public boolean concurrentPrecondition() {
            return (this.destination <= ElevatorGBoard.getBoard().numberOfLines() && this.destination > 0);
        }

        @Override
        public void execute() {
            boolean down = position.getFloor() > destination;
            while(position.getFloor() != destination){
                CThread.pause(200);

                if(down)
                    move(Direction.DOWN);
                else
                    move(Direction.UP);

                future.setResult(position.getFloor());
            }

            logger.info(this.toString() + ": finished routine to go to " + destination);
            logInfo();
            futureDone(future);
        }
    }

    class ElevatorRoutine extends Actor.Routine{
        private final Person person;
        private final Future<Integer> future;
        private final int destination;

        ElevatorRoutine(Person person, Future<Integer> result) {
            super();
            this.person = person;
            this.destination = person.getDestinationFloor();
            occupants.add(person);
            this.future = result;
        }

        @Override
        public boolean concurrentPrecondition() {
            return (this.destination <= ElevatorGBoard.getBoard().numberOfLines() && this.destination > 0);
        }

        @Override
        public void execute() {
            logger.info(this.toString() + ": started routine: " + person.toString());

            boolean down = position.getFloor() > destination;
            while(position.getFloor() != destination){
                CThread.pause(200);
                if(down)
                    move(Direction.DOWN);
                else
                    move(Direction.UP);

                future.setResult(position.getFloor());

            }
            logger.info(this.toString() + ": finished routine: " + person.toString());
            occupants.remove(person);
            logInfo();
            futureDone(future);
        }
    }
}


