package de.thb.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.thb.model.ConstantPercentage;
import de.thb.model.Event;
import de.thb.presenter.AppSystem;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class EventPropertyUI extends JPanel implements ActionListener, FocusListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JTextField eventNameField, ticketQuantityField, ticketQuantityToBuyField, availibilityField, eventDateField;
	boolean isFieldSet = false;
	Font regularFont, boldRegularFont, italicFont, boldFont;
	JLabel actualAvailabilityDisplay;
	JLabel actualAvailabilityLabel = new JLabel();
	JPanel leftHalf;
	JButton button;
	private final int GAP_BETWEEN = 7;
	final static int TEXTFIELD_COLUMN = 5;
	private Event event;
	private int anzViews = 0;
	private IView appSystem = AppSystem.getIstance();

	public EventPropertyUI(int anzViews) {
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
			leftHalf.add(createButton());
		}
		return leftHalf;
	}

	private JButton createButton() {
		EventUtilitiesUI eventUtilitiesUI = new EventUtilitiesUI();
		if (null == button) {
			button = eventUtilitiesUI.createButtons("Buy Tickets", GAP_BETWEEN);
			button.setEnabled(false);
			button.addActionListener(e -> {
				getHighLight();
			});
		}
		return button;
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
		italicFont = regularFont.deriveFont(Font.CENTER_BASELINE);
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

	/**
	 * Called when one of the fields gets the focus so that the focused field is
	 * selectable.
	 */
	public void focusGained(FocusEvent e) {
		Component c = e.getComponent();
		if (c instanceof JTextField) {
			selectItLater(c);
		} else if (c instanceof JTextField) {
			((JTextField) c).selectAll();
		}
	}

	// Workaround for formatted text field focus side effects.
	protected void selectItLater(Component c) {
		if (c instanceof JTextField) {
			final JTextField ftf = (JTextField) c;
			SwingUtilities.invokeLater(() -> ftf.selectAll());
		}
	}

	private void getHighLight() {
		final String valueOfFieldTicketToBuy = ticketQuantityToBuyField.getText();

		if (!valueOfFieldTicketToBuy.isEmpty()) {
			final int anzTicketToBuy = Integer.parseInt(valueOfFieldTicketToBuy);
			final int restOfEvents = appSystem.calculateRestOfEventTicket(event.getNumberOfTicket(), anzTicketToBuy);
			final float percentage = ((float)restOfEvents / event.getNumberOfTicket());

			if (1 == anzViews) {
				availibilityField.setText(String.valueOf(restOfEvents));
				setHighlight(availibilityField, percentage);
			} else {
				actualAvailabilityDisplay.setText(String.valueOf(restOfEvents));
				actualAvailabilityDisplay.setOpaque(true);
				setHighlight(actualAvailabilityDisplay, percentage);
			}
			event.setRestOfTicket(restOfEvents);
			appSystem.setData(event);
		}
	}

	private void setHighlight(Component c, float percentage) {
		if (percentage <= ConstantPercentage.PERCENTAGE_10.getPercentage()
				&& percentage > ConstantPercentage.PERCENTAGE_5.getPercentage()) {
			c.setBackground(Color.green);
		} else if (percentage <= ConstantPercentage.PERCENTAGE_5.getPercentage()
				&& percentage > ConstantPercentage.PERCENTAGE_0.getPercentage()) {
			c.setBackground(Color.YELLOW);
		}else if (ConstantPercentage.PERCENTAGE_0.getPercentage() >= percentage || percentage < 0) {
			if (c instanceof JLabel) {
				actualAvailabilityDisplay.setBackground(Color.RED);
				actualAvailabilityDisplay.setText("Sold out!");
			} else if (c instanceof JTextField) {
				availibilityField.setBackground(Color.RED);
				availibilityField.setText("Sold out!");
			}
		}
	}

	// Needed for FocusListener interface.
	public void focusLost(FocusEvent e) {
	} // ignore

	public void setData(final Event e, int anzViews) {
		System.out.println(anzViews);
		this.anzViews = anzViews;
		this.event = e;
		actualAvailabilityLabel.setText("Actual Availability");
		eventNameField.setText(e.getName());
		eventDateField.setText(String.valueOf(e.getDate()).substring(0, 10));
		ticketQuantityField.setText(String.valueOf(e.getNumberOfTicket()));
		if (1 == anzViews) {
			availibilityField.setText("");
			availibilityField.setBackground(Color.LIGHT_GRAY);
		} else if (2 == anzViews) {
			actualAvailabilityDisplay.setBackground(Color.LIGHT_GRAY);
			actualAvailabilityDisplay.setText("");
		}
		ticketQuantityToBuyField.setText("");
	}

	protected JComponent createEntryFields() {
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
			ticketQuantityToBuyField = new JTextField();
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
					if(!ticketQuantityToBuyField.getText().isEmpty()) {
						if (Integer.parseInt(ticketQuantityToBuyField.getText()) > 0) {
							createButton().setEnabled(true);
						}
					}else {
						createButton().setEnabled(false);
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
			actualField.addFocusListener(this);
		}
		EventUtilitiesUI.buildCompactGrid(panel, labelStrings.length, 2, 0, 0, 0, GAP_BETWEEN);
		return panel;
	}
}