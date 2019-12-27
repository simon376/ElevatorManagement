package pt.ua.m.simon;

import pt.ua.concurrent.Actor;
import pt.ua.concurrent.CRunnable;
import pt.ua.concurrent.CThread;
import pt.ua.m.simon.view.BuildingPosition;
import pt.ua.m.simon.view.Direction;
import pt.ua.m.simon.view.ElevatorGBoard;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.logging.Logger;



// This is the Controller
class Building {
    // manages elevators, floors, etc
    private static final Logger logger = Logger.getLogger("pt.ua.m.simon.building");

    private final LinkedList<Elevator> elevators;
    private final LinkedList<Person> people;

    private int noFloors;

    Building(int numPeople, int numFloors) {
        this.elevators = new LinkedList<>();
        this.elevators.push(new Elevator(3,2));
        this.elevators.push(new Elevator(0,5));
        this.people = new LinkedList<>();
        this.noFloors = numFloors;

        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        Random ran = new Random();
        int offset = 1;
        // create people at random positions
        for (int i = 0; i < numPeople; i++) {
            int dest = ran.nextInt(this.noFloors);
            int startLine = ran.nextInt(this.noFloors);
            this.people.push(new Person(this, dest,startLine, 0,alphabet[i%alphabet.length]));
        }

        // setup UI
        LinkedList<IObservable> observables = new LinkedList<>(this.people);
        observables.addAll(this.elevators);
        ElevatorGBoard ui = new ElevatorGBoard(observables, numFloors);
    }

    synchronized int getNoFloors() {
        return noFloors;
    }

    /** creates a new Thread for each Person and starts the simulation **/
    synchronized void runSimulationConcurrent(){
        LinkedList<CThread> threads = new LinkedList<>();
        int noPeople = people.size();
        for (int i = 0; i < noPeople; i++) {
            /* moves person to elevator, calls elevator to himself and then to destination, leaves at his floor */
            threads.push(new CThread(new CRunnable() {
                @Override
                public void arun() {
                    Person p = people.pop();
                    p.startMovementAgenda();
                }
            }, ("Person-"+i)));
        }
        threads.forEach(CThread::start);
        //threads.forEach(CThread::ajoin);

    }

    Elevator getClosestElevator(BuildingPosition position){
        int personCol = position.getColumn();
        int minDist = Integer.MAX_VALUE;
        Elevator closestElevator = null;
        for(Elevator elevator : elevators){
            int elevCol = elevator.getPosition().getColumn();
            int distance = Math.abs(personCol - elevCol);
            if (distance < minDist){
                minDist = distance;
                closestElevator = elevator;
            }
        }

        assert (closestElevator != null);
        return closestElevator;
    }

}
