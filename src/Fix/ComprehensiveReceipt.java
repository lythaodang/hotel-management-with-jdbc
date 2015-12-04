package Fix;
import Account;
import Receipt;
import Reservation;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * Implements the Receipt interface, generating a comprehensive receipt that details the
 * user's stay and amount to pay in length.
 */
public class ComprehensiveReceipt implements Receipt
{
	private Account user;
	private static double TAX = .0875;

	/**
	 * Takes in an account. 
	 * This receipt will display all reservations made by the user.
	 * @param user the current user's account
	 */
	public ComprehensiveReceipt(Account user)
	{
		this.user = user;
	}
	
	/**
	 * Sets the user
	 * @param user the user
	 */
	public void setUser(Account user)
	{
		this.user = user;
	}
	
	/**
	 * Generates a formatted receipt in comprehensive form.
	 * @return Comprehensive receipt.
	 */
	public String toString()
	{
		if (user == null)
			return "No current user";
		
		String receipt = String.format("Name: %s\nUser ID: %s", user.getName(), user.getUser());
		
		double cost = 0;
		int i = 1;
		for (Reservation r : user.getReservations())
		{
			receipt += String.format("\n\nReservation # %d\n%s", i, r.toString());
			cost += r.getCost();
			i++;
		}
		
		receipt += String.format("\n\nSubtotal: $%.2f\nTax: $%.2f\nTotal: $%.2f",
				cost, cost * TAX, cost * TAX + cost);
		
		return receipt;
	}
}
