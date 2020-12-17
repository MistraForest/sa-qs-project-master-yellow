package de.thb.model;

import de.thb.view.IEventObserver;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
@AllArgsConstructor
public class EventList implements IEvent, IEventObservable, handelEvent{

	public static final Logger LOGGER = Logger.getLogger(EventList.class.getName());
	
	private List<Event> events = new ArrayList<>();
	final String FILE_NAME = "EventsData.txt";
	private List<IEventObserver> iEventObservers;
	private Event event;
	private static EventList INSTANCE;

	private EventList(){
		iEventObservers = new ArrayList<>();
	}

	public synchronized static EventList getInstance(){
		if (INSTANCE == null){
			INSTANCE = new EventList();
		}
		return INSTANCE;
	}

	@Override
	public List<Event> loadEvents(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
		    String line = br.readLine();

		    while (line != null) {
		    	//Getting Event Attribut
		    	final String[] eventAttributs= line.split(",");

		    	//creating Events
		    	Event event = new Event();
		    	event.setName(eventAttributs[0]);
		    	event.setDate(LocalDate.parse(eventAttributs[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    	event.setNumberOfTicket(Integer.parseInt(eventAttributs[2]));

		    	events.add(event);

		        line = br.readLine();
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return events;
	}

	@Override
	public List<Event> getLoadedEvents() {
		return loadEvents();
	}

	@Override
	public void writeUpdatedData(Event event) {
		setData(event);
	}

	@Override
	public void setData(Event updatedEvent){
		BufferedReader br = null;
		FileReader fr = null;
		File originalFile = null;
		File tempFile = null;
		PrintWriter pw = null;
		try {
			originalFile = new File("EventsData.txt");
			tempFile = new File("tempfile.txt");
			fr = new FileReader(originalFile);
			br = new BufferedReader(fr);
			
			pw = new PrintWriter(new FileWriter(tempFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String line = null;
			
			while ((line = br.readLine()) != null) {
				//Getting Event Attribut
				final String[] eventAttributs= line.split(",");
				
				//Updating Data
				if(line.contains(updatedEvent.getName()) ){
					eventAttributs[2] = String.valueOf(updatedEvent.getRestOfTicket());

					line = eventAttributs[0] + "," + eventAttributs[1] + "," + eventAttributs[2];
				}
				pw.println(line);
				pw.flush();
			}
			pw.close();
			br.close();
			
			// Delete the original file
	        if (!originalFile.delete()) {
	            System.out.println("Could not delete file");
	            return;
	        }

	        // Rename the new file to the filename the original file had.
	        if (!tempFile.renameTo(originalFile)) {
	            System.out.println("Could not rename file");
	            return;
	        }

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Event findByEventName(String eventName) {
		LOGGER.info("Begin: "+ "findByEventName()");
		List<Event> events = loadEvents();
		for(int i = 0; i < events.size(); i++) {
			if(eventName.equals(events.get(i).getName())) {
				event = events.get(i);
			}
		}
		LOGGER.info("End: "+ "findByEventName()"+ eventName);
    	return event;
    }

	@Override
	public float getPercentageTen() {
		return ConstantPercentage.PERCENTAGE_10.getPercentage();
	}

	@Override
	public float getPercentageFive() {
		return ConstantPercentage.PERCENTAGE_5.getPercentage();
	}

	@Override
	public float getPercentageZero() {
		return ConstantPercentage.PERCENTAGE_0.getPercentage();
	}


	@Override
	public void registerObserver(IEventObserver observer) {
		iEventObservers.add(observer);
	}

	@Override
	public void removeObserver(IEventObserver observer) {
		iEventObservers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		for (IEventObserver observer: iEventObservers){
			observer.update(event);
		}
		LOGGER.info("End: "+ "notifyObservers()");
	}
}
