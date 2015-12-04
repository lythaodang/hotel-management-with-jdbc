import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * This is the database of the hotel. It holds rooms, accounts, and current
 * user.
 */
public class Model {
	public static final GregorianCalendar TODAY = new GregorianCalendar();
	private static final double TAX = .0875;

	private ArrayList<ChangeListener> listeners;

	// variables used for the transaction
	private Account currentUser;
	private String currentRole;

	// variables used for manager
	private Connection connection = JDBCUtil.getConnectionByDriverManager();
	private Statement statement = JDBCUtil.getStatement(connection);

	/**
	 * Constructs the database. Loads the serialized accounts and reservations.
	 */
	public Model() {
		TODAY.clear(Calendar.HOUR);
		TODAY.clear(Calendar.MINUTE);
		TODAY.clear(Calendar.SECOND);
		TODAY.clear(Calendar.MILLISECOND);

		listeners = new ArrayList<>();
		currentUser = null;
		currentRole = null;
	}

	/**
	 * Adds the changelisteners
	 * @param accounts the accounts to set
	 */
	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Add an account to the database.
	 * @param account the account to add
	 */
	public boolean addAccount(String username, String password, String firstName, String lastName, int age, String gender, String role, String secQuestion, String secAnswer) {		
		username = username.replace("'", "''");
		password = password.replace("'", "''");
		firstName = firstName.replace("'", "''");
		lastName = lastName.replace("'", "''");
		secQuestion = secQuestion.replace("'", "''");
		secAnswer = secAnswer.replace("'", "''");
		
		String query = String.format("INSERT INTO USER(userName,password,firstName,lastName,age,gender,userRole,question,answer)"
				+ " VALUES('%s','%s','%s','%s',%s,'%s','%s','%s', '%s')", 
				username, password, firstName, lastName, age, gender, role, secQuestion, secAnswer);

		try {
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Account getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(String username) {
		String query = "SELECT username, firstname, lastname, userrole FROM USER WHERE username = '" + username + "'";

		try {
			String userName, firstName, lastName, role;

			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				userName = rs.getString(1);
				firstName = rs.getString(2);
				lastName = rs.getString(3);
				role = rs.getString(4);
				setCurrentRole(role);
				currentUser = new Account(firstName, lastName, userName, role);
				update();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The current user's role.
	 * @return role customer, manager, room service, or receptionist
	 */
	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String role) {
		currentRole = role;
		update();
	}
	
	public boolean checkUserExistence(String username) {
		String query = "SELECT userName FROM USER";

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				if (rs.getString(1).equals(username)) return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean checkUserPassword(String username, String password) {
		String query = "SELECT password FROM USER WHERE userName = '" + username + "'";

		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next() && rs.getString(1).equals(password)) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; 
	}

	public String getUserSecurityQuestion(String username) {
		String query = "SELECT question FROM USER WHERE username = '" + username + "'";

		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String checkSecurityAnswer(String username, String answer) {
		String query = "SELECT answer,password FROM USER WHERE username = '" + username + "'";

		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next() && rs.getString(1).equals(answer)) {
				return rs.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Notifies the observers that there has been a change.
	 */
	private void update() {
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
	}
	
	/*
	public ArrayList<Room> getAvailRooms(String in, String out) {
		ArrayList<Room> rooms = new ArrayList<>();
		String query = "SELECT * FROM ROOM WHERE  = '" + username + "'";

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next() && rs.getString(1).equals(answer)) {
				rooms.add(new Room(String.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	*/
}
