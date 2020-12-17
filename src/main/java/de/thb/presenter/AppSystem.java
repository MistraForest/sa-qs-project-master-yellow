package de.thb.presenter;

import de.thb.model.Event;
import de.thb.model.EventList;
import de.thb.model.IEvent;
import de.thb.view.DashboardUI;
import de.thb.view.IView;

import javax.swing.*;
import java.util.List;

public class AppSystem implements IView {

    private static IEvent eventList;
    private static AppSystem INSTANCE;
    
    private AppSystem(IEvent eventList) {
        this.eventList = EventList.getInstance();
    }
    public synchronized static AppSystem getInstance(){
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
        return eventList.getLoadedEvents();
    }

    @Override
    public void setData(Event event){
    	eventList.writeUpdatedData(event);
    }

    @Override
    public float calculatePercentage(int restOfEvents, int actual) {
        return (float)restOfEvents / actual;
    }

    @Override
    public float getPercentageTen() {
        return eventList.getPercentageTen();
    }

    @Override
    public float getPercentageFive() {
        return eventList.getPercentageFive();
    }

    @Override
    public float getPercentageZero() {
        return eventList.getPercentageZero();
    }

    @Override
    public JComponent getUserInterface(int anzViews) {
        EventList eventList = EventList.getInstance();

        DashboardUI dashboardUI = new DashboardUI(anzViews, eventList);
        eventList.registerObserver(dashboardUI);
        eventList.notifyObservers();
        return dashboardUI;
    }

    public Event findByEventName(String eventName) {
    	return eventList.findByEventName(eventName);
    }

}
