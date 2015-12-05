/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

public class Room {
	private int roomId;
	private double costPerNight;
	private String roomType;
	
	public Room(int roomId, double costPerNight, String roomType) {
		this.roomId = roomId;
		this.costPerNight = costPerNight;
		this.roomType = roomType;
	}
	
	public int getRoomId() {
		return roomId;
	}
	
	public double getCostPerNight() {
		return costPerNight;
	}
	
	public String getRoomType() {
		return roomType;
	}
	
	public String toString() {
		return roomType + "\n" + "$" + costPerNight + " per night";
	}
}
