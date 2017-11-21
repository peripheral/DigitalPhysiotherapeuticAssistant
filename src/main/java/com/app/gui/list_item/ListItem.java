package com.app.gui.list_item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.app.entities.Exercise;

public class ListItem extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long exerciseId = -1;
	private Exercise exercise;
	public ListItem(Exercise e){
		exerciseId = e.getId();
		this.exercise = e;
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		add(exerciseName(e));
		add(latestProgressPane(e));
		add(latestHighestProgressPane(e));
		setBackground(Color.GREEN);
	}

	private JLabel exerciseName(Exercise e) {
		JLabel lbl = new JLabel(e.getName());
		lbl.setHorizontalAlignment(JLabel.LEFT);
		lbl.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lbl.setBackground(Color.white);
		lbl.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
		lbl.setOpaque(true);
		return lbl;
	}

	private JProgressBar latestHighestProgressPane(Exercise e) {
		JProgressBar pb = new JProgressBar(0,100);
		pb.setStringPainted(true);
		pb.setString("Highest Score:"+e.getHighestProgress()+"%");
		pb.setValue(25);
		pb.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		pb.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
		return pb;
	}

	private JProgressBar latestProgressPane(Exercise e) {
		JProgressBar pb = new JProgressBar(0,100);
		pb.setStringPainted(true);
		pb.setString("Latest Score:"+e.getLatestProgress()+"%");
		pb.setValue(40);
		pb.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		pb.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
		return pb;
	}


	public long getExerciseId(){
		return exerciseId;
	}
}
