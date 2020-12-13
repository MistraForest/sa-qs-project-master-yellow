package de.thb.presenter;

import de.thb.model.Event;
import de.thb.model.EventList;
import de.thb.model.IEvent;
import de.thb.view.IView;

import java.util.List;

public class AppSystem implements IView {

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

    @Override
    public int calculateRestOfEventTicket(int numberOfTicket, int numberOfTicketToBuy) {
		 return (numberOfTicket - numberOfTicketToBuy);
    }

    public List<Event> getEvents() {
        return eventList.loadEvents();
    }

    @Override
    public void setData(Event event){
    	eventList.setData(event);
    }

    @Override
    public float calculatePercentage(int restOfEvents, int actual) {
        return (float)restOfEvents / actual;
    }

    public Event findByEventName(final String eventName) {
    	return eventList.findByEventName(eventName);
    }


}
