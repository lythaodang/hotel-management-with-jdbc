package Fix;
import BasicPanel;
import Model;
import View;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * The panel in which the manager can view reservations.
 */
@SuppressWarnings("serial")
public class ManagerViewPanel extends BasicPanel {
	private final static String[] monthList = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private final static String[] daysList = { "S", "M", "T", "W", "T", "F", "S" };
	private final static int DAYS_IN_A_WEEK = 7;
	private final static int MAX_WEEKS_IN_A_MONTH = 6;

	private JButton selectedDay;

	/**
	 * Constructs the panel with a view manager.
	 * @param m the view manager
	 */
	public ManagerViewPanel(final View m) {
		super(m);
		selectedDay = null;
		
		c.weightx = 0;
		c.gridwidth = 2;
		addInstructions("<html>Select a day, month, or year and the calendar will update."
				+ "<br>Note: A day must be clicked to view availability.</html>");
		addDropDown();
		addCalendar();
		addRoomInfo();

		model.setSelectedDate(Model.TODAY);
	}

	/**
	 * Adds drop down menu
	 */
	public void addDropDown() {
		final JComboBox<Object> months = new JComboBox<Object>(monthList);
		months.setSelectedItem(monthList[Model.TODAY.get(Calendar.MONTH)]);
		months.setBackground(Color.white);
		months.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDay = null;
				GregorianCalendar temp = model.getSelectedDate();
				temp.set(Calendar.MONTH, months.getSelectedIndex());
				model.setSelectedDate(temp);
			}
		});
		c.gridwidth = 1;
		c.weightx = 0.05;
		c.weighty = 0.25;
		addComponent(months, 0, 1);

		final SpinnerModel spinnerModel = new SpinnerNumberModel(
				Model.TODAY.get(Calendar.YEAR),
				Model.TODAY.getMinimum(GregorianCalendar.YEAR),
				Model.TODAY.getMaximum(GregorianCalendar.YEAR), 1);
		JSpinner spinner = new JSpinner(spinnerModel);
		NumberEditor editor = new NumberEditor(spinner, "#");
		editor.getTextField().setHorizontalAlignment(JTextField.LEFT);
		editor.getTextField().setEditable(false);
		editor.getComponent(0).setBackground(Color.white);
		spinner.setEditor(editor);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				selectedDay = null;
				GregorianCalendar temp = model.getSelectedDate();
				temp.set(Calendar.YEAR, (int) spinnerModel.getValue());
				model.setSelectedDate(temp);
			};
		});
		addComponent(spinner, 1, 1);

		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedDay = null;
				spinnerModel.setValue(Model.TODAY.get(Calendar.YEAR));
				months.setSelectedItem(monthList[Model.TODAY.get(Calendar.MONTH)]);
				model.setSelectedDate(Model.TODAY);
				manager.switchPanel("MMenu");
			}
		});
		c.gridwidth = 3;
		c.weightx = 1;
		c.weighty = 0.5;
		addComponent(back, 0, 3);
	}

	/**
	 * Adds Calendar
	 */
	public void addCalendar() {
		JPanel calendarPanel = new JPanel(new GridLayout(
				MAX_WEEKS_IN_A_MONTH + 1, DAYS_IN_A_WEEK));

		for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
			JLabel label = new JLabel(daysList[i], JLabel.CENTER);
			label.setBackground(Color.blue);
			label.setBorder(null);
			label.setOpaque(true);
			label.setForeground(Color.white);
			calendarPanel.add(label);
		}

		final JButton[][] days = new JButton[MAX_WEEKS_IN_A_MONTH][DAYS_IN_A_WEEK];

		for (int i = 0; i < MAX_WEEKS_IN_A_MONTH; i++)
			for (int j = 0; j < DAYS_IN_A_WEEK; j++) {
				final JButton button = new JButton();
				button.setBackground(Color.white);
				button.setOpaque(true);
				days[i][j] = button;
				calendarPanel.add(days[i][j]);

				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!button.getText().equals("")) {
							selectedDay = button;
							GregorianCalendar temp = model.getSelectedDate();
							temp.set(Calendar.DATE, Integer.parseInt(button.getText()));
							model.setSelectedDate(temp);
						}
					}
				});
			}

		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				final int daysInMonth = model.getSelectedDate().getActualMaximum(
						GregorianCalendar.DAY_OF_MONTH);
				GregorianCalendar temp = (GregorianCalendar) model
						.getSelectedDate().clone();
				temp.set(Calendar.DAY_OF_MONTH, 1);
				final int weekdayStart = temp.get(GregorianCalendar.DAY_OF_WEEK) - 1;

				int currDay = 1;
				for (int i = 0; i < MAX_WEEKS_IN_A_MONTH; i++)
					for (int j = 0; j < DAYS_IN_A_WEEK; j++) {
						if (currDay <= daysInMonth) {
							if ((i == 0 && j >= weekdayStart) || (i != 0)) {
								days[i][j].setText(Integer.toString(currDay));
								currDay++;
							} else days[i][j].setText("");
						} else days[i][j].setText("");
					}
			}
		});

		c.weightx = 0;
		c.weighty = 1;
		c.gridwidth = 2;
		addComponent(calendarPanel, 0, 2);
	}

	/**
	 * Adds room information
	 */
	public void addRoomInfo() {
		final JTextArea roomInfo = new JTextArea("Room Information");
		roomInfo.setEditable(false);
		final JScrollPane scrollPane = new JScrollPane(roomInfo,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) roomInfo.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		c.gridwidth = 1;
		c.gridheight = 3;
		c.weightx = 1;
		addComponent(scrollPane, 2, 0);

		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (selectedDay != null) roomInfo.setText(model.getRoomInformation());
				else roomInfo.setText("");
			}
		});
	}
}