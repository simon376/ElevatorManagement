package pt.ua.m.simon;


import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.CharGelem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observer;

// muss er auch actor sein?
// This is the Model
public class Person implements IObservable {
    // person that gets spawned in the building and sends messages to the elevator to bring him somewhere
    char name;
    int currentFloor; // state
    int destinationFloor;   // goal state
    Gelem gelem;
    int[] position;
    int[] prev_position;

    private ArrayList<IObserver> observers;

    public Person(int destination, int current, char name) {
        this.destinationFloor = destination;
        this.currentFloor = current;
        this.name = name;
        this.gelem = new CharGelem(name, Color.red);
        this.position = this.prev_position = new int[]{6,0};

        this.observers = new ArrayList<>();
    }



    public void setLine(int line) {
        position[0] = line;
    }
    public void setColumn(int col) {
        position[1] = col;
    }

    public void moveRight()
    {
        this.prev_position = this.position;
        this.setColumn(this.getColumn()+1);
        notifyObservers();
    }

    public void moveUp()
    {
        this.prev_position = this.position;
        this.setLine(this.getLine()-1);
        ++this.currentFloor;
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", currentFloor=" + currentFloor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }

    public int getCurrentFloor() {
        return currentFloor;
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
        return position[0];
    }
    @Override
    public int getColumn() {
        return position[1];
    }

    @Override
    public void registerObserver(IObserver o){
        observers.add(o);
    }


    public void notifyObservers(){
        for (IObserver o : observers)
            o.update(this, prev_position[0], prev_position[1]);
    }

}
