package de.thb.presenter;

import de.thb.model.Event;
import de.thb.model.EventList;
import de.thb.model.IEvent;

import java.util.List;

public class AppSystem {

    private static IEvent eventList;
    private static AppSystem INSTANCE;
    
    private AppSystem(IEvent eventList) {
        this.eventList = new EventList();
    }
    public static AppSystem getIstance(){
        if (INSTANCE == null){
            INSTANCE = new AppSystem(eventList);
        }
        return INSTANCE;
    }
    
    public int calculateRestOfEventTicket(int numberOfTicket, int numberOfTicketToBuy) {
		 return (numberOfTicket - numberOfTicketToBuy);
    }

    public List<Event> getEvents() {
        return eventList.loadEvents();
    }

    public void setData(Event e){
    	eventList.setData(e);
    }

    public Event findByEventName(final String eventName) {
    	return eventList.findByEventName(eventName);
    }


}
