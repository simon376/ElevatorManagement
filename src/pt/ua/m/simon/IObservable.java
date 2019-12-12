package pt.ua.m.simon;

import pt.ua.gboard.Gelem;

import java.util.Observer;

public interface IObservable {
    public void registerObserver(IObserver o);
    public void notifyObservers();
    public Gelem getGelem();
    public int getLine();
    public int getColumn();
    public int getLayer();
}
