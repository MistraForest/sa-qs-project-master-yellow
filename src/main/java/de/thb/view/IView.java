package de.thb.view;

import de.thb.model.Event;

public interface IView {
    int calculateRestOfEventTicket(int numberOfTicket, int numberOfTicketToBuy);
    void setData(Event event);
}
