package pt.ua.m.simon;

import java.util.logging.Logger;

class Main {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.main");

    public static void main(String[] args) {
        int noPeople = 5; int noFloors = 8;
        if(args.length == 1)
            noPeople = Integer.parseInt(args[0]);
        if(args.length == 2)
            noFloors = Integer.parseInt(args[1]);
        Building building = new Building(noPeople,noFloors);
        building.runSimulationConcurrent();
//        building.runSimulation();

        logger.info("finished execution of main.");
    }
}
