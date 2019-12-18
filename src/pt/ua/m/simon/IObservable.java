package pt.ua.m.simon;

import pt.ua.gboard.Gelem;
import pt.ua.m.simon.view.BuildingPosition;

public interface IObservable {
    void registerObserver(IObserver o);
    void notifyObservers();
    Gelem getGelem();
    BuildingPosition getPosition();
    BuildingPosition getPrevPosition();
}
