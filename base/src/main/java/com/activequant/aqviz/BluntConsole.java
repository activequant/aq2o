package com.activequant.aqviz;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * extremely blunt and simple console frame. 
 * 
 * @author GhostRider
 *
 */
public class BluntConsole extends JFrame {

	private static final long serialVersionUID = 1L;
	JTextPane textPane = new JTextPane(); 
	StringBuilder sb = new StringBuilder();
	public BluntConsole(String title){
		super(title);
		setSize(500,300);
		textPane.setEditable(false);
		textPane.setDoubleBuffered(true);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);
		this.toFront();
		this.setVisible(true);
	}
	
	
	public void addLog(String text){
		sb.insert(0, "\n");
		sb.insert(0, text);
		textPane.setText(sb.toString());		
	}
	
	
}
