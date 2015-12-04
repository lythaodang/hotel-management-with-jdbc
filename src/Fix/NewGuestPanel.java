package Fix;
import Account;
import BasicPanel;
import View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * A panel in which the user can create an account.
 */
@SuppressWarnings("serial")
public class NewGuestPanel extends BasicPanel {

	/**
	 * Constructs the panel in which the user can create an account.
	 * @param m the view manager
	 */
	public NewGuestPanel(View m) {
		super(m);

		c.gridwidth = 2;
		addInstructions("<html>Fill out the following information."
				+ "<br>Your user ID should be at least 6 characters and at most 12 "
				+ "characters. Your password must be at least 8 characters. "
				+ "Your first and last name should not exceed 15 "
				+ "characters.</html>");
		
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		addLabel("First name: ", 0, 1);
		addLabel("Last name: ", 0, 2);
		addLabel("User ID: ", 0, 3);
		addLabel("Password: ", 0, 4);
		addLabel("Age: ", 0, 5);
		addLabel("Gender: ", 0, 6);
		addLabel("Phone: ", 0, 7);
		addLabel("Address: ", 0, 8);
		addLabel("Security Question: ", 0, 9);
		addLabel("Security Answer: ", 0, 10);
		
		final JTextField firstName = new JTextField();
		addComponent(firstName, 1, 1);

		final JTextField lastName = new JTextField();
		addComponent(lastName, 1, 2);

		final JTextField userID = new JTextField();
		addComponent(userID, 1, 3);
		
		final JPasswordField password = new JPasswordField();
		addComponent(password, 1, 4);
		
		final JTextField birthDate = new JTextField();
		addComponent(birthDate, 1, 5);
		
		final JTextField gender = new JTextField();
		addComponent(gender, 1, 6);
		
		final JTextField phone = new JTextField();
		addComponent(phone, 1, 7);
		
		final JTextField address = new JTextField();
		addComponent(address, 1, 8);
		
		final JTextField securityQuestion = new JTextField();
		addComponent(securityQuestion, 1, 9);
		
		final JTextField securityAnswer = new JTextField();
		addComponent(securityAnswer, 1, 10);

		addNavigationButton("Back", "Initial", 0, 11);

		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String first = firstName.getText();
				String last = lastName.getText();
				String id = userID.getText();
				String pass = new String(password.getPassword()); 
				Integer age = Integer.parseInt(birthDate.getText());
				String gen = gender.getText();
				String fone = phone.getText();
				String addr = address.getText();
				String secQuestion = securityQuestion.getText();
				String secAnswer = securityAnswer.getText();
				
				if (validEntry(first, last, pass, id)) {
					//Account newAccount = new Account(first + " " + last, id);
					Account newAccount = new Account(first + " " + last, id, pass, 
							                         age, gen, fone, addr, "customer", secQuestion, secAnswer);
					clearComponents();
					model.addAccount(newAccount);
					model.setCurrentUser(newAccount);
					manager.switchPanel("GMenu");
				}
			}
		});
		addComponent(submit, 1, 11);
	}

	/**
	 * Checks if entry is valid.
	 * @param first the first name
	 * @param last the last name
	 * @param id the user id
	 * @return true if input is valid else false
	 */
	private boolean validEntry(String first, String last, String pass, String id) {
		
		if (first.length() < 1 || last.length() < 1 || id.length() < 1) {
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: One or more fields not entered.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (first.length() > 15 || last.length() > 15) {
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: First and/or last name(s) too long.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (pass.length() < 8) {
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: Password is less than 8 characters.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (id.length() < 6 || id.length() > 12) {
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: ID is less than 6 or more than 12 characters.", "Error",
					JOptionPane.ERROR_MESSAGE);
		//} else if (model.findUser(id) != null) {
		} else if (model.checkUserExistence(id)) {
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: ID taken.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else return true;
		return false;
	}
}
