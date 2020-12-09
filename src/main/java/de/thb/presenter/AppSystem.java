package de.thb.presenter;

import java.util.List;

import de.thb.model.EventList;
import de.thb.model.Event;
import de.thb.view.UserInteraction;

public class AppSystem {

    private UserInteraction userInteraction ;
    
    private EventList eventList;
    
    public AppSystem() {
    	eventList = new EventList();
    }
    
    public int berechneRestOfEvents(int anzOfEvents, int anzTicketToBuy) {
		 return (anzOfEvents - anzTicketToBuy);
    }
    
    public List<Event> getEvents(){
    	return eventList.loadEvents();
    }
    
    public void setData(Event e){
    	eventList.setData(e);
    }
    
    public Event findByEventName(final String eventName) {
    	return eventList.findByEventName(eventName);
    }
}
