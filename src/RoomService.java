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
	private final int taskId;
	private final String task;
	private final int roomID;
	private final Date time;

	public RoomService(int taskId, String task, int roomID, Date time) {
		this.taskId = taskId;
		this.task = task;
		this.roomID = roomID;
		this.time = time;
		
	}
	public int getTaskId() {
		return taskId;
	}
	
	public String getTask() {
		return task;
	}
	
	public int getRoomID() {
		return roomID;
	}
	
	public Date getTime() {
		return time;
	}
	
	public String toString() {
		return "Task ID: " + taskId + "\nRoom ID: " + roomID + "\nOrdered on: " + time.toString() + "\nTask: " + task; 
	}
}
