package Fix;
import BasicPanel;
import View;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * The panel in which the guest can choose to make, view, and cancel reservations.
 */
@SuppressWarnings("serial")
public class GuestMenuPanel extends BasicPanel
{
	/**
	 * Constructor for GuestMenuPanel
	 * @param m the view
	 */
	public GuestMenuPanel(View m)
	{
		super(m);
		
		c.weighty = 1;
		
		final JLabel name = new JLabel();
		model.addChangeListener(new
				ChangeListener()
				{
					@Override
					public void stateChanged(ChangeEvent event)
					{
						if (model.getCurrentUser() != null)
							name.setText("<html>User:<br>" + model.getCurrentUser().getName());
					}
				});
		addComponent(name, 0, 0);
		
		addSignOutButton("Initial", 1, 0);
		
		c.gridwidth = 2;
		addNavigationButton("Make a Reservation", "Make Reservation", 0, 1);
		addNavigationButton("View/Cancel a Reservation", "View/Cancel", 0, 2);
	}
}
