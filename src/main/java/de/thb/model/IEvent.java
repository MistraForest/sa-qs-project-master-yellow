package de.thb.model;

import java.util.List;

public interface IEvent {

    List<Event> loadEvents();
    void setData(Event e);
    Event findByEventName(final String eventName);
}
