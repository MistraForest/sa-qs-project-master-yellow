package de.thb.view;

import de.thb.model.Event;

import javax.swing.*;

public interface IView {
    int calculateRestOfEventTicket(int numberOfTicket, int numberOfTicketToBuy);
    void setData(Event event);

    float calculatePercentage(int restOfEvents, int actual);

    float getPercentageTen();
    float getPercentageFive();
    float getPercentageZero();

    JComponent getUserInterface(int anzViews);
}
