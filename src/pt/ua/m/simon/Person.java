package pt.ua.m.simon;


import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Future;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.CharGelem;
import pt.ua.m.simon.view.BuildingPosition;
import pt.ua.m.simon.view.Direction;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

// This is the Model
public class Person implements IObservable {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.person");


    // person that gets spawned in the building and sends messages to the elevator to bring him somewhere
    private char name;
    private int destinationFloor;   // goal state
    private Gelem gelem;
    private BuildingPosition position;
    private BuildingPosition prev_position;
    private Building building;

    private ArrayList<IObserver> observers;

    private int MAX_FLOOR = Integer.MAX_VALUE;

    /** create new Person, provide destination floor, start line & column and a character as a name **/
    Person(Building building, int destination, int startLine, int startColumn, char name) {
        this.building = building;
        this.MAX_FLOOR = this.building.getNoFloors();
        this.destinationFloor = destination;
        this.name = name;
        this.gelem = new CharGelem(name, Color.red);
        this.position = new BuildingPosition(startLine,startColumn);
        this.prev_position = new BuildingPosition(startLine,startColumn);
        this.observers = new ArrayList<>();
        this.position.setLayer(2);
    }

    void startMovementAgenda(){
        /*
        repeat a random number of times
            find closest elevator
            move to random floor
            go left or right
            wait random time
         */
        Random ran = new Random();
        int walkTime = ran.nextInt(100)+100;
        int sleepTime = ran.nextInt(900)+100;
        boolean moveRight = true;
        int repetitions = ran.nextInt(15)+5;

        for (int i = 0; i < repetitions; i++) {
            logger.info(this.toString() + " starting rep #" + i + " of " + repetitions);
            int destFloor = ran.nextInt(MAX_FLOOR);
            this.setDestinationFloor(destFloor);
            Elevator elevator = building.getClosestElevator(this.position);

            // move to elevator
            while (this.getPosition().getColumn() < elevator.getPosition().getColumn()){
                CThread.pause(walkTime);
                this.move(Direction.RIGHT);
            }
            while (this.getPosition().getColumn() > elevator.getPosition().getColumn()){
                CThread.pause(walkTime);
                this.move(Direction.LEFT);
            }

            // call the elevator to persons floor
            if(this.getCurrentFloor() != elevator.getCurrentFloor()){
                Future f  = elevator.goToFloorFuture(this.getCurrentFloor());
                f.done();
            }

            // call the elevator to destination floor
            Future f = elevator.goToFloorFuture(this);

            f.done();

            // move to end of the floor
            if(moveRight) {
                while (this.getPosition().getColumn() < ElevatorGBoard.getBoard().numberOfColumns() - 1) {
                    CThread.pause(walkTime);
                    this.move(Direction.RIGHT);
                }
            }
            else{
                while (this.getPosition().getColumn() > 0) {
                    CThread.pause(walkTime);
                    this.move(Direction.LEFT);
                }
            }
            moveRight = !moveRight;

            CThread.pause(sleepTime);
        }
    }

    /** move Person one step (line or column) in given Direction **/
    synchronized void move(Direction direction)
    {
        int currLine = this.position.getLine();
        prev_position.setLine(currLine);
        int currCol = this.position.getColumn();
        prev_position.setColumn(currCol);

        int lineLimit = ElevatorGBoard.getBoard().numberOfLines() -1;
        int colLimit = ElevatorGBoard.getBoard().numberOfColumns() -1;
        switch (direction){
            case UP:
                if(currLine > 0)
                    position.setLine(currLine-1);
                break;
            case DOWN:
                if(currLine < lineLimit)
                    position.setLine(currLine+1);
                break;
            case LEFT:
                if(currCol > 0)
                    position.setColumn(currCol-1);
                break;
            case RIGHT:
                if(currCol < colLimit)
                    position.setColumn(currCol+1);
                break;
        }

        notifyObservers();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", currentFloor=" + getCurrentFloor() +
                ", destinationFloor=" + destinationFloor +
                '}';
    }

    synchronized int getCurrentFloor() {
        return position.getFloor();
    }

    synchronized void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    synchronized int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public synchronized Gelem getGelem() {
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

    @Override
    public synchronized void registerObserver(IObserver o){
        observers.add(o);
    }


    public synchronized void notifyObservers(){
        for (IObserver o : observers)
            o.update(this);
    }

}
