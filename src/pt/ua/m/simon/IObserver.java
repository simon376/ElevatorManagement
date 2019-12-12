package pt.ua.m.simon;

import pt.ua.m.simon.view.BuildingPosition;

public interface IObserver {
    public void update(IObservable observable, BuildingPosition oldPosition);
}

