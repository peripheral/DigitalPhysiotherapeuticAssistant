package com.app.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.awt.GLJPanel;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.gui.view.ExerciseView;
import com.app.gui.view.PhysicianView;

public class PostureList extends JPanel{
	/**
	 * Local access to displayed postureListItems 
	 */
	private List<GLJPanel> postureListItems = new LinkedList<>();
	private Exercise exercise = new Exercise();
	private MouseListener parent;
	private Posture activePosture = new Posture();

	public PostureList(Posture[] postures,JPanel listener){	
		this.parent = (MouseListener)listener;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		createItems(postures);
		initActivePosture();		
	}

	private void initActivePosture() {
		if(exercise.getPostures().size()>0){
			activePosture = exercise.getPostures().getFirst();
		}else{
			activePosture = new Posture();
			exercise.addPosture(activePosture);
			postureListItems.add(new PostureListItem(this,activePosture));
			postureListItems.get(0).setAlignmentX(LEFT_ALIGNMENT);
			add(postureListItems.get(0));
		}

	}

	public PostureList(PhysicianView listener) {
		this.parent = (MouseListener)listener;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initActivePosture();
	}

	public PostureList() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initActivePosture();
	}

	private void createItems(Posture[] postures) {
		PostureListItem item = null;
		for(Posture p:postures){
			item = new PostureListItem(this,p);
			item.setAlignmentX(LEFT_ALIGNMENT);
			item.addMouseListener(parent);
			this.postureListItems.add(item);
			add(item);
			exercise.addPosture(p);
		}
	}


	@Override
	public void paintComponent(Graphics g){
		for(GLJPanel p:postureListItems){
			p.display();
		}
	}

	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Posture[] postures = new Posture[5];
		for(int i = 0;i <postures.length;i++){
			postures[i] = new Posture();
		}		
		PostureList postureList = new PostureList(postures,null);
		frame.add(postureList,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	public void addPosture(Posture p) {
		activePosture = p;
		PostureListItem item = new PostureListItem(this,p);
		item.setAlignmentX(LEFT_ALIGNMENT);
		item.addMouseListener(parent);
		this.postureListItems.add(item);
		add(item);
		exercise.addPosture(p);
	}

	public void setMouseListener(MouseListener listener) {
		this.parent = listener;		
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void clear() {
		for(GLJPanel p:postureListItems){
			remove(p);
		}	
		postureListItems.clear();
	}

	public void setExercise(Exercise exercise) {
		clear();
		this.exercise = exercise;
		PostureListItem item = null;
		if(exercise == null){
			return;
		}
		for(Posture p:exercise.getPostures()){
			item = new PostureListItem(this,p);
			item.setAlignmentX(LEFT_ALIGNMENT);
			item.addMouseListener(parent);
			this.postureListItems.add(item);
			add(item);
		}
		if( exercise.getPostures().size()>0){
			activePosture = exercise.getPostures().getFirst();
		}
	}

	public Posture getActivePosture() {
		return activePosture;	
	}

	public void setActivePosture(Posture posture) {
		activePosture = posture;		
	}

	public void removeListItem(PostureListItem item) {
		postureListItems.remove(item);
		remove(item);
		System.out.println("Postue removed:"+exercise.getPostures().remove(item.getPosture()));
		((JPanel)parent).revalidate();
		((JPanel)parent).repaint();
	}

	/**
	 * Clears displayed postureList items and clears the list with postureListItems
	 */
	public void newExercise() {
		for(GLJPanel item:postureListItems){
			remove(item);
		}
		postureListItems.clear();
		exercise = new Exercise();
		exercise.addPosture(new Posture());
		postureListItems.add(new PostureListItem(this,exercise.getPostures().getFirst()));
		postureListItems.get(0).setAlignmentX(LEFT_ALIGNMENT);
		activePosture = exercise.getPostures().getFirst();
		add(postureListItems.get(0));
	}

}
