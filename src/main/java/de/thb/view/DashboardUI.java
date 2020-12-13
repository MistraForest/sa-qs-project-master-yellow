package de.thb.view;

import de.thb.model.Event;
import de.thb.presenter.AppSystem;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class DashboardUI extends JPanel {

	private AppSystem appSystem;
	private DefaultListModel eventListModel;
	private JList eventListField;
	private EventPropertyUI eventPropertyUI;


	public DashboardUI(int anzViews) {

		setLayout(new GridLayout(0, 1, 5, 10));
		setBounds(20, 20, 60, 300);
		JScrollPane scrollPaneForEvents = new JScrollPane();

		appSystem = AppSystem.getIstance();
		eventPropertyUI = new EventPropertyUI(anzViews);
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
		final String selectedValue = (String) eventListField.getSelectedValue();
		final Event e = appSystem.findByEventName(selectedValue);
		eventPropertyUI.setData(e,anzViews);
	}
}
