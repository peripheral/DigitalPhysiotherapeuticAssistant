package com.app.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Stack;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import com.jogamp.opengl.util.FPSAnimator;
import com.app.classifiers.IClassifierCallbacks;
import com.app.classifiers.ClassifierManagerImpl;
import com.app.classifiers.FeatureVectorModel;
import com.app.classifiers.IClassifierManager;
import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;
import com.app.constants.Constants.VIEW_ID;
import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.gui.GraphicalUserInterface;
import com.app.kinect.connector.KinectConnector;
import com.app.kinect.connector.KinectEventListener;
import com.app.graphics.GLAvatarPanel;

public class ExerciseView extends GLJPanel implements GLEventListener,IClassifierCallbacks,KinectEventListener{
	private static final int INNER_LIMIT = 45;
	private String exerciseProgress = "N/A";
	protected GLJPanel expectedPostureContainer = new GLAvatarPanel(this,"Expected Posture");
	protected GLJPanel avatarContainer = new GLAvatarPanel(this,"Your posture");
	private JPanel controlsContainer = new JPanel();
	private JPanel measurementPane = new JPanel();
	private JPanel centerContainer = new JPanel();
	private JLabel annResult = new JLabel("0-40%");
	private JLabel rTreesResult = new JLabel("0-40%");
	private JLabel knnResult = new JLabel("0-40%");
	protected JLabel manualResult = new JLabel("0-40%");
	private JLabel annUpdateRate = new JLabel("N/A");
	private JLabel rTreesUpdateRate= new JLabel("N/A");
	private JLabel knnUpdateRate = new JLabel("N/A");
	private JLabel manualResultUpdateRate = new JLabel("N/A");
	private JRadioButton radioBtnAnn =  new JRadioButton("",true);
	private JRadioButton radioBtnKnn = new JRadioButton();
	private JRadioButton radioBtnRTrees = new JRadioButton();
	protected JButton beginExercise = new JButton("BEGIN EXERCISE");
	private FPSAnimator animator1;
	private GraphicalUserInterface parent = null;
	private Border aura = BorderFactory.createLineBorder(new Color(200,200,255), 4);
	private IClassifierManager cManager = new ClassifierManagerImpl();
	private JProgressBar pb = new JProgressBar(JProgressBar.HORIZONTAL) ;
	private KinectConnector kinConnector = new KinectConnector();

