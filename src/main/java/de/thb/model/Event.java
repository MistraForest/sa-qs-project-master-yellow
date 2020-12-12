package de.thb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Observable;
import java.util.Random;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

	private String name;
    private LocalDate date;
    private int numberOfTicket; 
    private int restOfTicket; 

    public int getRestOfTicket() {
		return restOfTicket;
	}

	public void setRestOfTicket(int restOfTicket) {
    	if (restOfTicket < 0) restOfTicket = 0;
		this.restOfTicket = restOfTicket;
	}
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate localDate) {
		this.date = localDate;
	}

	public int getNumberOfTicket() {
		return numberOfTicket;
	}

	public void setNumberOfTicket(int numberOfTicket) {
    	if(numberOfTicket < 0) numberOfTicket = 0;
		this.numberOfTicket = numberOfTicket;
	}
    
}
