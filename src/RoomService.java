import java.util.Date;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * A user's Complaint.
 */
public class RoomService {

	private final String task;
	private final String customer;
	private final int roomID;
	private final String completedBy;
	private final int reservationID;
	private final Date time;
	private final int cost;

	public RoomService(String task, String customer, int roomID, 
			String completedBy, int reservationID, Date time,int cost) {

		this.task = task;
		this.customer = customer;
		this.roomID = roomID;
		this.completedBy = completedBy;
		this.reservationID = reservationID;
		this.time = time;
		this.cost = cost;
		
	}

	public String getTask() {
		return task;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public int getRoomID() {
		return roomID;
	}
	
	public String getCompletedBy() {
		return completedBy;
	}
	
	public int getReservationID() {
		return reservationID;
	}
	
	public Date getTime() {
		return time;
	}
	
	public int getCost() {
		return cost;
	}
	
}
