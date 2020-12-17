package de.thb.view;

import de.thb.model.Event;
import de.thb.model.EventList;
import de.thb.presenter.AppSystem;
import lombok.Data;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

@Data
public class EventPropertyUI extends JPanel implements ActionListener, IEventObserver {

	public static final Logger LOGGER = Logger.getLogger(EventPropertyUI.class.getName());
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTextField eventNameField, ticketQuantityField, ticketQuantityToBuyField, availibilityField, eventDateField;
	private boolean isFieldSet = false;
	Font regularFont, boldRegularFont, italicFont, boldFont;
	private JLabel actualAvailabilityDisplay = new JLabel();
	private JLabel actualAvailabilityLabel = new JLabel();
	private JPanel leftHalf;
	private JButton button;
	private final EventUtilitiesUI eventUtilitiesUI;
	private final int GAP_BETWEEN = 7;
	private final static int TEXTFIELD_COLUMN = 5;
	private Event event;
	private EventList eventList;
	private int anzViews;
	private final IView appSystem = AppSystem.getInstance();


	public EventPropertyUI(int anzViews, EventList eventList) {
		this.eventList = eventList;
		eventUtilitiesUI = new EventUtilitiesUI();
		this.anzViews = anzViews;

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(createLeftHalf());
		if (2 == this.anzViews) {
			add(createActualAvailabilityDisplay());
		}
	}

