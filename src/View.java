import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * The view manager. Contains the frame and all different panels.
 */
public class View {
	private Model model;
	private JPanel cards;
	private CardLayout cardLayout;
	private JFrame frame;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	final View view = this;

	/**
	 * Constructs the frame and adds panels to CardLayout.
	 */
	public View(Model model) {
		this.model = model;
		frame = new JFrame("Hotel");
		cards = new JPanel(cardLayout = new CardLayout());

		// add panels to the card layout
		cards.add(getLoginPanel(), "Login");
		cards.add(getChooseRolePanel(), "Choose Role");
		cards.add(getRegisterPanel(), "Register");
		cards.add(getForgotPasswordPanel(), "Forgot Password");
		cards.add(getWelcomePanel("Customer"), "Customer");
		cards.add(getWelcomePanel("Manager"), "Manager");
		cards.add(getWelcomePanel("Room Service"), "Room Service");
		cards.add(getWelcomePanel("Receptionist"), "Receptionist");
		cards.add(getMakeReservationPanel(), "Book");
		cards.add(getReceiptPanel(), "Receipt");

		//Kun added
		cards.add(getGiveFeedbackPanel(), "Give Feedback");
		
		
		frame.add(cards); // add the panel with card layout to the frame

		// below are the frame's characteristics
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Gets the model of the view manager.
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Switches the current panel.
	 * @param panelName
	 */
	public void switchPanel(String panelName) {
		if (panelName.equals("Login"))
			frame.setSize(400, 350);
		else 
			frame.setSize(600, 500);
		frame.setLocationRelativeTo(null);
		cardLayout.show(cards, panelName);
	}

	private JPanel getLoginPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;
		c.gridwidth = 2;
		c.ipady = 15;
		panel.addLabel("Login", 36, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(10,15,5,15);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipady = 0;
		panel.addLabel("Username:", 20, "center", null, null, 0, 2);
		final JTextField usernameField = new JTextField();
		panel.addComponent(usernameField, 1, 2);

		c.insets = new Insets(5,15,5,15);

		panel.addLabel("Password:", 20, "center", null, null, 0, 3);
		final JPasswordField passwordField = new JPasswordField();
		panel.addComponent(passwordField, 1, 3);

		c.gridwidth = 2;
		JButton loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (username.length() < 6 || username.length() > 12) {
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Entered user username is invalid.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!model.checkUserExistence(username)) {	
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Username does not exist in the system.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!model.checkUserPassword(username, password)) {
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Password is incorrect.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					panel.clearComponents();
					model.setCurrentUser(username);
					view.switchPanel(model.getCurrentRole());
				}
			}
		});
		panel.addComponent(loginBtn, 0, 4);

		c.gridwidth = 1;
		panel.addNavigationButton("Forgot Password?", 14, "Forgot Password", 0, 5);
		panel.addNavigationButton("Register", 14, "Choose Role", 1, 5);

		return panel;
	}

	private JPanel getChooseRolePanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;

		panel.addLabel("Choose a role", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(10,25,10,25);
		JButton custBtn = new JButton("Customer");
		custBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		custBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Customer");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(custBtn, 0, 1);

		JButton mgrBtn = new JButton("Manager");
		mgrBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		mgrBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Manager");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(mgrBtn, 0, 2);

		JButton recBtn = new JButton("Receptionist");
		recBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		recBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Receptionist");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(recBtn, 0, 3);

		JButton rsBtn = new JButton("Room Service");
		rsBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		rsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Room Service");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(rsBtn, 0, 4);

		panel.addNavigationButton("Back", 12, "Login", 0, 11);

		return panel;
	}

	private JPanel getRegisterPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;
		c.gridwidth = 4;
		final JLabel instructions = panel.addLabel("", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);
		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (model.getCurrentRole() != null) {
					String text = model.getCurrentRole();
					if (text.equalsIgnoreCase("room service"))
						text += " Staff";
					instructions.setText(text + " Registration");
				}
			};
		});

		c.weightx = 0;
		c.insets = new Insets(5,25,5,25);
		c.gridwidth = 1;
		panel.addLabel("First name (max 15 chars)", 12, "left", null, null, 0, 1);
		panel.addLabel("Last name (max 15 chars)", 12, "left", null, null, 0, 2);
		panel.addLabel("Username (6 to 12 chars)", 12, "left", null, null, 0, 3);
		panel.addLabel("Password (8 to 20 chars)", 12, "left", null, null, 0, 4);
		panel.addLabel("Age", 12, "left", null, null, 0, 5);
		panel.addLabel("Gender", 12, "left", null, null, 0, 6);
		panel.addLabel("Security Question (10 to 50 chars)", 12, "left", null, null, 0, 7);
		panel.addLabel("Security Answer (5 to 30 chars)", 12, "left", null, null, 0, 8);
		panel.addNavigationButton("Back", 16, "Choose Role", 0, 9);

		c.weightx = 1;
		c.gridwidth = 3;
		final JTextField firstName = new JTextField();
		panel.addComponent(firstName, 1, 1);

		final JTextField lastName = new JTextField();
		panel.addComponent(lastName, 1, 2);

		final JTextField username = new JTextField();
		panel.addComponent(username, 1, 3);

		final JPasswordField password = new JPasswordField();
		panel.addComponent(password, 1, 4);

		List<String> age = new ArrayList<String>();
		age.add("Select Age");
		for (int i = 18; i < 100; ++i)
			age.add(String.valueOf(i));
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final JComboBox ageComboBox = new JComboBox(age.toArray());
		panel.addComponent(ageComboBox, 1, 5);

		List<String> gender = new ArrayList<String>();
		gender.add("Select Gender");
		gender.add("Female");
		gender.add("Male");
		gender.add("Decline to state");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final JComboBox genderComboBox = new JComboBox(gender.toArray());
		panel.addComponent(genderComboBox, 1, 6);

		final JTextField securityQuestion = new JTextField();
		panel.addComponent(securityQuestion, 1, 7);

		final JTextField securityAnswer = new JTextField();
		panel.addComponent(securityAnswer, 1, 8);

		JButton registerBtn = new JButton("Register");
		registerBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		registerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean validEntry = true;
				String errors = "<html>The following are not valid entries:<br>";

				String first = firstName.getText();
				if (first.isEmpty() || first.length() > 15) {
					firstName.setText("");
					validEntry = false;
					errors += "First name<br>";
				}

				String last = lastName.getText();
				if (last.isEmpty() || last.length() > 15) {
					lastName.setText("");
					validEntry = false;
					errors += "Last name<br>";
				}

				String login = username.getText();
				if (login.length() < 6 || login.length() > 12 || model.checkUserExistence(login)) {
					username.setText("");
					validEntry = false;
					errors += "Username<br>";
				}

				String pass = new String(password.getPassword()); 
				if (pass.length() > 20 && pass.length() < 8) {
					password.setText("");
					validEntry = false;
					errors += "Password<br>";
				}

				Integer age = null;
				try {
					age = Integer.parseInt((String)ageComboBox.getSelectedItem());
				}
				catch (Exception e) {
					validEntry = false;
					errors += "Age<br>";
				}

				String gen = (String)genderComboBox.getSelectedItem();
				if (gen.equals("Female"))
					gen = "F";
				else if (gen.equals("Male"))
					gen = "M";
				else if (gen.equals("Decline to state"))
					gen = "D";
				else {
					validEntry = false;
					errors += "Gender<br>";
				}

				String secQuestion = securityQuestion.getText();
				if (secQuestion.length() > 50 || secQuestion.length() < 10) {
					securityQuestion.setText("");
					validEntry = false;
					errors += "Security Question<br>";
				}

				String secAnswer = securityAnswer.getText();
				if (secQuestion.length() > 30 || secQuestion.length() < 5) {
					securityAnswer.setText("");
					validEntry = false;
					errors += "Security Answer";
				}

				if (validEntry) {
					panel.clearComponents();
					if (model.addAccount(login, pass, first, last, age, gen, model.getCurrentRole(), secQuestion, secAnswer))
						view.switchPanel(model.getCurrentRole());
					else
						JOptionPane.showMessageDialog(new JFrame(), 
								"An unexpected error has occurred. Please contact your system admin.", "Error", 
								JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(new JFrame(), errors + "</html>", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		panel.addComponent(registerBtn, 1, 9);

		return panel;
	}

	private JPanel getForgotPasswordPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.gridwidth = 2;
		panel.addLabel("Retrieve Password", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(20, 20, 20, 20);
		c.ipady = 25;
		panel.addInstructions("<html>Enter a valid username and a question will appear."
				+ "<br>Answer the question to receive your password.</html>");

		c.weighty = 1;
		c.gridwidth = 1;
		panel.addLabel("Enter your username:", 16, "left", null, null, 0, 2);
		panel.addLabel("Your security question:", 16, "left", null, null, 0, 3);
		panel.addLabel("Enter your answer:", 16, "left", null, null, 0, 4);

		final JTextField userField = new JTextField();
		panel.addComponent(userField, 1, 2);

		final JLabel questionField = new JLabel();
		panel.addComponent(questionField, 1, 3);

		final JTextField answerField = new JTextField();
		panel.addComponent(answerField, 1, 4);

		userField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String username = userField.getText();
				String question = model.getUserSecurityQuestion(username);
				if (question != null) {
					questionField.setText(question);
				}
			}
		});

		panel.addNavigationButton("Back", 16, "Login", 0, 5);

		JButton submitButton = new JButton("Get Password");
		submitButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String password = null;
				String username = userField.getText();
				String answer = answerField.getText();
				if (questionField.getText().isEmpty())
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Enter a valid username", "Error",
							JOptionPane.ERROR_MESSAGE);
				else if (answer.length() < 1)
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Answer the security question", "Error",
							JOptionPane.ERROR_MESSAGE);
				else if ((password = model.checkSecurityAnswer(username, answer)) != null) {
					JOptionPane.showMessageDialog(new JFrame(),
							"Your password is " + password, "Password",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Wrong security answer.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.addComponent(submitButton, 1, 5);

		return panel;
	}

	private JPanel getWelcomePanel(String role) {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;
		c.insets = new Insets(10,10,10,10);

		final JLabel profile = new JLabel();
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event)
			{
				if (model.getCurrentUser() != null) {
					Account user = model.getCurrentUser();
					profile.setText("<html>Username: " + user.getUsername() 
					+ "<br>Name: " + user.getFirstName() + " " + user.getLastName()
					+ "<br>Role: " + user.getRole());
				}
			}
		});
		panel.addComponent(profile, 0, 0);

		panel.addSignOutButton(16, "Login", 1, 0);

		c.gridwidth = 2;
		if (role.equalsIgnoreCase("manager")) {
			panel.addNavigationButton("Reservations", 16, "Reservations", 0, 1);
			panel.addNavigationButton("Room Service", 16, "Room Service", 0, 2);
			panel.addNavigationButton("Feedback", 16, "Feedback", 0, 3);
			panel.addNavigationButton("Employees", 16, "Employees", 0, 4);
			panel.addNavigationButton("Customers", 16, "Customers", 0, 5);
			panel.addNavigationButton("Statistics", 16, "Statistics", 0, 5);
			panel.addNavigationButton("Archive Database", 16, "Archive", 0, 5);
		}
		else if (role.equalsIgnoreCase("customer")) {
			panel.addNavigationButton("Book a reservation", 16, "Book", 0, 1);
			panel.addNavigationButton("View/Cancel Reservations", 16, "View/Cancel", 0, 2);
			panel.addNavigationButton("Order Room Service", 16, "Order Room Service", 0, 3);
			panel.addNavigationButton("Give Feedback", 16, "Give Feedback", 0, 4);
		}
		else if (role.equalsIgnoreCase("room service")) {
			panel.addNavigationButton("Tasks", 16, "Room Service", 0, 1);
			panel.addNavigationButton("Customers", 16, "Customers", 0, 1);
		}
		else {
			panel.addNavigationButton("Reservations", 16, "Reservations", 0, 1);
			panel.addNavigationButton("Room Service", 16, "Room Service", 0, 2);
			panel.addNavigationButton("Customers", 16, "Customers", 0, 3);
			panel.addNavigationButton("Check Out", 16, "Check out", 0, 4);
		}

		return panel;
	}

	private JPanel getMakeReservationPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.gridwidth = 2;
		c.ipady = 15;
		panel.addLabel("Reserve a Room", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(10, 10, 10, 10);
		c.gridwidth = 1;
		c.ipady = 0;
		panel.addLabel("Check-in (MM/DD/YYYY):", 12, "left", null, null, 0, 1);
		panel.addLabel("Check-out (MM/DD/YYYY):", 12, "left", null, null, 1, 1);

		c.gridwidth = 1;
		final JTextField checkIn = new JTextField();
		panel.addComponent(checkIn, 0, 2);

		final JTextField checkOut = new JTextField();
		panel.addComponent(checkOut, 1, 2);

		c.gridwidth = 2;
		c.weighty = 1;
		@SuppressWarnings({ "rawtypes"})
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		panel.addComponent(list);
		JScrollPane listScroller = new JScrollPane(list);
		panel.addComponent(listScroller, 0, 4);
		
		c.weighty = 0;
		
		JButton searchBtn = new JButton("Search for rooms");
		searchBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		searchBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override() 
			public void actionPerformed(ActionEvent e) {
				list.setListData(new Object[1]);
				String in = checkIn.getText();
				String out = checkOut.getText();
				GregorianCalendar inCal;
				GregorianCalendar outCal;
				if (in.length() == 10 && out.length() == 10) {
					inCal = isValidDateFormat(in);
					outCal = isValidDateFormat(out);
					if (inCal != null && outCal != null) {
						if (inCal.before(Model.TODAY) || outCal.before(Model.TODAY))
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Date(s) prior to today.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else if (outCal.before(inCal))
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Check-in after check-out.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else if (checkDaysBetween(inCal, outCal) > 60)
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Reservation cannot be longer than 60 days.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else {
							if (model.getAvailRooms(in, out) != null)
								list.setListData(model.getAvailRooms(in, out).toArray());
						}
					}
					else 
						JOptionPane.showMessageDialog(new JFrame(),
								"Error: Invalid formats.", "Error",
								JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.addComponent(searchBtn, 0, 3);

		c.gridwidth = 1;
		JButton confirmBtn = new JButton("Confirm");
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Room room = (Room)list.getSelectedValue();
				if (room != null) {
					if (model.addReservation(room.getRoomId(), checkIn.getText(), checkOut.getText())) {
						int response = JOptionPane.showConfirmDialog(
								new JFrame(), "Your reservation has been saved.<br>"
										+ "Would you like to make more transactions?</html>",
										"Confirmation", JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE);
						if (response == JOptionPane.NO_OPTION) switchPanel("Receipt");
						if (response == JOptionPane.YES_OPTION) ;
						panel.clearComponents();
					}
				}
				else
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: No room has been selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		panel.addComponent(confirmBtn, 0, 5);

		JButton doneBtn = new JButton("Transaction Done");
		doneBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getReservations().isEmpty()) 
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: No reservations have been made.", "Error",
							JOptionPane.ERROR_MESSAGE);
				else {
					panel.clearComponents();
					switchPanel("Receipt");
				}
			}
		});
		panel.addComponent(doneBtn, 1, 5);

		c.gridwidth = 2;
		panel.addNavigationButton("Back", 12, "Customer", 0, 6);
		
		return panel;
	}

	private JPanel getReceiptPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
	
		c.gridwidth = 1;
		c.ipady = 30;
		panel.addLabel("Receipt", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);
		
		c.ipady = 0;
		c.weighty = 1;
		c.insets = new Insets(10,10,10,10);
		final JTextArea receipt = new JTextArea();
		receipt.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(receipt,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) receipt.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		panel.addComponent(scrollPane, 0, 2);

		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!model.getReservations().isEmpty()) {
					Account user = model.getCurrentUser();
					String text = "Username: " + user.getUsername() + "\nName: " + user.getFirstName() 
					+ " " + user.getLastName() + "\n";
					
					double cost = 0;
					int i = 1;
					for (Reservation r : model.getReservations()) {
						text += String.format("\n\nReservation # %d\n%s", i, r.toString());
						cost += r.getTotalCost();
						i++;
					}
					
					text += String.format("\n\nTotal: $%.2f", cost);
					
					receipt.setText(text);
				}
			};
		});

		c.weighty = 0;
		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.clearResrvations();
				panel.clearComponents();
				view.switchPanel("Customer");
			}
		});
		panel.addComponent(backBtn, 0, 3);
		return panel;
	}
	
	private GregorianCalendar isValidDateFormat(String input) {
		try {
			dateFormatter.setLenient(false);
			GregorianCalendar cal = new GregorianCalendar();
			Date d = dateFormatter.parse(input);
			cal.setTime(d);

			return cal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private int checkDaysBetween(GregorianCalendar checkIn, GregorianCalendar checkOut) {
		GregorianCalendar temp = (GregorianCalendar) checkIn.clone();
		int count = 0;
		while (!temp.equals(checkOut)) {
			temp.add(Calendar.DATE, 1);
			count++;
		}
		return count;
	}
	
	private JPanel getGiveFeedbackPanel() {
	
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
	
		c.weighty = 1;
		c.gridwidth = 1;

		panel.addLabel("complaint", 16, "left", null, null, 0, 1);

		final JTextField complaint = new JTextField();
		panel.addComponent(complaint, 0, 2);

		c.weighty = 0;
		c.gridwidth = 0;
		panel.addNavigationButton("Back", 16, "Customer", 0, 3);
		
		
		return panel;
	}
}