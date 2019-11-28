package pt.ua.m.simon;


// muss er auch actor sein?
public class Person {
    // person that gets spawned in the building and sends messages to the elevator to bring him somewhere
    String name;
    int currentFloor; // state
    int destinationFloor;   // goal state

    public Person(int destination, int current, String name) {
        this.destinationFloor = destination;
        this.currentFloor = current;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", currentFloor=" + currentFloor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
}
