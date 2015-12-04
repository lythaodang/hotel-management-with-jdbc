package Fix;
import Account;
import Receipt;
import Reservation;

import java.util.ArrayList;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * Implements the Receipt interface, generating a simple receipt that details
 * the user's stay and amount to pay in length.
 */
public class SimpleReceipt implements Receipt {
	private ArrayList<Reservation> transaction;
	private static double TAX = .0875;

	/**
	 * Takes in all reservations made in the current transaction. This receipt
	 * will only display reservations made in current transaction.
	 * @param transaction the reservations made in current transaction
	 */
	public SimpleReceipt(ArrayList<Reservation> transaction) {
		this.transaction = transaction;
	}

	/**
	 * Generates a formatted receipt in simple form.
	 * @return simple receipt.
	 */
	public String toString() {
		if (transaction.isEmpty())
			return "";
		
		Account user = transaction.get(0).getUser();
		String receipt = String.format("Name: %s\nUser ID: %s", user.getName(), 
				user.getUser());

		double cost = 0;
		int i = 1;
		for (Reservation r : transaction)
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
