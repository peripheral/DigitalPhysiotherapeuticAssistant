package com.app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;





import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;
import com.app.classifiers.FeatureVectorModel;
import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;
import com.app.constants.Constants.VIEW_ID;
import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.gui.view.EntryView;
import com.app.gui.view.ExerciseView;
import com.app.gui.view.PatientView;
import com.app.gui.view.PhysicianView;
import com.app.thesis.utils.ExperimentalClassifierCallbacks;
import com.app.thesis.utils.PostureReader;


public class GraphicalUserInterface extends JFrame implements IMainGraphicInterface {
	public final static int DEFAULT_WIDTH = 800;
	public final static int DEFAULT_HEIGHT = 700;
	private static final Dimension PREFFERED_FRAME_SIZE = new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
	private EntryView entryPanel = null;
	private PatientView patientPanel = null;
	private PhysicianView physicianPanel = null;
	private ExerciseView exercisePanel = null;

	public GraphicalUserInterface(){
		super("KDFa");
		initiateFrame();
		initiatePanels();			
		add(entryPanel,BorderLayout.CENTER);		
		setVisible(true);		
	}

	private void initiateFrame() {
		setLayout(new BorderLayout());
		setSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setSize(new Dimension(width-50,height));		
	}

	private void initiatePanels() {
		entryPanel = new EntryView(this);
		patientPanel = new PatientView(this);
		physicianPanel = new PhysicianView(this);
		exercisePanel = new ExerciseView(this);
	}

	public GraphicalUserInterface(int width, int height){
		this();
		setSize(new Dimension(width,height));	
	}

	public Dimension getPrefferedSize(){
		return PREFFERED_FRAME_SIZE;
	}

	public void openView(VIEW_ID type) {
		remove(entryPanel);remove(patientPanel);remove(physicianPanel);	remove(exercisePanel);
		switch(type){
		case PATIENT_VIEW:
			patientPanel.refresh();
			add(patientPanel,BorderLayout.CENTER);
			break;
		case THERAPIST_VIEW: 
			add(physicianPanel,BorderLayout.CENTER);
			physicianPanel.resumeAnimator();
			break;
		case ENTRY_VIEW:
			add(entryPanel,BorderLayout.CENTER);
			break;
		case EXERCISE_VIEW:
			add(exercisePanel,BorderLayout.CENTER);
			exercisePanel.resumeAnimator();
			break;
		}
		revalidate();
		repaint();
	}

	public static void main(String[] args){
		new GraphicalUserInterface();
	}

	public void loadExercise(Exercise exercise) {
		exercisePanel.stopExercise();
		exercisePanel.loadExercise(exercise);
		openView(VIEW_ID.EXERCISE_VIEW);	
	}
	
	private Posture pose = new Posture();

	public void loadPosture(Posture pose) {
		this.pose = pose;
		exercisePanel.setExpectedPosture(this.pose);
		openView(VIEW_ID.EXERCISE_VIEW);			
	}

	public Posture getUserAvatar() {		
		return exercisePanel.getUserPosture();
	}

	public ExerciseView getExercisePanel() {
		return exercisePanel;
	}

	public void setExercisePanel(ExerciseView exercisePanel) {
		this.exercisePanel = exercisePanel;		
	}
}
