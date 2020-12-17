package de.thb.model;

import de.thb.view.IEventObserver;

public interface IEventObservable {
    void registerObserver(IEventObserver observer);
    void removeObserver(IEventObserver observer);
    void notifyObservers();
}
