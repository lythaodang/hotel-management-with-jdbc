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

	private ArrayList<ChangeListener> listeners;

	// variables used for the transaction
	private Account currentUser;
	private String currentRole;
	private ArrayList<Reservation> reservations;

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
		reservations = new ArrayList<Reservation>();
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
			setCurrentUser(username);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addReservation(int roomId, String checkIn, String checkOut) {
		String query = String.format("insert into reservation(roomId, customer, startDate, endDate) values ('%s','%s',%s,%s)",
				roomId, currentUser.getUsername(), toDateSQL(checkIn), toDateSQL(checkOut));
		try {
			statement.execute(query);
			setCurrentUser(currentUser.getUsername());
			ArrayList<Reservation> res = currentUser.getReservations();
			reservations.add(res.get(res.size() - 1));
			update();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void setCurrentUser(String username) {
		if (username == null) {
			currentUser = null;
			currentRole = null;
			return;
		}
		
		String queryUser = "SELECT username, firstname, lastname, userrole FROM USER WHERE username = '" + username + "'";

		try {
			String role;

			ResultSet rs = statement.executeQuery(queryUser);
			if (rs.next()) {
				setCurrentRole(role = rs.getString("userrole"));
				currentUser = new Account(rs.getString("firstname"), rs.getString("lastname"), rs.getString("username"), role);
				update();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String queryRes = "select reservationId, room.roomId, startDate, endDate, numOfDays, totalCost, costpernight, roomtype "
				+ "from room left outer join reservation on room.roomid = reservation.roomid "
				+ "where customer ='" + username + "'";
		try {
			ResultSet rs = statement.executeQuery(queryRes);
			if (rs.next()) {
				Room r = new Room(rs.getInt("roomid"), rs.getDouble("costPerNight"), rs.getString("roomtype"));
				currentUser.getReservations().add(new Reservation(rs.getInt("reservationid"), r, 
						rs.getDate("startdate"), rs.getDate("enddate"), rs.getInt("numOfDays"), rs.getDouble("totalCost")));
				update();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		
	}
	
	public boolean checkUserExistence(String username) {
		String query = "SELECT userName FROM USER";

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				if (rs.getString("username").equals(username)) return true;
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
			if (rs.next() && rs.getString("password").equals(password)) return true;
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
				return rs.getString("question");
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
			if (rs.next() && rs.getString("answer").equals(answer)) {
				return rs.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<Room> getAvailRooms(String in, String out) {
		ArrayList<Room> rooms = new ArrayList<>();
		String checkIn = toDateSQL(in);
		String checkOut = toDateSQL(out);
		String query = "select * from room where roomId not in "
				+ "(select distinct room.roomId "
				+ "from room left outer join reservation on room.roomId = reservation.roomId "
				+ "where " + checkIn + " = reservation.startdate"
				+ " or " + checkIn + " = reservation.enddate"
				+ " or " + checkOut + "= reservation.startdate"
				+ " or " + checkOut + " = reservation.enddate"
				+ " or " + "(reservation.startdate < " + checkOut + " and reservation.enddate > " + checkIn + ")"
				+ " or (" + checkIn + " < reservation.startdate and " + checkOut + " > reservation.startdate)"
				+ " or (" + checkIn + " < reservation.enddate and " + checkOut + " > reservation.enddate))";

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				rooms.add(new Room(rs.getInt("roomid"), rs.getDouble("costpernight"), rs.getString("roomtype")));
			}
			return rooms;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public String toDateSQL(String date) {
		return "str_to_date('" + date + "', '%m/%d/%Y')";
	}
	
	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String role) {
		currentRole = role;
		update();
	}
	
	public Account getCurrentUser() {
		return currentUser;
	}
	
	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void clearResrvations() {
		reservations = new ArrayList<Reservation>();
	}
	
	/**
	 * Adds the changelisteners
	 * @param accounts the accounts to set
	 */
	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Notifies the observers that there has been a change.
	 */
	private void update() {
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
	}
}
