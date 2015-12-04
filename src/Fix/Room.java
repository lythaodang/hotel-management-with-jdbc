package Fix;
/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * The interface of a room.
 */
public interface Room {
	/**
	 * Gets the room number.
	 * @return the room number
	 */
	int getRoomNumber();

	/**
	 * Gets the cost
	 * @return the cost
	 */
	double getCost();

	/**
	 * Gets the string representation of the Room.
	 * @return the room type followed by the room number
	 */
	String toString();
}