	public ExerciseView(){
		ExerciseView ea = this;
		setLayout(new BorderLayout());
		initiateAvatarContainer();
		initiateExpectedPosePane();	
		initiatiateAnimator();

		kinConnector.addKinectEventListener(this);
		centerContainer.setLayout(new GridLayout(1, 2));
		controlsContainer.setLayout(new BoxLayout(controlsContainer,BoxLayout.PAGE_AXIS));
		controlsContainer.setAlignmentX(LEFT_ALIGNMENT);
		add(centerContainer,BorderLayout.CENTER);
		add(beginExercise,BorderLayout.SOUTH);
		initiateLeftButtonList(ea);			
		initiateLeftPanel();
		initiateBtnBeginExercise(ea);
		measurementPane.setLayout(new BoxLayout(measurementPane, BoxLayout.PAGE_AXIS) );
		measurementPane.setAlignmentX(LEFT_ALIGNMENT);
		measurementPane.setMaximumSize(new Dimension(btnWidth,Short.MAX_VALUE));
		JPanel column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.LINE_AXIS));
		JLabel lbl = new JLabel("NNResult:");
		lbl.setAlignmentX(LEFT_ALIGNMENT);
		column.add(getRadioBtnAnn());
		column.add(setDefaulLabelLength(lbl));	
		column.add(Box.createHorizontalGlue());
		column.setAlignmentX(LEFT_ALIGNMENT);
		annResult.setAlignmentX(CENTER_ALIGNMENT);
		column.add(annResult);
		column.add(Box.createHorizontalGlue());
		annUpdateRate.setAlignmentX(RIGHT_ALIGNMENT);
		column.add(annUpdateRate);
		measurementPane.add(column);
		column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.LINE_AXIS));
		lbl = new JLabel("Random Forests:");
		lbl.setAlignmentX(LEFT_ALIGNMENT);
		column.add(radioBtnRTrees);
		column.add(setDefaulLabelLength(lbl));
		column.add(Box.createHorizontalGlue());
		column.setAlignmentX(LEFT_ALIGNMENT);
		column.add(rTreesResult);
		column.add(Box.createHorizontalGlue());
		rTreesUpdateRate.setAlignmentX(RIGHT_ALIGNMENT);
		column.add(rTreesUpdateRate);
		measurementPane.add(column);
		column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.LINE_AXIS));
		lbl = new JLabel("K - NN:");
		lbl.setAlignmentX(LEFT_ALIGNMENT);
		column.add(radioBtnKnn);
		column.add(setDefaulLabelLength(lbl));
		column.add(Box.createHorizontalGlue());
		column.setAlignmentX(LEFT_ALIGNMENT);
		column.add(knnResult);
		column.add(Box.createHorizontalGlue());
		knnUpdateRate.setAlignmentX(RIGHT_ALIGNMENT);
		column.add(knnUpdateRate);
		measurementPane.add(column);
		column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.LINE_AXIS));
		lbl = new JLabel("Expected:");
		lbl.setAlignmentX(LEFT_ALIGNMENT);
		column.add(setDefaulLabelLength(lbl));
		column.add(Box.createHorizontalGlue());
		column.setAlignmentX(LEFT_ALIGNMENT);
		column.add(manualResult);	
		column.add(Box.createHorizontalGlue());
		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(radioBtnAnn);
		group.add(radioBtnKnn);
		group.add(radioBtnRTrees);
		manualResultUpdateRate.setAlignmentX(RIGHT_ALIGNMENT);
		column.add(manualResultUpdateRate);
		measurementPane.add(column);
		measurementPane.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
	}

	private void initiateBtnBeginExercise(ExerciseView ea) {
		beginExercise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getRadioBtnAnn().isSelected()){
					cManager.subcribeTo(CLASSIFIER_TYPE.ANN, ea);
				}
				if(radioBtnKnn.isSelected()){
					cManager.subcribeTo(CLASSIFIER_TYPE.KNN, ea);	
				}
				if(radioBtnRTrees.isSelected()){
					cManager.subcribeTo(CLASSIFIER_TYPE.RANDOMTREES, ea);	
				}
				setEnabledRadioButtons(false);
				ea.remove(beginExercise);
				ea.add(pb,BorderLayout.NORTH);
				startExercise();				
			}
		});		
	}

	protected void setEnabledRadioButtons(boolean b) {
		radioBtnAnn.setEnabled(b);
		radioBtnKnn.setEnabled(b);
		radioBtnRTrees.setEnabled(b);

	}

	protected void startExercise() {
		postureStack = new Stack<Posture>();
		List<Posture> list = selectedExercise.getPostures();
		int max = 0;
		for(int i = list.size()-1;i>=0;i-- ){
			max = max+ list.get(i).getDuration();
			postureStack.push(list.get(i));
		}		
		pb.setMaximum(0);
		pb.setMaximum( max);
		progressIndicator = 0;
		exerciseStarted = true;
		exerciseTerminated = false;
		startSlider();	
	}

	private void initiateLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.add(controlsContainer);
		leftPanel.add(measurementPane);
		add(leftPanel,BorderLayout.WEST);
	}

	private void initiateLeftButtonList(ExerciseView ea) {
		JButton exitExercise = new JButton("Exit exercise");
		exitExercise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopAnimators();
				ea.remove(pb);
				ea.add(beginExercise,BorderLayout.SOUTH);
				setEnabledRadioButtons(true);
				ea.revalidate();
				cManager.unsubscribeAll(ea);
				exerciseStarted = false;
				exerciseTerminated = true;
				parent.openView(VIEW_ID.ENTRY_VIEW);
			}
		});
		controlsContainer.add(wrapButton(exitExercise));
	}
	private void initiatiateAnimator() {
		animator1 = new FPSAnimator(this, 15);			//Animator reloads display 10 times/sec normal 25  fps
		animator1.start();		
	}

	private void initiateAvatarContainer() {
		avatarContainer.addGLEventListener((GLAvatarPanel)avatarContainer);
		avatarContainer.addMouseWheelListener((GLAvatarPanel)avatarContainer);
		avatarContainer.addKeyListener((GLAvatarPanel)avatarContainer);
		((GLAvatarPanel)avatarContainer).setRotationAngleY(-90);
		centerContainer.add(avatarContainer,BorderLayout.CENTER);
		addMouseListenerToAvatar();
	}

	private void initiateExpectedPosePane() {
		expectedPostureContainer.addGLEventListener((GLAvatarPanel)expectedPostureContainer);
		expectedPostureContainer.addMouseWheelListener((GLAvatarPanel)expectedPostureContainer);
		expectedPostureContainer.addKeyListener((GLAvatarPanel)expectedPostureContainer);
		centerContainer.add(expectedPostureContainer,BorderLayout.CENTER);
		addMouseListenerToExpectedPostureContainer();		
	}

	private JLabel setDefaulLabelLength(JLabel label){
		label.setMaximumSize(new Dimension(110, label.getMaximumSize().height));
		label.setPreferredSize(new Dimension(110, label.getMaximumSize().height));
		return label;
	}

	public ExerciseView(GraphicalUserInterface parent) {
		this();
		this.parent = parent;
	}	

	private void addMouseListenerToExpectedPostureContainer() {
		expectedPostureContainer.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				expectedPostureContainer.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				expectedPostureContainer.requestFocus();
				expectedPostureContainer.setBorder(aura);

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void addMouseListenerToAvatar() {
		avatarContainer.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				avatarContainer.setBorder(null);

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				avatarContainer.requestFocus();
				avatarContainer.setBorder(aura);

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected void stopAnimators() {
		animator1.stop();
		exerciseTerminated = true;
	}

	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700,500);
		frame.add(new ExerciseView());
		frame.setVisible(true);
	}


	private int btnWidth = 250;
	private JPanel wrapButton(JButton btn) {
		adjustButton(btn);
		JPanel panel = new JPanel();
		panel.setAlignmentX(RIGHT_ALIGNMENT);
		panel.setLayout(new GridLayout(1,1));
		panel.add(btn);
		panel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		panel.setPreferredSize(new Dimension(btnWidth+6,80+10));	
		panel.setMaximumSize(new Dimension(btnWidth+6,80+10));		
		return panel;
	}
	private void adjustButton(JButton btn){
		btn.setPreferredSize(new Dimension(btnWidth+6,80+10));	
		btn.setMaximumSize(new Dimension(btnWidth,80));
		btn.setBackground(new Color(59, 89, 182));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setFont(new Font("Tahoma", Font.BOLD, 16));
	}



	public void setExpectedPosture(Posture pose) {
		((GLAvatarPanel)expectedPostureContainer).getPostureRenderer().setPosture(pose);
	}

	private long annLastUpdate = System.currentTimeMillis();
	private long knnLastUpdate = System.currentTimeMillis();
	private long rTreesLastUpdate = System.currentTimeMillis();
	public void setClassificationResult(CLASSIFIER_TYPE type, String result) {
		DecimalFormat myFormatter = new DecimalFormat("00.0");
		setCalculatedResult();
		refreshSuccessBar();
		switch(type){
		case ANN:
			annResult.setText(result);
			annUpdateRate.setText(myFormatter.format((1/((System.currentTimeMillis()-annLastUpdate)/1000.0))));
			annLastUpdate = System.currentTimeMillis();
			break;
		case KNN:	
			knnResult.setText(result);
			knnUpdateRate.setText(myFormatter.format((1/((System.currentTimeMillis()-knnLastUpdate)/1000.0))));
			knnLastUpdate = System.currentTimeMillis();
			break;
		case RANDOMTREES:
			rTreesResult.setText(result);
			rTreesUpdateRate.setText(myFormatter.format((1/((System.currentTimeMillis()-rTreesLastUpdate)/1000.0))));
			rTreesLastUpdate = System.currentTimeMillis();
			break;
		}	
	}

	private void refreshSuccessBar() {
		FeatureVectorModel fVectorModel = new FeatureVectorModel();
		fVectorModel.setExpectedPosture(((GLAvatarPanel)avatarContainer).getPosture());
		fVectorModel.setObservedPosture(((GLAvatarPanel)expectedPostureContainer).getPosture());
		ClassifierManagerImpl man = new ClassifierManagerImpl();
		state = man.classifySampleAsId(fVectorModel.getFeatureVectorPercentages());

	}

	private void setCalculatedResult() {
		FeatureVectorModel fVectorModel = new FeatureVectorModel();
		fVectorModel.setExpectedPosture(((GLAvatarPanel)avatarContainer).getPosture());
		fVectorModel.setObservedPosture(((GLAvatarPanel)expectedPostureContainer).getPosture());
		ClassifierManagerImpl man = new ClassifierManagerImpl();
		manualResult.setText(man.classifySampleAsLabelFromPercentages(fVectorModel.getFeatureVectorPercentages()));
	}

	public void setExpectedClassification(String str){
		manualResult.setText(str);
	}

	public void resumeAnimator() {
		animator1.start();	
	}

	@Override
	public void paintChildren(Graphics gx){
		super.paintChildren(gx);
		gx.setColor(Color.GREEN);
		gx.fillArc(300, 30, 50, 50, 90,(int)angle);
		drawSuccessBars(gx);		
	}
	private int state = 0;
	protected boolean exerciseStarted = false;
	private void drawSuccessBars(Graphics gx) {
		gx.setColor(new Color(255,0,0,255));
		int startX = 0;
		int startY = getHeight()-beginExercise.getHeight();
		if(exerciseStarted){
			startY = getHeight();
		}
		startX = measurementPane.getWidth()+10;
		gx.fillRect(startX, startY-30, 30, 30);
		if(state> 1){
			gx.setColor(new Color(255,255,0,255));
		}else{
			gx.setColor(new Color(255,255,0,90));
		}

		gx.fillRect(startX, startY-70, 60, 40);
		if(state> 2){
			gx.setColor(new Color(90,255,0,255));
		}else{
			gx.setColor(new Color(90,255,0,90));
		}

		gx.fillRect(startX, startY-120, 90, 50);	
		if(state> 3){
			gx.setColor(new Color(0,255,0,255));
		}else{
			gx.setColor(new Color(0,255,0,90));
		}

		gx.fillRect(startX, startY-170, 120, 50);	
	}


	private int timer = 0; 
	private double angle = 0;
	private double delta = 1;
	private Exercise selectedExercise;
	private Stack<Posture> postureStack;
	private int assignedTime;
	private double counter(){
		angle = (angle-delta)%361;	
		if(Math.abs(angle) > 360-360%delta){
			angle = -360;
		}
		return angle;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		((GLEventListener)avatarContainer).init(drawable);
		((GLEventListener)expectedPostureContainer).init(drawable);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		((GLEventListener)avatarContainer).dispose(drawable);
		((GLEventListener)avatarContainer).dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		((GLEventListener)avatarContainer).display(drawable);	
		((GLEventListener)avatarContainer).display(drawable);
		repaint();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		((GLEventListener)avatarContainer).reshape(drawable,x,y,width,height);
		((GLEventListener)avatarContainer).reshape(drawable,x,y,width,height);
	}

	public Posture getUserPosture() {
		return ((GLAvatarPanel)avatarContainer).getPosture();		
	}

	@Override
	public float[] getFeatureVector() {
		FeatureVectorModel fVectorModel = new FeatureVectorModel();
		fVectorModel.setExpectedPosture(((GLAvatarPanel)avatarContainer).getPosture());
		fVectorModel.setObservedPosture(((GLAvatarPanel)expectedPostureContainer).getPosture());
		return fVectorModel.getFeatureVectorPercentages();
	}

	public void loadExercise(Exercise exercise) {
		this.selectedExercise = exercise;
		setFirstPostureAsExpected();	
	}

	/*
	 * Sets first posture in the list as expected
	 */
	private void setFirstPostureAsExpected() {
		((GLAvatarPanel)expectedPostureContainer).setPosture(selectedExercise.getPostures().getFirst());	
	}

	protected void loadNextPosture() {
		if(!postureStack.isEmpty()){
			setExpectedPosture(postureStack.pop());
			assignedTime =  ((GLAvatarPanel)expectedPostureContainer).getPostureRenderer().getPosture().getDuration();
			delta = 360.0/assignedTime;
		}
		System.out.println("Posture loaded");
	}

	private int progressIndicator = 0;
	protected boolean exerciseTerminated = true;
	private void startSlider() {
		ExerciseView parent = this;
		new Thread(){

			public void run(){
				ExerciseView ea = parent;
				while(!exerciseTerminated&&!postureStack.isEmpty()){
					loadNextPosture();
					int counter = assignedTime;
					try {
						while(counter-->0){
							progressIndicator++;
							pb.setValue(progressIndicator);
							sleep(1000);
							counter();
							if(exerciseTerminated != false){
								return;
							}
						}					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				cManager.unsubscribeAll(ea);
				exerciseTerminated = true;
				ea.resetControls();
			}
		}.start();
	}

	protected void resetControls() {
		add(beginExercise,BorderLayout.SOUTH);
		setEnabledRadioButtons(true);
		remove(pb);
		revalidate();
		repaint();		
	}

	public JLabel getAnnResultLable() {
		return annResult;		
	}
	public JLabel getKnnResultLable() {
		return knnResult;		
	}
	public JLabel getRTreesResultLable() {
		return rTreesResult;	
	}

	public void setCMnager(IClassifierManager cManager) {
		this.cManager = cManager;		
	}

	public ClassifierManagerImpl getClassifierManager() {
		return (ClassifierManagerImpl)cManager;		
	}

	public void stopExercise() {
		animator1.stop();
		exerciseTerminated = true;
		exerciseStarted = false;
	}

	public JRadioButton getRadioBtnAnn() {
		return radioBtnAnn;
	}

	public void setRadioBtnAnn(JRadioButton radioBtnAnn) {
		this.radioBtnAnn = radioBtnAnn;
	}

	public JRadioButton getRadioBtnKnn() {
		return radioBtnKnn;
	}

	public void setRadioBtnKnn(JRadioButton radioBtnKnn) {
		this.radioBtnKnn = radioBtnKnn;
	}

	public JRadioButton getRadioBtnRTrees() {
		return radioBtnRTrees;
	}

	public void setRadioBtnRTrees(JRadioButton radioBtnRTrees) {
		this.radioBtnRTrees = radioBtnRTrees;
	}

	@Override
	public void onSkeletonFrameEvent(Posture p) {
		p.copyTo(((GLAvatarPanel)avatarContainer).getPosture());		
	}
}