	JPanel createLeftHalf() {
		if (null == leftHalf) {
			leftHalf = new JPanel() {

				private static final long serialVersionUID = 1L;

				public Dimension getMaximumSize() {
					Dimension pref = getPreferredSize();
					return new Dimension(Integer.MAX_VALUE, pref.height);
				}
			};
			leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.PAGE_AXIS));
			leftHalf.add(createEntryFields());
			leftHalf.add(getButton());
		}
		return leftHalf;
	}

	private JButton getButton() {
		if (null == button) {
			button = new EventUtilitiesUI().createButtons("Buy Tickets", GAP_BETWEEN);
			button.setEnabled(false);
			button.addActionListener(e -> {
				try {
					getHighLight();
				} catch (Exception exception) {
					showDialog("Select first an event to check out.");
					System.out.println(exception.getLocalizedMessage()+": Event fields are empty"+ " when heating " +e.getActionCommand());
				}
			});
		}
		return button;
	}
	private void showDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	/**
	 * Called when the user clicks the button or presses Enter in a text field.
	 */
	public void actionPerformed(ActionEvent e) {

	}
	protected void updateDisplays() {
		actualAvailabilityDisplay.setText(getActualAvailability());
		actualAvailabilityLabel.setText("");
		if (isFieldSet) {
			actualAvailabilityDisplay.setFont(regularFont);
			actualAvailabilityLabel.setFont(regularFont);
		} else {
			actualAvailabilityDisplay.setFont(boldFont);
			actualAvailabilityLabel.setFont(italicFont);
		}
	}

	protected JComponent createActualAvailabilityDisplay() {
		JPanel panel = new JPanel(new BorderLayout());
		actualAvailabilityDisplay = new JLabel();
		actualAvailabilityLabel = new JLabel();
		actualAvailabilityDisplay.setHorizontalAlignment(JLabel.CENTER);
		actualAvailabilityLabel.setHorizontalAlignment(JLabel.CENTER);
		boldRegularFont = actualAvailabilityDisplay.getFont().deriveFont(Font.ITALIC, 45.0f);
		regularFont = actualAvailabilityLabel.getFont().deriveFont(Font.ITALIC, 16.0f);
		italicFont = regularFont.deriveFont(Font.BOLD);
		boldFont = boldRegularFont.deriveFont(Font.BOLD);
		updateDisplays();

		// Lay out the panel.
		panel.setBorder(BorderFactory.createEmptyBorder(GAP_BETWEEN - 5, 0, GAP_BETWEEN - 5, 0));
		panel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
		panel.add(actualAvailabilityDisplay, BorderLayout.CENTER);
		panel.add(actualAvailabilityLabel, BorderLayout.NORTH);
		panel.setPreferredSize(new Dimension(200, 150));

		return panel;
	}

	protected String getActualAvailability() {
		if (!isFieldSet)
			return "Actual Availability";
		String actualAvailability = actualAvailabilityLabel.getText();
		return " Actual Availability " + actualAvailability;
	}

	private void getHighLight() {
		final String valueOfFieldTicketToBuy = ticketQuantityToBuyField.getText();

		if (!valueOfFieldTicketToBuy.isEmpty()) {
			int anzTicketToBuy = Integer.parseInt(valueOfFieldTicketToBuy);
			int restOfEvents = getRestOfEvents(anzTicketToBuy);
			float percentage = getPercentage(restOfEvents);
			if (percentage == getPercentageZero()) {
				event.setNumberOfTicket((int)percentage);
			}
			if (1 == anzViews) {
				availibilityField.setText(String.valueOf(restOfEvents));
				setHighlight(availibilityField, percentage);
			} else {
				actualAvailabilityDisplay.setText(String.valueOf(restOfEvents));
				actualAvailabilityDisplay.setOpaque(true);
				setHighlight(actualAvailabilityDisplay, percentage);
			}
			event.setRestOfTicket(restOfEvents);
			event.setNumberOfTicket(restOfEvents);

			appSystem.setData(event);
		}
	}

	private float getPercentage(int restOfEvents) {
		return appSystem.calculatePercentage(restOfEvents, event.getNumberOfTicket());
	}

	private int getRestOfEvents(int numberOfTicketToBuy) {
		return appSystem.calculateRestOfEventTicket(event.getNumberOfTicket(), numberOfTicketToBuy);
	}

	private void setHighlight(Component c, float percentage) {
		if (percentage <= getPercentageTen()
				&& percentage > getPercentageFive()) {
			c.setBackground(Color.GREEN);
		} else if (percentage <= getPercentageFive()
				&& percentage > getPercentageZero()) {
			c.setBackground(Color.YELLOW);
		}else if (getPercentageZero() >= percentage || percentage < 0) {
			if (c instanceof JLabel) {
				actualAvailabilityDisplay.setBackground(Color.RED);
				actualAvailabilityDisplay.setText("Sold out!");
				event.setNumberOfTicket(0);
			} else if (c instanceof JTextField) {
				availibilityField.setBackground(Color.RED);
				availibilityField.setText("Sold out!");
			}
		}
	}
	private float getPercentageTen() {
		return appSystem.getPercentageTen();
	}

	private float getPercentageFive() {
		return appSystem.getPercentageFive();
	}

	private float getPercentageZero() {
		return appSystem.getPercentageZero();
	}


	public void updateUIWithData(final Event eventData, int anzViews) {
		this.anzViews = anzViews;
		update(eventData);
//		this.event = eventData;
		actualAvailabilityLabel.setText("Actual Availability");
		eventNameField.setText(eventData.getName());
		eventDateField.setText(String.valueOf(eventData.getDate()).substring(0, 10));
		ticketQuantityField.setText(String.valueOf(eventData.getNumberOfTicket()));
		if (1 == anzViews) {
			availibilityField.setText("");
			availibilityField.setBackground(Color.LIGHT_GRAY);
		} else if (2 == anzViews) {
			actualAvailabilityDisplay.setBackground(Color.LIGHT_GRAY);
			actualAvailabilityDisplay.setText("");
		}
		ticketQuantityToBuyField.setText("");
	}

	private JComponent createEntryFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings;
		if (1 == anzViews) {
			labelStrings = new String[] { "Event: ", "Date: ", "Quantity: ", "Actual Availibility: ",
					"Booked Tickets: " };
		} else {
			labelStrings = new String[] { "Event: ", "Date: ", "Quantity: ", "Booked Tickets: " };
		}

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		setComponent(panel, labelStrings, labels, fields);
		EventUtilitiesUI.buildCompactGrid(panel, labelStrings.length, 2, 0, 0, 0, GAP_BETWEEN);
		return panel;
	}

	private void setComponent(JPanel panel, String[] labelStrings, JLabel[] labels, JComponent[] fields) {
		int fieldNum = 0;

		// Create the text field and set it up.
		if (null == eventNameField) {
			eventNameField = new JTextField();
			eventNameField.setColumns(TEXTFIELD_COLUMN);
			eventNameField.setEditable(false);
			fields[fieldNum++] = eventNameField;
		}
		if (null == eventDateField) {
			eventDateField = new JTextField();
			eventDateField.setColumns(TEXTFIELD_COLUMN);
			eventDateField.setEditable(false);
			fields[fieldNum++] = eventDateField;
		}
		if (null == ticketQuantityField) {
			ticketQuantityField = new JTextField();
			ticketQuantityField.setColumns(TEXTFIELD_COLUMN);
			ticketQuantityField.setEditable(false);
			fields[fieldNum++] = ticketQuantityField;
		}
		if (null == availibilityField && anzViews == 1) {
			availibilityField = new JTextField();
			availibilityField.setColumns(TEXTFIELD_COLUMN);
			availibilityField.setEditable(false);
			fields[fieldNum++] = availibilityField;
		}
		if (null == ticketQuantityToBuyField) {
			ticketQuantityToBuyField = eventUtilitiesUI.createdHintextField("Enter a number");
			ticketQuantityToBuyField.setColumns(TEXTFIELD_COLUMN);
			ticketQuantityToBuyField.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					warn();
				}

				public void removeUpdate(DocumentEvent e) {
					warn();
				}

				public void insertUpdate(DocumentEvent e) {
					warn();
				}

				public void warn() {
					try {
						if(!ticketQuantityToBuyField.getText().isEmpty()) {
							if (Integer.parseInt(ticketQuantityToBuyField.getText()) > 0) {
								getButton().setEnabled(true);
							}
						}else {
							getButton().setEnabled(false);
						}
					} catch (NumberFormatException e) {
						showDialog("Please entez only positiv number!");
						System.out.println("Wrong value "+ e.getLocalizedMessage());
					}
				}
			});
			fields[fieldNum++] = ticketQuantityToBuyField;
		}


		// Associate label/field pairs, add everything,
		// and lay it out.
		for (int i = 0; i < labelStrings.length; i++) {
			labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
			labels[i].setLabelFor(fields[i]);
			panel.add(labels[i]);
			panel.add(fields[i]);

			// Add listeners to each field.
			JTextField actualField = (JTextField) fields[i];

			actualField.addActionListener(this);
//			actualField.addFocusListener(this);
		}
	}

	@Override
	public void update(Event post) {
		LOGGER.info("Begin: "+ "update()");
		this.event = post;
//		System.out.println("First: "+ post);
		LOGGER.info("End: "+ "update()");
	}
}