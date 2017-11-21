package com.app.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.app.entities.Exercise;
import com.app.entities.ExerciseFactory;
import com.app.gui.list_item.ListItem;
import com.app.gui.view.PatientView;

public class ProgressTab extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3844080846948183219L;
	private PatientView parent = null;
	public ProgressTab(PatientView parent) {
		this.parent = parent;
		setBackground(Color.yellow);
		loadItems();
		setLayout(new GridLayout(1,1));
	}
	
	/**
	 * Populates tab with exercises and their progress
	 */
	private void loadItems(){
		List<Exercise> exercises = getExerciseList();
		if(exercises.size()<1){
			add(new JLabel("No Entries found"));
		}
		JPanel listContainer = new JPanel();
		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.PAGE_AXIS));
		createList(listContainer,exercises);
		JScrollPane listScroll = new JScrollPane(listContainer);
		add(listScroll,BorderLayout.CENTER);
	}	

	private void createList(JPanel listContainer, List<Exercise> exercises) {
		for(Exercise e:exercises){
			ListItem item = new ListItem(e);
			item.setBorder(new EmptyBorder(5, 5, 5, 5));
			listContainer.add(item);
		}
	}

	private List<Exercise> getExerciseList() {
		String[] exerciseNames ={"Exercise 1","Exercise 2","Exercise 3"};
		List<Exercise> list = Arrays.asList(ExerciseFactory.makeExercises(exerciseNames));		
		return list;
	}
}
