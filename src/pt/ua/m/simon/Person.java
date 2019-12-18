package pt.ua.m.simon;


import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.CharGelem;
import pt.ua.m.simon.view.BuildingPosition;
import pt.ua.m.simon.view.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

// muss er auch actor sein?
// This is the Model
public class Person implements IObservable {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.person");


    // person that gets spawned in the building and sends messages to the elevator to bring him somewhere
    private char name;
    private int destinationFloor;   // goal state
    private Gelem gelem;
    private BuildingPosition position;
    private BuildingPosition prev_position;

    private ArrayList<IObserver> observers;


    Person(int destination, int startLine, int startColumn, char name) {
        this.destinationFloor = destination;
        this.name = name;
        this.gelem = new CharGelem(name, Color.red);
        this.position = new BuildingPosition(startLine,startColumn);
        this.prev_position = new BuildingPosition(startLine,startColumn);
        this.observers = new ArrayList<>();
        this.position.setLayer(2);
    }


    void move(Direction direction)
    {
        int currLine = this.position.getLine();
        prev_position.setLine(currLine);
        int currCol = this.position.getColumn();
        prev_position.setColumn(currCol);

        switch (direction){
            case UP:
                position.setLine(currLine-1); break;
            case DOWN:
                position.setLine(currLine+1); break;
            case LEFT:
                position.setColumn(currCol-1); break;
            case RIGHT:
                position.setColumn(currCol+1); break;
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

    int getCurrentFloor() {
        return position.getFloor();
    }

    int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public Gelem getGelem() {
        return gelem;
    }

    @Override
    public BuildingPosition getPosition() {
        return position;
    }

    @Override
    public BuildingPosition getPrevPosition() {
        return prev_position;
    }

    @Override
    public void registerObserver(IObserver o){
        observers.add(o);
    }


    public void notifyObservers(){
        for (IObserver o : observers)
            o.update(this);
    }

}
