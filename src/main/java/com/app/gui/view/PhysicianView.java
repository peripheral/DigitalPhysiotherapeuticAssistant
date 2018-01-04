package com.app.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jogamp.opengl.util.FPSAnimator;
import com.app.constants.Constants.VIEW_ID;
import com.app.db.DAO;
import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.graphics.PostureList;
import com.app.graphics.PostureListItem;
import com.app.graphics.GLAvatarPanel;
import com.app.gui.GraphicalUserInterface;
import com.app.io.EntityExportImport;

public class PhysicianView extends JPanel implements MouseListener {

	public final static int DEFAULT_WIDTH = 800;
	public final static int DEFAULT_HEIGHT = 700;
	private static final Dimension PREFFERED_FRAME_SIZE=new Dimension(400,800);

	private SpinnerNumberModel xValue;
	private SpinnerNumberModel yValue;
	private SpinnerNumberModel zValue;
	private JLabel lblExerciseName = new JLabel("Exercise name:");
	private JLabel lblPostureName = new JLabel("Posture name:");
	private GraphicalUserInterface parent = null;

	/*
	 * Panel canvas on which the posture is rendered
	 */
	private GLAvatarPanel posturePanel = new GLAvatarPanel(this);

	//private int DEGREE_OF_FREEDOM = 180;
	/* Used as limit  */
	private FPSAnimator animator;
	private int frame_rate = 15;
	private JTextField inputExerciseName;
	private JTextField inputPostureName;
	private JLabel jointLabel;
	private PostureList postureList;
	

	public PhysicianView(){
		setLayout(new BorderLayout());
		postureList = new PostureList(this);
		postureList.setMouseListener(this);
		posturePanel = new GLAvatarPanel(this);
		posturePanel.addGLEventListener((GLAvatarPanel)posturePanel);
		posturePanel.addKeyListener((GLAvatarPanel)posturePanel);
		posturePanel.addMouseWheelListener((GLAvatarPanel)posturePanel);
		posturePanel.addMouseListener((GLAvatarPanel)posturePanel);
		posturePanel.addMouseMotionListener((GLAvatarPanel)posturePanel);
		((GLAvatarPanel)posturePanel).setPosture(postureList.getActivePosture());
		setUpControls();	
		add(posturePanel,BorderLayout.CENTER);
		animator = new FPSAnimator(posturePanel,frame_rate);			//Animator reloads display 10 times/sec normal 60  fps
		animator.start();	
		
		add(new JScrollPane(postureList),BorderLayout.EAST);
		
	}

	public PhysicianView(int width, int height){
		this();
		setPreferredSize(new Dimension(width, height));
	}

	public PhysicianView(GraphicalUserInterface parent) {
		this();
		this.parent = parent;
	}

