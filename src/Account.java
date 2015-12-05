import java.util.ArrayList;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * A user's account.
 */
public class Account {
	final private String username; // cannot be changed once account is created
	private String firstName;
	private String lastName;
	private String role;
	private ArrayList<Reservation> reservations;

	public Account(String firstName, String lastName, String username, String role) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		reservations = new ArrayList<Reservation>();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getRole() {
		return role;
	}
	
	public ArrayList<Reservation> getReservations() {
		return reservations;
	}
}
