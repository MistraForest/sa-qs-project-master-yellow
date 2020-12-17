package de.thb.view;

import de.thb.model.Event;
import de.thb.model.EventList;
import de.thb.presenter.AppSystem;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Data
public class DashboardUI extends JPanel implements IEventObserver {

	private AppSystem appSystem;
	private DefaultListModel eventListModel;
	private JList eventListField;
	private EventPropertyUI eventPropertyUI;
	private EventList eventList;


	public DashboardUI(int anzViews, EventList eventList) {

		setLayout(new GridLayout(0, 1, 5, 10));
		setBounds(20, 20, 60, 300);
		JScrollPane scrollPaneForEvents = new JScrollPane();

		this.eventList = eventList;
		appSystem = AppSystem.getInstance();
		eventPropertyUI = new EventPropertyUI(anzViews, eventList);
		List<Event> events = appSystem.getEvents();

		// DefaultListModell wird erzeugt
		eventListModel = new DefaultListModel();

		// JList mit Einträgen wird erstellt
		eventListField = new JList(eventListModel);
		if (1 == anzViews)
			eventListField.setCellRenderer(EventUtilitiesUI.getCellRenderer(Color.decode("#fce1fd")));
		else eventListField.setCellRenderer(EventUtilitiesUI.getCellRenderer(Color.decode("#E0FFFF")));

		for (Event event : events) {
			eventListModel.addElement(event.getName());
		}

		// JList wird Panel hinzugefügt
		add(eventListField);
		scrollPaneForEvents.setViewportView(eventListField);
		eventListField.setLayoutOrientation(JList.VERTICAL);
		add(scrollPaneForEvents);
		add(eventPropertyUI);

		eventListField.addListSelectionListener(evt -> jList1ValueChanged(anzViews));

	}

	private void jList1ValueChanged(int anzViews) {
		String selectedValue = (String) eventListField.getSelectedValue();
		final Event eventData = appSystem.findByEventName(selectedValue);
		eventPropertyUI.updateUIWithData(eventData,anzViews);
	}

	@Override
	public void update(Event post) {

	}
}
