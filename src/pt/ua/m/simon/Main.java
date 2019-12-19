package pt.ua.m.simon;

import java.util.logging.Logger;

class Main {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.main");

    public static void main(String[] args) {
        int noPeople = 5; int noFloors = 8;
        if(args.length == 1){
            int p = Integer.parseInt(args[0]);
            if(p > 0)
                noPeople = p;
        }
        if(args.length == 2){
            int f = Integer.parseInt(args[1]);
            if(f > 0)
                noFloors = f;
        }

        Building building = new Building(noPeople,noFloors);
        building.runSimulationConcurrent();
//        building.runSimulation();

        logger.info("finished execution of main.");
    }
}
