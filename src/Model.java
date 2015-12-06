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
				roomId, currentUser.getUsername(), sqlToDate(checkIn), sqlToDate(checkOut));
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

	public boolean cancelReservation(Reservation r) {
		String query = "update reservation set canceled = true where reservationid = " + r.getReservationId(); 
		try {
			statement.execute(query);
			setCurrentUser(currentUser.getUsername());
			update();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean checkOutReservation(Reservation r) {
		String query = "update reservation set paid = true and keyreturned = true where reservationid = " + r.getReservationId(); 
		try {
			statement.execute(query);
			setCurrentUser(currentUser.getUsername());
			update();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<Reservation> getAllReservations() {
		ArrayList<Reservation> resList = new ArrayList<Reservation>();

		String queryRes = "select canceled, customer, reservationId, room.roomId, startDate, endDate, numOfDays, totalCost, costpernight, roomtype "
				+ "from room right outer join reservation on room.roomid = reservation.roomid ";
		try {
			ResultSet rs = statement.executeQuery(queryRes);
			while (rs.next()) {
				Room room = new Room(rs.getInt("roomid"), rs.getDouble("costPerNight"), rs.getString("roomtype"));
				Reservation res = new Reservation(rs.getInt("reservationid"), rs.getString("customer"), room, 
						rs.getDate("startdate"), rs.getDate("enddate"), rs.getInt("numOfDays"), rs.getDouble("totalCost"),
						rs.getBoolean("canceled"));
				resList.add(res);
			}
			rs.close();
			return resList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Reservation> getCheckOut() {
		ArrayList<Reservation> resList = new ArrayList<Reservation>();

		String queryRes = "select canceled, customer, reservationId, room.roomId, startDate, endDate, numOfDays, totalCost, costpernight, roomtype "
				+ "from room right outer join reservation on room.roomid = reservation.roomid where (now() + interval 20 day between startdate and enddate) and canceled <> true";
		try {
			ResultSet rs = statement.executeQuery(queryRes);
			while (rs.next()) {
				Room room = new Room(rs.getInt("roomid"), rs.getDouble("costPerNight"), rs.getString("roomtype"));
				Reservation res = new Reservation(rs.getInt("reservationid"), rs.getString("customer"), room, 
						rs.getDate("startdate"), rs.getDate("enddate"), rs.getInt("numOfDays"), rs.getDouble("totalCost"),
						rs.getBoolean("canceled"));
				resList.add(res);
			}
			rs.close();
			return resList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Reservation> getReservations(String orderBy, Double min, Double max) {
		ArrayList<Reservation> resList = new ArrayList<Reservation>();

		String queryRes = "select canceled, customer, reservationId, room.roomId, startDate, endDate, numOfDays, totalCost, costpernight, roomtype "
				+ "from room right outer join reservation on room.roomid = reservation.roomid";

		if (min != null) {
			queryRes += " having totalCost >= " + min;
			if (max != null)
				queryRes += " and totalCost <= " + max;
		}
		else
			if (max != null)
				queryRes += " having totalCost <= " + max;

		queryRes += " order by " + orderBy;

		try {
			ResultSet rs = statement.executeQuery(queryRes);
			while (rs.next()) {
				Room room = new Room(rs.getInt("roomid"), rs.getDouble("costPerNight"), rs.getString("roomtype"));
				Reservation res = new Reservation(rs.getInt("reservationid"), rs.getString("customer"), room, 
						rs.getDate("startdate"), rs.getDate("enddate"), rs.getInt("numOfDays"), rs.getDouble("totalCost"),
						rs.getBoolean("canceled"));
				resList.add(res);
			}
			rs.close();
			return resList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setCurrentUser(String username) {
		if (username == null) {
			currentUser = null;
			currentRole = null;
		}
		else {
			currentUser = getAccount(username);
			currentRole = currentUser.getRole();
		}
		update();
	}

	public Account getAccount(String username) {
		Account acc = null;
		String queryAccount = "SELECT firstname, lastname, username, userrole "
				+ "FROM USER WHERE username = '" + username + "'"; 
		String queryRes = "select customer, canceled, reservationId, room.roomId, "
				+ "startDate, endDate, numOfDays, totalCost, costpernight, roomtype "
				+ "from room right outer join reservation on room.roomid = reservation.roomid "
				+ "where customer ='" + username + "'";
		try {
			ResultSet rs = statement.executeQuery(queryAccount);
			while (rs.next()) {
				acc = new Account(rs.getString("firstname"), rs.getString("lastname"), 
						rs.getString("username"), rs.getString("userrole"));
			}
			rs.close();
			rs = statement.executeQuery(queryRes);
			while (rs.next()) {
				Room r = new Room(rs.getInt("roomid"), rs.getDouble("costPerNight"), rs.getString("roomtype"));
				acc.getReservations().add(new Reservation(rs.getInt("reservationid"), rs.getString("customer"), r, 
						rs.getDate("startdate"), rs.getDate("enddate"), rs.getInt("numOfDays"), rs.getDouble("totalCost"), 
						rs.getBoolean("canceled")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return acc;
	}

	public ArrayList<Account> getUsers(Integer numOfReservations) {
		ArrayList<Account> list = new ArrayList<Account>();
		String queryUser = "";
		ArrayList<String> usernames = new ArrayList<String>();
	
		if (numOfReservations == null)
			queryUser = "SELECT username from user";
		else
			queryUser = "select username FROM USER right outer join "
					+ "(select customer, reservationId, room.roomId, startDate, "
					+ "endDate, numOfDays, totalCost, costpernight, roomtype "
					+ "from room right outer join reservation on room.roomid = reservation.roomid "
					+ "group by customer having count(*) >= " + numOfReservations + ") as reservations "
					+ "on user.username = reservations.customer";

		try {
			ResultSet rs = statement.executeQuery(queryUser);
			while (rs.next()) 
				usernames.add(rs.getString("username"));
			rs.close();
			
			for (String s : usernames) 
				list.add(getAccount(s));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}

	public boolean checkUserExistence(String username) {
		String query = "SELECT userName FROM USER";

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				if (rs.getString("username").equals(username)) return true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean checkUserPassword(String username, String password) {
		String query = "SELECT password FROM USER WHERE userName = '" + username + "'";

		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next() && rs.getString("password").equals(password)) {
				rs.close();
				return true;
			}
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
			rs.close();
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
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<Room> getAvailRooms(String in, String out) {
		ArrayList<Room> rooms = new ArrayList<>();
		String checkIn = sqlToDate(in);
		String checkOut = sqlToDate(out);
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
			rs.close();
			return rooms;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean addComplaint(String customer, String complaintTest) {	
		String query = String.format("INSERT INTO COMPLAINT(customer,complaint)"
				+ " VALUES('%s','%s')", 
				currentUser.getUsername(), complaintTest);

		try {
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<Complaint> getComplaints() {
		ArrayList<Complaint> complaints = new ArrayList<Complaint>();
		String query = "SELECT * FROM COMPLAINT";

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				complaints.add(new Complaint(rs.getInt("complaintId"), rs.getString("customer"),rs.getString("complaint"),
						rs.getDate("time"),rs.getString("resolvedBy"),rs.getString("solution")));
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return complaints;
	}
	
	public Complaint getComplaint(int id) {
		String query = "SELECT * FROM COMPLAINT where complaintid = " + id;
		Complaint c = null;
		
		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) 
				c = new Complaint(rs.getInt("complaintId"), rs.getString("customer"),rs.getString("complaint"),
						rs.getDate("time"),rs.getString("resolvedBy"),rs.getString("solution"));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return c;
	}
	
	public boolean updateComplaint(int id, String resolvedBy, String solution) {
		String query = "update complaint set resolvedBy = '" + resolvedBy 
				+ "', solution = '" + solution + "' where complaintId = " + id;
		try {
			statement.execute(query);
			update();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getStatistics() {
		String avgAge = "select avg(age) from user", 
		avgNumRes = "select avg(rescount) from (select count(*) as rescount from reservation group by customer) counts",
		avgCost = "select avg(totalcost) from reservation", result = "";
		
		try {
			ResultSet rs = statement.executeQuery(avgAge);
			if (rs.next())
				result += "Average age of users: " + rs.getString(1);
			rs.close();
			
			rs = statement.executeQuery(avgNumRes);
			if (rs.next())
				result += "\nAverage number of reservations per customer: " + rs.getString(1);
			rs.close();
			
			rs = statement.executeQuery(avgCost);
			if (rs.next())
				result += "\nAverage cost of a reservation " + rs.getString(1);
			rs.close();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean addRoomService(Reservation r, String task, double cost) {	
		String query = String.format("INSERT INTO ROOMSERVICE(task,roomId,reservationid,cost)"
				+ " VALUES('%s','%d','%d','%f')", 
				task, r.getRoom().getRoomId(), r.getReservationId(), cost);

		try {
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/*
	public ArrayList<RoomService> getRoomService() {
		String query = "select * from roomservice where now() + interval 20 day between startdate and enddate";
		ArrayList<RoomService> roomservice = new ArrayList<RoomService>();
		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				if (rs.getString("resolved")))
				roomservice.add(new RoomService(rs.getString("task"), rs.getString(columnIndex), roomID, completedBy, reservationID, time, cost))
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	*/
	public boolean archive(String date) {
		return false;
	}
	
	public String sqlToDate(String date) {
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
