package pt.ua.m.simon;

import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.main");

    public static void main(String[] args) {
	// write your code here
        Building building = new Building(1,5);
        building.runSimulation();

        logger.info("finished execution of main.");
    }
}
