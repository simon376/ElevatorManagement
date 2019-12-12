package pt.ua.m.simon;

import pt.ua.gboard.Gelem;
import pt.ua.m.simon.view.BuildingPosition;

import java.util.Observer;

public interface IObservable {
    public void registerObserver(IObserver o);
    public void notifyObservers();
    public Gelem getGelem();
    public BuildingPosition getPosition();
    public BuildingPosition getPrevPosition();
}
