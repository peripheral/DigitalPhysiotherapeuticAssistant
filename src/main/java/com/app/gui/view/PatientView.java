package com.app.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import com.jogamp.opengl.util.FPSAnimator;
import com.app.classifiers.FeatureVectorModel;
import com.app.constants.Constants.VIEW_ID;
import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.gui.GraphicalUserInterface;
import com.app.gui.tabs.ExercisesTab;
import com.app.gui.tabs.HelpTab;
import com.app.gui.tabs.HistoryTab;
import com.app.gui.tabs.ProgressTab;

public class PatientView extends JPanel {

	public static enum TAB{HELP,HISTORY,MY_EXERCISES,MY_PROGRESS};
	private GraphicalUserInterface parent = null;
	private JButton loadExercise = new JButton("Load Exercise");	
	private ExercisesTab exerciseTab = null;
	private ProgressTab progressTab = null;
	private HelpTab helpTab = null;
	private HistoryTab historyTab = null;

	public PatientView(){
		setPreferredSize(new Dimension(GraphicalUserInterface.DEFAULT_WIDTH,
										GraphicalUserInterface.DEFAULT_HEIGHT));
		setLayout(new BorderLayout());
		setBackground(Color.BLUE);
		setUpControls();
		loadMyProgressTab();
	}
	
	private void clearTabPane() {
		if(exerciseTab != null){
			remove(exerciseTab);
		}
		if(progressTab != null){
			remove(progressTab);
		}
	
		if(historyTab != null){
			remove(historyTab);
		}
	
		if(helpTab != null){
			remove(helpTab);
		}
	}

	private void loadMyExercisesTab() {
		clearTabPane();
		exerciseTab = new ExercisesTab(this);
		add(exerciseTab,BorderLayout.CENTER);
	}


	private void loadMyProgressTab(){
		clearTabPane();
		progressTab = new ProgressTab(this);
		add(progressTab,BorderLayout.CENTER);
	}

	private void loadHelpTab(){
		clearTabPane();
		helpTab = new HelpTab(this);
		add(helpTab,BorderLayout.CENTER);
	}

	private void loadHistoryTab(){
		clearTabPane();
		historyTab = new HistoryTab(this);
		add(historyTab,BorderLayout.CENTER);
	}

	public PatientView(GraphicalUserInterface parent){
		this();		
		this.parent = parent;
	}

	/**
	 * Controls of UI
	 */
	private void setUpControls() {


		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.PAGE_AXIS));
		JButton myProgress = new JButton("My Progress");
		myProgress.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				openTab(TAB.MY_PROGRESS);			
			}
		});
		buttonPane.add(wrapButton(myProgress));
		JButton myExercises = new JButton("My Exercises");
		myExercises.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openTab(TAB.MY_EXERCISES);				
			}
		});
		buttonPane.add(wrapButton(myExercises));
		JButton history = new JButton("History");
		history.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openTab(TAB.HISTORY);				
			}
		});
		buttonPane.add(wrapButton(history));
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openTab(TAB.HELP);				
			}
		});
		buttonPane.add(wrapButton(btnHelp));	
		JButton btnReturn = new JButton("Back");
		btnReturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.openView(VIEW_ID.ENTRY_VIEW);

			}
		});
		buttonPane.add(wrapButton(btnReturn));
		add(buttonPane,BorderLayout.WEST);
	}

	private void displayStartButton(){
		add(loadExercise,BorderLayout.SOUTH);
		loadExercise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Exercise selected = (Exercise)exerciseTab.getSelectedValue();
				if(selected != null){
					hideLoadButton();
					loadExercise(selected);
				}								
			}
		});
	}
	protected void startExercise(Posture pose) {
		parent.loadPosture(pose);			
	}
	
	public void loadExercise(Exercise selectedValue) {
		parent.loadExercise(selectedValue);	
	}

	protected void openTab(TAB tab) {
		hideLoadButton();
		switch(tab){
		case MY_PROGRESS:
			loadMyProgressTab();
			break;
		case MY_EXERCISES:
			loadMyExercisesTab();
			displayStartButton();
			break;
		case HISTORY:
			loadHistoryTab();
			break;
		case HELP:
			loadHelpTab();
			break;
		default:
			/*N/A*/
			break;
		}
		revalidate();	
	}

	private void hideLoadButton() {
		remove(loadExercise);		
	}

	private JPanel wrapButton(JButton btn) {
		adjustButton(btn);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		panel.add(btn);
		panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		panel.setMaximumSize(new Dimension(300+6,46+10));		
		return panel;
	}

	public PatientView(int width, int height){
		this();
		setPreferredSize(new Dimension(width,height));
	}

	public PatientView(GraphicalUserInterface parent, int defaultWidth, int defaultHeight) {
		this(defaultWidth,defaultHeight);
		this.parent = parent;
	}


	private double[] getPercentages(float[] featureVector,int innerLimit) {
		double[] targetVector = new double[featureVector.length/2];
		for(int i = 0; i < targetVector.length;i++){
			targetVector[i] = 0;
			targetVector[i] = featureVector[i+19] - featureVector[i];
			if(targetVector[i] <0 ){
				targetVector[i]*=-1;	
			}
			if(targetVector[i] >=innerLimit){
				targetVector[i] =0;
			}else{
				targetVector[i] = targetVector[i]/innerLimit;
				targetVector[i] = 100-targetVector[i];
			}

		}
		return targetVector;
	}

	/**
	 * Total percentage in double, 1-100
	 * @param expectedPoseData - expected array
	 * @param observedPoseData - provided array
	 * @return total matching percentage
	 */
	public static double getTargetValue(double[] expectedPoseData,
			double[] observedPoseData,int outerLimit,int innerLimit) {
		double[] targetVector = new double[expectedPoseData.length];
		double sum = 0;
		double max = 100 * expectedPoseData.length;
		for(int i = 0; i < targetVector.length;i++){
			targetVector[i] = 0;
			targetVector[i] = expectedPoseData[i] - observedPoseData[i];
			if(targetVector[i] <0 ){
				targetVector[i]*=-1;	
			}
			if(targetVector[i] > innerLimit){
				targetVector[i] = 0;
			}else{
				targetVector[i] = 100-(targetVector[i]/innerLimit)*100;
			}	
			sum = sum +targetVector[i];
		}
		return (int)((sum/max)*100);
	}

	public static void main(String[] args){
		new GraphicalUserInterface();

	}
	private void adjustButton(JButton btn){
		btn.setMaximumSize(new Dimension(300,46));
		btn.setBackground(new Color(59, 89, 182));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setFont(new Font("Tahoma", Font.BOLD, 24));
	}

	public void refresh() {
		openTab(TAB.MY_PROGRESS);	
	}
}
