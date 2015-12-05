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
	private int roomId;
	private Date startDate;
	private Date endDate;
	private int numOfDays;
	private double totalCost;	

	public Reservation(int reservationId, int roomId, Date startDate, Date endDate, int numOfDays, double totalCost) {
		this.reservationId = reservationId;
		this.roomId = roomId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.numOfDays = numOfDays;
		this.totalCost = totalCost;
	}

	public int getReservationId() {
		return reservationId;
	}

	public int getRoomId() {
		return roomId;
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
	
	
}
