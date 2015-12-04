package Fix;
import BasicPanel;
import View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

/**
 * COPYRIGHT 2014 InfiniteLoops. All Rights Reserved. 
 * Hotel Management 
 * CS151 Group Project
 * @author Mike Phe, Ly Dang, Andrew Yobs
 * @version 1.00 2014/10/30
 */

/**
 * This is the panel in which the user can view the receipts.
 */
@SuppressWarnings("serial")
public class ReceiptPanel extends BasicPanel {
	/**
	 * Constructs the receipt panel with a view manager
	 * @param m the view manager
	 */
	public ReceiptPanel(View m) {
		super(m);

		final SimpleReceipt simple = new SimpleReceipt(model.getTransaction());
		final ComprehensiveReceipt comprehensive = new ComprehensiveReceipt(
				model.getCurrentUser());

		c.gridwidth = 2;
		addInstructions("<html>Choose the type of receipt you wish to view.<br>"
				+ "Simple: Shows only reservations in this transaction.<br>"
				+ "Comprehensive: Shows all reservations made with this account.<br>"
				+ "Sign out when you are done.");

		final JTextArea receipt = new JTextArea();
		receipt.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(receipt,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) receipt.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		c.weighty = 1;
		addComponent(scrollPane, 0, 2);

		JButton simpleButton = new JButton("Simple");
		simpleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				receipt.setText(simple.toString());
			}
		});
		c.weighty = 0.25;
		c.gridwidth = 1;
		addComponent(simpleButton, 0, 1);

		JButton comprehensiveButton = new JButton("Comprehensive");
		comprehensiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				receipt.setText(comprehensive.toString());
			}
		});
		addComponent(comprehensiveButton, 1, 1);

		JButton backButton = new JButton("Back to Guest Menu");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.getTransaction().clear();
				clearComponents();
				manager.switchPanel("GMenu");
			}
		});
		addComponent(backButton, 0, 3);

		JButton signOutButton = new JButton("Sign out");
		signOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentUser(null);
				model.getTransaction().clear();
				clearComponents();
				manager.switchPanel("Initial");
			}
		});
		addComponent(signOutButton, 1, 3);

		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				comprehensive.setUser(model.getCurrentUser());
				receipt.setText("");
			}
		});
	}
}