import java.text.SimpleDateFormat;
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
public class Complaint {
	private final int id;
	private final String customer;
	private final String complaint;
	private final Date time;
	private final String resolvedBy;
	private final String solution;

	public Complaint(int id, String customer, String complaint, Date time, 
					String resolvedBy, String solution) {
		this.id = id;
		this.customer = customer;
		this.complaint = complaint;
		this.time = time;
		this.resolvedBy = resolvedBy;
		this.solution = solution;
	}

	public int getId() {
		return id;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public String getComplaint() {
		return complaint;
	}
	
	public Date getTime() {
		return time;
	}
	
	public String getResolvedBy() {
		return resolvedBy;
	}
	
	public String getSolution() {
		return solution;
	}
	
	public String toString() {
		String s = "Username: " + customer +"\nComplaint ID: " + id + "\nFiled on: " + 
	new SimpleDateFormat("MM/dd/yyyy").format(time) + "\nComplaint: " + complaint;
		if (resolvedBy != null)
			s += "\nResolved By: " + resolvedBy + "\nSolution: " + solution;
		return s;
	}
}
