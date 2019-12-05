package pt.ua.m.simon;


import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.CharGelem;

import java.awt.*;

// muss er auch actor sein?
public class Person {
    // person that gets spawned in the building and sends messages to the elevator to bring him somewhere
    char name;
    int currentFloor; // state
    int destinationFloor;   // goal state
    Gelem gelem;
    int[] position;

    public Person(int destination, int current, char name) {
        this.destinationFloor = destination;
        this.currentFloor = current;
        this.name = name;
        this.gelem = new CharGelem(name, Color.red);
        this.position = new int[]{6,0};
    }

    public Gelem getGelem() {
        return gelem;
    }

    public int getLine() {
        return position[0];
    }
    public int getColumn() {
        return position[1];
    }

    public void setLine(int line) {
        position[0] = line;
    }
    public void setColumn(int col) {
        position[1] = col;
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
}
