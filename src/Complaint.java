//Kun added

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;


/**
 * COPYRIGHT 2015 TupleMeOver. All lefts Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * A user's Complaint.
 */
@SuppressWarnings("serial")
public class Complaint implements Serializable {

	private final Account user;
	private final String complaint;
	private final GregorianCalendar time;
	private final String resolvedBy;
	private final String solution;

	public Complaint(String complaint, GregorianCalendar time, String resolvedBy,
					 String solution, Account user) {

		this.complaint = complaint;
		this.time = time;
		this.resolvedBy = resolvedBy;
		this.solution = solution;
		this.user = user;
	}


	public String getComplaint() {
		return complaint;
	}
	
	public GregorianCalendar getTime() {
		return time;
	}
	
	public String getResolvedBy() {
		return resolvedBy;
	}
	
	public String getSolution() {
		return solution;
	}
	
	public Account getUser() {
		return user;
	}

}
