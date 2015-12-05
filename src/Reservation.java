import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * Reservation for a room.
 */
public class Reservation {
	private int reservationId;
	private String username;
	private Room room;
	private Date startDate;
	private Date endDate;
	private int numOfDays;
	private double totalCost;	
	private boolean canceled;

	public Reservation(int reservationId, String username, Room room, Date startDate, Date endDate, int numOfDays, double totalCost) {
		this.reservationId = reservationId;
		this.username = username;
		this.room = room;
		this.startDate = startDate;
		this.endDate = endDate;
		this.numOfDays = numOfDays;
		this.totalCost = totalCost;
		this.canceled = false;
	}

	public int getReservationId() {
		return reservationId;
	}

	public Room getRoom() {
		return room;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getNumOfDays() {
		return numOfDays;
	}

	public double getTotalCost() {
		return totalCost;
	}
	
	public String getCustomer() {
		return username;
	}
	
	public boolean getCanceled() {
		return canceled;
	}
	
	public void setCanceled() {
		canceled = true;
	}
	
	/**
	 * String representation of reservation information
	 * @return the reservation information
	 */
	public String toString() {
		return String.format("%s \n%s to %s \nCost: %d days X $%.2f a night = "
				+ "$%.2f", room.toString(), 
				new SimpleDateFormat("MM/dd/yyyy").format(startDate),
				new SimpleDateFormat("MM/dd/yyyy").format(endDate),
				numOfDays, room.getCostPerNight(), numOfDays * room.getCostPerNight());
	}
}