	/**
	 * Controls of UI
	 */
	private void setUpControls() {

		JPanel controlPane = new JPanel();
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.PAGE_AXIS));
		JButton newPosture = new JButton("New Posture");
		controlPane.add(wrapButton(newPosture));
		JButton backToMenu = new JButton("Back to menu");
		controlPane.add(wrapButton(backToMenu));
		JButton exportExercise = new JButton("Export exercise");
		controlPane.add(wrapButton(exportExercise));
		JButton importExercise = new JButton("Import exercise");
		controlPane.add(wrapButton(importExercise));
		JButton importPosture = new JButton("Import Posture");
		controlPane.add(wrapButton(importPosture));
		JButton exportPosture = new JButton("Export Posture");
		controlPane.add(wrapButton(exportPosture));
		JButton saveExercise = new JButton("Save Exercise");
		controlPane.add(wrapButton(saveExercise));
		JButton retriveExercise = new JButton("Open Exercise DB");
		controlPane.add(wrapButton(retriveExercise));
		JButton newExercise = new JButton("New Exercise");
		controlPane.add(wrapButton(newExercise));
		backToMenu.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				animator.stop();
				parent.openView(VIEW_ID.ENTRY_VIEW);
			}
		});
		
	
		exportExercise.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EntityExportImport writer = new EntityExportImport();
				Exercise exercise = postureList.getExercise();
				writer.export(exercise);
			}
		});
		
		importExercise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EntityExportImport reader = new EntityExportImport();
				Exercise ex = reader.importExercise();
				if(ex != null){
					loadExercise(ex);
				}
			}
		});
	
	
		importPosture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EntityExportImport reader = new EntityExportImport();
				
				loadPosture(reader.importPosture());
			}
		});
	
		exportPosture.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				exportPosture();
			}
		});

		saveExercise.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveExercise();
			}
		});
		
		newPosture.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				newPosture();
			}
		});
	
		retriveExercise.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				displayExerciseList();
			}
		});
		newExercise.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				postureList.newExercise();
				setActivePosture(postureList.getActivePosture());
				revalidate();
				repaint();
			}
		});

		lblExerciseName.setAlignmentX(RIGHT_ALIGNMENT);
		lblExerciseName.setHorizontalAlignment(SwingConstants.LEFT);
		lblExerciseName.setOpaque(true);
		lblPostureName.setAlignmentX(RIGHT_ALIGNMENT);
		lblPostureName.setHorizontalAlignment(SwingConstants.LEFT);
		lblPostureName.setOpaque(true);
		inputExerciseName = new JTextField("Exercise Name");
		inputExerciseName.setMaximumSize(inputExerciseName.getPreferredSize());
		inputPostureName = new JTextField("Posture Name");
		inputPostureName.setMaximumSize(inputPostureName.getPreferredSize());
		inputPostureName.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				///postureList.getActivePosture().setName(inputPostureName.getText()+e.getKeyChar());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				postureList.getActivePosture().setName(inputPostureName.getText());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
						
			}
		});
		jointLabel = new JLabel();
		jointLabel.setText(((GLAvatarPanel)posturePanel).getPostureRenderer().getActiveJoint());
		JLabel lblJointName = new JLabel("Joint name:");
		lblJointName.setAlignmentX(RIGHT_ALIGNMENT);
		lblJointName.setHorizontalTextPosition(SwingConstants.RIGHT);
		controlPane.add(makeBox(lblExerciseName,inputExerciseName));
		controlPane.add(makeBox(lblPostureName,inputPostureName));
		controlPane.add(makeBox(lblJointName,jointLabel));
		configureSpinners();
		JSpinner xSpinner = new JSpinner(xValue);
		xSpinner.setAlignmentX(LEFT_ALIGNMENT);
		xSpinner.setBorder(BorderFactory.createEmptyBorder(3, 3,3, 3));
		controlPane.add(makeBox(new JLabel("X :"),xSpinner));
		xSpinner.setMaximumSize(new Dimension(btnWidth,64));
		JSpinner ySpinner = new JSpinner(yValue);
		ySpinner.setMaximumSize(new Dimension(btnWidth,64));
		ySpinner.setAlignmentX(LEFT_ALIGNMENT);
		ySpinner.setBorder(BorderFactory.createEmptyBorder(3, 3,3, 3));
		controlPane.add(makeBox(new JLabel("Y :"),ySpinner));
		JSpinner zSpinner = new JSpinner(zValue);
		zSpinner.setMaximumSize(new Dimension(btnWidth,64));
		zSpinner.setAlignmentX(LEFT_ALIGNMENT);
		zSpinner.setBorder(BorderFactory.createEmptyBorder(3, 3,3, 3));
		controlPane.add(makeBox(new JLabel("Z :"),zSpinner));
		add(controlPane,BorderLayout.WEST);
	}

	protected void loadPosture(Posture posture) {
		posturePanel.getPostureRenderer().setPosture(posture);
		postureList.addPosture(posture);
		inputPostureName.setText(posture.getName());
		revalidate();
		repaint();
	}
	
	protected void loadExercise(Exercise exercise) {
		postureList.setExercise(exercise);
		inputExerciseName.setText(exercise.getName());
		if(exercise.getPostures().size() >0){
			Posture first = exercise.getPostures().getFirst();
			posturePanel.getPostureRenderer().setPosture(first);
		}
		inputPostureName.setText(postureList.getActivePosture().getName());
		revalidate();
		repaint();
	}

	protected void newPosture() {
		postureList.addPosture(new Posture());
		((GLAvatarPanel)posturePanel).setPosture(postureList.getActivePosture());
		setActivePosture(postureList.getActivePosture());
		revalidate();
		repaint();
	}

	protected void saveExercise() {
		DAO dao = new DAO();
		Exercise e = postureList.getExercise();
		e.setName(inputExerciseName.getText());
		dao.create(e);
	}

	private JPanel initiatePanel(JPanel panel) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		return panel;
	}

	private JPanel makeBox(Container label,Container input){
		JPanel panel = initiatePanel(new JPanel());
		((JLabel)label).setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		panel.add(label);
		panel.add(input);
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		return panel;
	}

	protected void exportPosture() {		
		Posture p = postureList.getActivePosture();
		EntityExportImport writer = new EntityExportImport();
		writer.export(p);
	}

	private int btnWidth = 250;
	private int btnHeight = 35;
	private JPanel wrapButton(JButton btn) {
		adjustButton(btn);
		JPanel panel = new JPanel();
		panel.setAlignmentX(LEFT_ALIGNMENT);
		panel.setLayout(new GridLayout(1,1));
		panel.add(btn);
		panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		panel.setPreferredSize(new Dimension(btnWidth+6,btnHeight+10));	
		panel.setMaximumSize(new Dimension(btnWidth+6,btnHeight+10));		
		return panel;
	}

	private void adjustButton(JButton btn){
		btn.setPreferredSize(new Dimension(btnWidth+6,btnHeight+10));	
		btn.setMaximumSize(new Dimension(btnWidth,btnHeight));
		btn.setBackground(new Color(59, 89, 182));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setFont(new Font("Tahoma", Font.BOLD, 16));
	}

	private void configureSpinners() {
		xValue =
				new SpinnerNumberModel(posturePanel.getPosture().getHead()[0], //initial value
						-2000.0, //min
						2000.0, //max
						1.0);
		xValue.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				posturePanel.getPostureRenderer().setXValueOfSelectedJoint((Double)xValue.getValue());
			}
		});
		yValue =
				new SpinnerNumberModel(posturePanel.getPostureRenderer().getPosture().getHead()[1], //initial value
						-2000.0, //min
						2000.0, //max
						1.0);
		yValue.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				posturePanel.getPostureRenderer().setYValueOfSelectedJoint((Double)yValue.getValue());
			}
		});
		zValue =
				new SpinnerNumberModel(posturePanel.getPostureRenderer().getPosture().getHead()[2], //initial value
						-2000.0, //min
						2000.0, //max
						1.0);
		zValue.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				posturePanel.getPostureRenderer().setZValueOfSelectedJoint((Double)zValue.getValue());
			}
		});		
	}

	public Dimension getPrefferedSize(){
		return PREFFERED_FRAME_SIZE;
	}


	public void resumeAnimator() {
		if(!animator.isAnimating()){
			animator.start();
		}

	}
	
	private void displayExerciseList(){
		JFrame listWindow = new JFrame("Exercise List");
		Container c =listWindow.getContentPane();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		listWindow.setLocation((int)width/2, (int)height/2);
		DAO dao = new DAO();
		Exercise[] exerciseList = (Exercise[]) dao.getList(Exercise.class.getSimpleName());
		JList<Exercise> eList = new JList<Exercise>();
	
		if(exerciseList != null){
			eList.setListData(exerciseList);
		}
		eList.setFixedCellWidth(300);
	
		JScrollPane sEList = new JScrollPane(eList);
		eList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Exercise selectedV = eList.getSelectedValue();
				if(selectedV != null){
					loadExercise(eList.getSelectedValue());
				}
				
			}
		});
		JPopupMenu popMenu = new JPopupMenu();
		JMenuItem deleteExercise = new JMenuItem("Delete Exercise");
		deleteExercise.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				eList.setSelectedIndex(eList.locationToIndex(((JMenuItem)e.getSource()).getLocation()));
				System.out.println("Source:"+eList.getSelectedValue());
				DAO dao = new DAO();
				dao.delete(eList.getSelectedValue());
				if(postureList.getExercise().equals(eList.getSelectedValue())){
					/* Create new Exercise if the deleted entity is the current */
					postureList.newExercise();
					revalidate();
					repaint();
				}
				eList.setListData((Exercise[]) dao.getList(Exercise.class.getSimpleName()));
				listWindow.revalidate();
				listWindow.repaint();
				
			}
		});
		popMenu.add(deleteExercise);
		eList.setComponentPopupMenu(popMenu);
	

		sEList.setSize(new Dimension(300, 300));
		sEList.setMinimumSize(new Dimension(300, 300));
		c.setLayout(new BorderLayout());
		c.add(sEList, BorderLayout.CENTER);
		JButton submit = new JButton("Select");
		c.add(submit, BorderLayout.SOUTH);
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listWindow.dispose();
				System.out.println("Selected value is:"+eList.getSelectedValue());
			}
		});
		listWindow.setDefaultCloseOperation(
				JDialog.DISPOSE_ON_CLOSE);
	
		JPanel optionPane = new JPanel();
		optionPane.addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						String prop = e.getPropertyName();

						if (listWindow.isVisible() 
								&& (e.getSource() == optionPane)
								&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
							//If you were going to check something
							//before closing the window, you'd do
							//it here.
							listWindow.setVisible(false);
						}
					}
				});
		listWindow.pack();
		listWindow.setVisible(true);
	}

	private void setSetSelectedPosture(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object src = e.getSource();
		if(src instanceof PostureListItem){
			setActivePosture(((PostureListItem) src).getPosture());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	private FPSAnimator listAnimator = new FPSAnimator(10);
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println(e.getSource());
		Object src = e.getSource();
		if(src instanceof PostureListItem){
			listAnimator.add((PostureListItem)src);
			((PostureListItem) src).requestFocus();
			listAnimator.start();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println(e.getSource());
		Object src = e.getSource();
		if(src instanceof PostureListItem){
			listAnimator.stop();
			listAnimator.remove((PostureListItem)src);
		}

	}

	public Posture getActivePosture() {
		return postureList.getActivePosture();
	}

	public void setActivePosture(Posture activePosture) {
		postureList.setActivePosture(activePosture);
		posturePanel.setPosture(activePosture);
		inputPostureName.setText(activePosture.getName());
	}

	public void setSelectedJoint(double[] coords,String jointName) {
		xValue.setValue(coords[0]);	
		yValue.setValue(coords[1]);
		zValue.setValue(coords[2]);
		jointLabel.setText(jointName);
	}

	public void updateTextFields(double[] coords) {
		xValue.setValue(coords[0]);	
		yValue.setValue(coords[1]);
		zValue.setValue(coords[2]);		
	}
}
