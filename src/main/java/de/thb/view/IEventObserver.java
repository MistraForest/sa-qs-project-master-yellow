package de.thb.view;

import de.thb.model.Event;

public interface IEventObserver {
    void update(Event post);
}
