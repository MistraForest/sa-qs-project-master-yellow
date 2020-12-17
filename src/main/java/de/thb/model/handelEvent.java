package de.thb.model;

import java.util.List;

public interface handelEvent {
    List<Event> loadEvents();
    void setData(Event event);
}
