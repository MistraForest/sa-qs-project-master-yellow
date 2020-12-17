package de.thb.model;

import java.util.List;

public interface IEvent {

    List<Event> getLoadedEvents();
    void writeUpdatedData(Event event);
    Event findByEventName(String eventName);
    float getPercentageTen();
    float getPercentageFive();
    float getPercentageZero();
}
