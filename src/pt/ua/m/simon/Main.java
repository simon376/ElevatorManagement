package pt.ua.m.simon;

import gboard.TestGBoard;
import pt.ua.m.simon.view.BuildingView;

import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger("pt.ua.m.simon.main");

    public static void main(String[] args) {
	// write your code here
        Building building = new Building(3,5);
        building.runSimulation();

//        BuildingView view = new BuildingView("gboard_map.txt",1);
        logger.info("finished execution of main.");
    }
}
