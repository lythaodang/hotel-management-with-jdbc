
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
}
