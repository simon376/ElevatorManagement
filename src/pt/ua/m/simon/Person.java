package pt.ua.m.simon;


import pt.ua.gboard.GBoard;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.CharGelem;
import pt.ua.m.simon.view.BuildingPosition;

import java.awt.*;
import java.util.ArrayList;

// muss er auch actor sein?
// This is the Model
public class Person implements IObservable {
    // person that gets spawned in the building and sends messages to the elevator to bring him somewhere
    char name;
    int destinationFloor;   // goal state
    Gelem gelem;
    BuildingPosition position;
    BuildingPosition prev_position;

    private ArrayList<IObserver> observers;

    public Person(int destination, int current, char name) {
        this.destinationFloor = destination;
        this.name = name;
        this.gelem = new CharGelem(name, Color.red);
        this.position = new BuildingPosition(6,0);
        this.prev_position = new BuildingPosition(6,0);
        this.observers = new ArrayList<>();
        this.position.setLayer(2);
    }



//    public void setLine(int line) {
//        this.prev_position.setFloor(this.position.getFloor());
//        position.setFloor(line);
////        this.prev_position.setLine(this.position.getLine());
////        position.setLine(line);
//    }
    public void setColumn(int col) {
        this.prev_position.setColumn(this.position.getColumn());
        position.setColumn(col);
    }

    public void moveRight()
    {
        this.setColumn(this.getColumn()+1);
        notifyObservers();
    }

    public void moveUp()
    {
//        this.setLine(this.getLine()-1);
        this.position.setFloor(this.position.getFloor()-1);
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

    public int getCurrentFloor() {
        return position.getFloor();
    }

    public int getDestinationFloor() {
        return destinationFloor;
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

    @Override
    public void registerObserver(IObserver o){
        observers.add(o);
    }


    public void notifyObservers(){
        for (IObserver o : observers)
            o.update(this, prev_position);
    }

}
