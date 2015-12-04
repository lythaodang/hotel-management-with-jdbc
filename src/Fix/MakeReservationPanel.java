package Fix;
import BasicPanel;
import Model;
import Room;
import View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * The panel in which the user can make a reservation.
 */
@SuppressWarnings("serial")
public class MakeReservationPanel extends BasicPanel {
	/**
	 * Constructor for make reservation panel
	 * @param m the view
	 */
	public MakeReservationPanel(View m) {
		super(m);

		c.gridwidth = 2;
		addInstructions("<html>Please enter a check-in and "
				+ "check-out date. Then choose your room type.<br> A list of "
				+ "available rooms will be displayed when all input is valid. <br>"
				+ "Note: Dates must be in correct format (MM/DD/YYYY).</html>");

		c.gridwidth = 1;
		addLabel("Check-in:", 0, 1);
		addLabel("Check-out:", 1, 1);

		addControllers();
		addView();
	}

	/**
	 * Adds the controllers of MVC pattern.
	 */
	private void addControllers() {
		final JTextField checkIn = new JTextField();
		addComponent(checkIn, 0, 2);

		final JTextField checkOut = new JTextField();
		addComponent(checkOut, 1, 2);

		checkIn.getDocument().addDocumentListener(
				new TextFieldListener(checkIn, "in"));
		checkOut.getDocument().addDocumentListener(
				new TextFieldListener(checkOut, "out"));

		addLabel("Room type:", 0, 3);

		final JButton luxuryRoom = new JButton("$200");
		luxuryRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCost(200);
			}
		});
		addComponent(luxuryRoom, 0, 4);
		
		final JButton normalRoom = new JButton("$80");
		normalRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setCost(80);
			}
		});
		addComponent(normalRoom, 1, 4);

		JButton confirmButton = new JButton("Confirmed");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getSelectedRoom() != null) {
					model.addReservation();
					int response = JOptionPane.showConfirmDialog(
							new JFrame(), "<html>Your reservation has been saved.<br>"
							+ "Would you like to make more transactions?</html>",
							"Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) manager.switchPanel("Receipt");
					if (response == JOptionPane.YES_OPTION) ;
					clearComponents();
					model.setCheckIn(null);
					model.setCheckOut(null);
					model.setCost(0);
				}
				else
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: No room has been selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		addComponent(confirmButton, 0, 7);
		
		JButton transButton = new JButton("Transaction Done");
		transButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getTransaction().isEmpty()) 
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: No reservations have been made.", "Error",
							JOptionPane.ERROR_MESSAGE);
				else {
					clearComponents();
					model.setCheckIn(null);
					model.setCheckOut(null);
					model.setCost(0);
					manager.switchPanel("Receipt");
				}
			}
		});
		addComponent(transButton, 1, 7);
	}
	
	/**
	 * Adds the view of the MVC pattern
	 */
	private void addView() {
		final JLabel availableLabel = new JLabel(model.getValidityOfInput());
		addComponent(availableLabel, 0, 5);
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final JList list = new JList(model.getAvailRooms().toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		c.weighty = 1;
		c.gridwidth = 2;
		addComponent(listScroller, 0, 6);

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				model.setSelectedRoom((Room) list.getSelectedValue());
			}
		});

		model.addChangeListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void stateChanged(ChangeEvent event) {
				availableLabel.setText(model.getValidityOfInput());
				list.setListData(model.getAvailRooms().toArray());
			}
		});
	}

	/**
	 * TextFieldListeners to check if dates are inputed correctly.
	 */
	class TextFieldListener implements DocumentListener {
		private JTextField tf;
		private String inOrOut;

		/**
		 * Listener for text field
		 * @param tf the JTextField
		 * @param inOrOut check in or check out
		 */
		public TextFieldListener(JTextField tf, String inOrOut) {
			this.tf = tf;
			this.inOrOut = inOrOut;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			return;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			// must be 10 characters
			if (tf.getText().length() == 10) {
				// must be in correct format MM/DD/YYYY
				if (isValidDateFormat(tf.getText())) {
					GregorianCalendar date = stringToDate(tf.getText());
					// cannot be before today
					if (date.before(Model.TODAY)
							&& !model.compareDates(date, Model.TODAY)) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Error: Entered date is prior to today.", "Error",
								JOptionPane.ERROR_MESSAGE);
						if (inOrOut.equals("in")) model.setCheckIn(null);
						else model.setCheckOut(null);
					} else {
						if (inOrOut.equals("in")) model.setCheckIn(date);
						else model.setCheckOut(date);
					}
				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Invalid format.", "Error",
							JOptionPane.ERROR_MESSAGE);
					if (inOrOut.equals("in")) model.setCheckIn(null);
					else model.setCheckOut(null);
				}
			} else {
				if (inOrOut.equals("in")) model.setCheckIn(null);
				else model.setCheckOut(null);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			insertUpdate(e);
		}
	}

	/**
	 * Checks if inputed date is in correct format MM/DD/YYYY
	 * @param input the date
	 * @return true if date is valid else false
	 */
	private boolean isValidDateFormat(String input) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			format.setLenient(false);
			format.parse(input);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Converts the user's input into a GregorianCalendar.
	 * @param input the user's string input in the form of MM/DD/YYYY
	 * @return a GregorianCalendar with the user's inputed date
	 */
	public GregorianCalendar stringToDate(String input) {
		GregorianCalendar temp = new GregorianCalendar(Integer.parseInt(input
				.substring(6, 10)), Integer.parseInt(input.substring(0, 2)) - 1,
				Integer.parseInt(input.substring(3, 5)));

		return temp;
	}
}
