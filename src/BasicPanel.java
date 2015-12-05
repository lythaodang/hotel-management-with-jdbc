import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * The basic panel with the common factors between all other panels.
 */
@SuppressWarnings("serial")
public class BasicPanel extends JPanel {
	private GridBagConstraints c;
	private Model model;
	private View manager;
	private ArrayList<JTextField> tfs;
	private ArrayList<JTextArea> tas;
	@SuppressWarnings("rawtypes")
	private ArrayList<JComboBox> cbs;
	@SuppressWarnings("rawtypes")
	private ArrayList<JList> ls;
	
	/** 
	 * Constructs the panel with a view manager.
	 * @param manager the view manager
	 */
	@SuppressWarnings("rawtypes")
	public BasicPanel(View manager) {
		this.manager = manager;
		model = manager.getModel();
		tfs = new ArrayList<JTextField>();
		tas = new ArrayList<JTextArea>();
		cbs = new ArrayList<JComboBox>();
		ls = new ArrayList<JList>();
		
		this.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
	}
	
	/**
	 * Adds a component to the panel at location x, y
	 * @param comp the component to add
	 * @param x the x location
	 * @param y the y location
	 */
	@SuppressWarnings("rawtypes")
	public void addComponent(JComponent comp, int x, int y) {
		c.gridx = x;
		c.gridy = y;
		add(comp, c);
		
		addComponent(comp);
	}
	
	public void addComponent(JComponent comp) {
		if (comp instanceof JTextField)
			tfs.add((JTextField) comp);
		if (comp instanceof JTextArea)
			tas.add((JTextArea) comp);
		if (comp instanceof JComboBox)
			cbs.add((JComboBox) comp);
		if (comp instanceof JList)
			ls.add((JList) comp);
	}
	
	/**
	 * Adds instructions to the panel.
	 * @param instructions the instructions to add
	 */
	public void addInstructions(String instructions) {
		JLabel label = new JLabel(instructions);
		addComponent(label, 0, 0);
	}
	
	/**
	 * Adds a button for navigation.
	 * @param text the text displayed on the button
	 * @param destination the panel to switch back to
	 * @param x the x location 
	 * @param y the y location
	 */
	public void addNavigationButton(String text, int textSize, final String destination, int x, int y) {
		JButton button = new JButton(text);
		button.setFont(new Font("Tahoma", Font.BOLD, textSize));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearComponents();
				manager.switchPanel(destination);
			}
		});
		addComponent(button, x, y);
	}
	
	/**
	 * Adds a sign out button.
	 * @param backTo the panel to switch back to
	 * @param x the x location
	 * @param y the y location
	 */
	public void addSignOutButton(int textSize, final String backTo, int x, int y) {
		JButton button = new JButton("Sign Out");
		button.setFont(new Font("Tahoma", Font.BOLD, textSize));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentUser(null);
				clearComponents();
				manager.switchPanel(backTo);
			}
		});
		addComponent(button, x, y);
	}
	
	/**
	 * Adds a label to the panel.
	 * @param text the text to display
	 * @param x the x location
	 * @param y the y location
	 */
	public JLabel addLabel(String text, int size, String align, Color foreground, Color background, int x, int y) {
		JLabel label = new JLabel(text);
		if (align.equals("left"))
			label.setHorizontalAlignment(SwingConstants.LEFT);
		else if (align.equals("center"))
				label.setHorizontalAlignment(SwingConstants.CENTER);
		else
			label.setHorizontalAlignment(SwingConstants.RIGHT);
		
		if (foreground != null)
			label.setForeground(foreground);
		
		if (background != null) {
			label.setBackground(background);
			label.setOpaque(true);
		}
		
		label.setFont(new Font("Tahoma", Font.BOLD, size));
		addComponent(label, x, y);
		return label;
	}
	
	/**
	 * Clears all textfields and textareas on the panel.
	 */
	@SuppressWarnings({ "rawtypes" })
	public void clearComponents() {
		for (JTextField tf : tfs)
			tf.setText("");
		for (JTextArea ta : tas)
			ta.setText("");
		for (JComboBox cb : cbs)
			cb.setSelectedIndex(0);
		for (JList l : ls) 
			l.setListData(new Object[1]);
	}

	public GridBagConstraints getConstraints() {
		return c;
	}
}
