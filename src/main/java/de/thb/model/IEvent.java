package de.thb.model;

import java.util.List;

public interface IEvent {

    List<Event> loadEvents();
//    void updateEventNumberOfTicket(int numberOfTicket);
//    List<Event> getEvents();
    void setData(Event e);
    Event findByEventName(final String eventName);
}
