package com.app.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.app.constants.Constants.VIEW_ID;
import com.app.gui.IMainGraphicInterface;

/**
 * 
 * @author Artur Vitt
 *
 */
public class EntryView extends JPanel{
	private JButton proceedAsPatient = new JButton("Proceed As Patient");
	private JButton proceedAsTherapist = new JButton("Proceed As Therapist");
	public EntryView(IMainGraphicInterface parent) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		adjustButton(proceedAsPatient);adjustButton(proceedAsTherapist);
		proceedAsPatient.setAlignmentX(CENTER_ALIGNMENT);
		proceedAsTherapist.setAlignmentX(CENTER_ALIGNMENT);
		add(Box.createVerticalGlue());
		add(proceedAsPatient);
		JPanel space = new JPanel();
		space.setMaximumSize(new Dimension(5,0));
		add(space);	
		add(proceedAsTherapist);
		add(Box.createVerticalGlue());
		proceedAsPatient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.openView(VIEW_ID.PATIENT_VIEW);
			}
		});
		proceedAsTherapist.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.openView(VIEW_ID.THERAPIST_VIEW);
			}
		});		
	}
	
	private void adjustButton(JButton btn){
		btn.setMaximumSize(new Dimension(300,50));
		btn.setBackground(new Color(59, 89, 182));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setFont(new Font("Tahoma", Font.BOLD, 24));
	}
	
}
