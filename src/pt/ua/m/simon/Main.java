package pt.ua.m.simon;

import java.util.logging.Logger;

class Main {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.main");

    public static void main(String[] args) {
	// write your code here
        Building building = new Building(5,8);
        building.runSimulationConcurrent();
//        building.runSimulation();

//        BuildingView view = new BuildingView("gboard_map.txt",1);
        logger.info("finished execution of main.");
    }
}
