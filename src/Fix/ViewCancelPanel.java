package Fix;
import BasicPanel;
import Reservation;
import View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This panel allows the user to view and cancel their reservations.
 */
@SuppressWarnings("serial")
public class ViewCancelPanel extends BasicPanel {
	/**
	 * Constructs the panel with a view manager
	 * @param m the view manager
	 */
	public ViewCancelPanel(View m) {
		super(m);

		c.gridwidth = 2;
		addInstructions("<html>Below are all your reservations.<br>To cancel a "
				+ "reservation: Select the one you wish to cancel. Press cancel.<br>"
				+ "If the list is empty, then you have not made any reservations.</html>");

		@SuppressWarnings("rawtypes")
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		c.weighty = 1;
		addComponent(listScroller, 0, 1);

		model.addChangeListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void stateChanged(ChangeEvent e) {
				if (model.getCurrentUser() != null) list.setListData(model
						.getCurrentUser().getReservations().toArray());
				else list.setListData(new Reservation[0]);
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!list.isSelectionEmpty()) {
					int response = JOptionPane.showConfirmDialog(new JFrame(),
							"Are you sure you want to cancel this reservation?",
							"Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) ;
					if (response == JOptionPane.YES_OPTION) {
						model.cancelReservation((Reservation) list.getSelectedValue());
					}
				}
			}
		});
		c.weighty = 0.5;
		addComponent(cancelButton, 0, 2);

		c.gridwidth = 1;
		addNavigationButton("Back to Guest Menu", "GMenu", 0, 3);

		addSignOutButton("Initial", 1, 3);
	}
}
