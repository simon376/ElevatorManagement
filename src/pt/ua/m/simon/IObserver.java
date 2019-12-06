package pt.ua.m.simon;

public interface IObserver {
    public void update(IObservable observable, int old_line, int old_col);
}

