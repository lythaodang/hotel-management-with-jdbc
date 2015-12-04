package Fix;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Account;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * Reservation for a room, detailing the time it starts and ends, along with the
 * user who reserved it.
 */
@SuppressWarnings("serial")
public class Reservation implements Serializable {
	private final Room room;
	private final GregorianCalendar start;
	private final GregorianCalendar end;
	private final Account user;
	private final int days;

	/**
	 * Constructor for the reservation, detailing the start, end, and userid.
	 * @param start Start date of the reservation.
	 * @param end End date of the reservation.
	 * @param user ID number of the user who made the reservation.
	 */
	public Reservation(Room room, GregorianCalendar start,
			GregorianCalendar end, Account user) {
		this.room = room;
		this.start = start;
		this.end = end;
		this.user = user;
		days = calculateDays();
	}

	/**
	 * Calculates the number of nights
	 * @return the number of days calculated
	 */
	private int calculateDays() {
		GregorianCalendar temp = (GregorianCalendar) start.clone();
		int count = 0;
		while (!temp.equals(end)) {
			temp.add(Calendar.DATE, 1);
			count++;
		}
		return count;
	}

	/**
	 * Gets the room
	 * @return the Room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Gets the cost
	 * @return the cost
	 */
	public double getCost() {
		return days * room.getCost();
	}

	/**
	 * Gets the days
	 * @return the days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * Return the date that the reservation starts.
	 * @return GregorianCalendar of the start date
	 */
	public GregorianCalendar getStart() {
		return start;
	}

	/**
	 * Return the date that the reservation ends.
	 * @return GregorianCalendar of the end date.
	 */
	public GregorianCalendar getEnd() {
		return end;
	}

	/**
	 * Return the user who reserved the room.
	 * @return ID of the user who made the reservation.
	 */
	public Account getUser() {
		return user;
	}

	/**
	 * String representation of reservation information
	 * @return the reservation information
	 */
	public String toString() {
		return String.format("%s \n%s to %s \nCost: %d days X $%.2f a night = "
				+ "$%.2f", room.toString(), 
				new SimpleDateFormat("MM/dd/yyyy").format(start.getTime()),
				new SimpleDateFormat("MM/dd/yyyy").format(end.getTime()),
				days, room.getCost(), days * room.getCost());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.hashCode() == obj.hashCode()) return true;

		return false;
	}
}
