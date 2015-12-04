package Fix;
import java.io.Serializable;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * The representation of a luxury room.
 */
@SuppressWarnings("serial")
public class LuxuryRoom implements Room, Serializable {
	private final double COST = 200;
	private final int roomNumber;

	/**
	 * Constructs the room with a room number.
	 * @param roomNumber the room number
	 */
	public LuxuryRoom(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public int getRoomNumber() {
		return roomNumber;
	}

	@Override
	public double getCost() {
		return COST;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " #" + roomNumber;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.hashCode() == obj.hashCode()) return true;
		else return false;
	}
}
