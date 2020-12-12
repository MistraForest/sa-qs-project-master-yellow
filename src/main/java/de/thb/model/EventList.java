package de.thb.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventList implements IEvent{

	
	public List<Event> events = new ArrayList<>();
	public Event e = null;


	@Override
	public List<Event> loadEvents(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("EventsData.txt"));
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
		    	event.setNumberOfTicket(Integer.valueOf(eventAttributs[2]));

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
	public Event findByEventName(final String eventName) {
		List<Event> events = loadEvents();
		for(int i = 0; i < events.size(); i++) {
			if(eventName.equals(events.get(i).getName())) {
				e = events.get(i);
			}
		}
    	return e;
    }
}
