package app.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;





import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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

import app.Pose;
import app.PoseCanvas;
import app.PoseReader;
import app.classifier_wrappers.Scence;


public class UserInterface extends JFrame implements GLEventListener,KeyListener,MouseWheelListener {
	public final static int DEFAULT_WIDTH = 800;
	public final static int DEFAULT_HEIGHT = 700;
	private static final Dimension PREFFERED_FRAME_SIZE=new Dimension(400,800);
	//private PoseCanvas canvas = new PoseCanvas();
	private GLCanvas canvas;

	private int Rx=90,Ry=0,Dist=2000;
	private SpinnerNumberModel xValue;
	private SpinnerNumberModel yValue;
	private SpinnerNumberModel zValue;
	private JLabel nnResult = new JLabel("0-40%");
	private JLabel treeResult = new JLabel("0-40%");
	private JLabel knnResult = new JLabel("0-40%");
	private JLabel nnRate = new JLabel("N/A");
	private JLabel treeRate= new JLabel("N/A");
	private JLabel knnRate = new JLabel("N/A");
	/*
	 * Contains Canvases with poses
	 */
	private Scence scene = new Scence();

	public UserInterface(){
		super("Window");
		setUpControls();		
		GLCapabilities capabilities = new GLCapabilities(null);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseWheelListener(this);
		setPreferredSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));	
		pack();		
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});	

		add(canvas,BorderLayout.CENTER);
		FPSAnimator animator = new FPSAnimator(25);			//Animator reloads display 10 times/sec normal 60  fps
		animator.add(canvas);
		animator.start();
	}

	/**
	 * Controls of UI
	 */
	private void setUpControls() {
		JButton btn = new JButton("Print training set");
		JButton exportPose = new JButton("Export pose");

		JButton importPose = new JButton("Import pose");
		JButton playRecorded = new JButton("Play Recorded");
		playRecorded.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(){

					@Override
					public void run(){
						PoseReader pr = new PoseReader(new File("standing_pose_R.csv"));
						while(true){
							Pose p = pr.nextSample();
							if(p != null){
								scene.getObservedPose().setPose(p);
							}
							System.out.println("New Change ::::");
							try {
								sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				};
				t.start();
			}
		});

	
		xValue =
				new SpinnerNumberModel(scene.getExpectedPose().getPose().getHead()[0], //initial value
						-2000.0, //min
						2000.0, //max
						1.0);
		xValue.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				scene.getExpectedPose().setMoveJointOnX((Double)xValue.getValue());
			}
		});
		yValue =
				new SpinnerNumberModel(scene.getExpectedPose().getPose().getHead()[1], //initial value
						-2000.0, //min
						2000.0, //max
						1.0);
		yValue.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				scene.getExpectedPose().setMoveJointOnY((Double)yValue.getValue());
			}
		});
		zValue =
				new SpinnerNumberModel(scene.getExpectedPose().getPose().getHead()[2], //initial value
						-2000.0, //min
						2000.0, //max
						1.0);
		zValue.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				scene.getExpectedPose().setMoveJointOnZ((Double)zValue.getValue());
			}
		});
		JSpinner xSpinner = new JSpinner(xValue);
		JSpinner ySpinner = new JSpinner(yValue);
		JSpinner zSpinner = new JSpinner(zValue);
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(4, 1));
		controls.add(xSpinner);
		controls.add(ySpinner);
		controls.add(zSpinner);
		JPanel resultPanel = new JPanel();
		JPanel btnGroup = new JPanel();
		btnGroup.setLayout(new GridLayout(3, 3));
		btnGroup.add(new JLabel("NNResult:"));	
		btnGroup.add(new JLabel("Random Forests:"));
		btnGroup.add(new JLabel("K - NN:"));
		btnGroup.add(nnResult);
		btnGroup.add(treeResult);
		btnGroup.add(knnResult);
		btnGroup.add(nnRate);
		btnGroup.add(treeRate);
		btnGroup.add(knnRate);
		resultPanel.add(btnGroup);
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());
		btnPanel.add(btn);
		btnPanel.add(exportPose);
		btnPanel.add(importPose);
		btnPanel.add(playRecorded);

		add(btnPanel,BorderLayout.NORTH);
		add(resultPanel,BorderLayout.SOUTH);
		add(controls,BorderLayout.WEST);

		}

		public UserInterface(int width, int height){
			super("Window");
			setPreferredSize(new Dimension(width,height));
			setLayout(new FlowLayout());
			add(new JButton("Print training set"));
			pack();
			setVisible(true);
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
				}
			});	
			GLCapabilities capabilities = new GLCapabilities(null);
			canvas = new GLCanvas(capabilities);
			canvas.addGLEventListener(this);
			canvas.addKeyListener(this);
			canvas.addMouseWheelListener(this);
			add(canvas,BorderLayout.CENTER);

			FPSAnimator animator = new FPSAnimator(canvas, 25);			//Animator reloads display 10 times/sec normal 25  fps

			animator.start();
		}

		public Dimension getPrefferedSize(){
			return PREFFERED_FRAME_SIZE;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent m) {
			Dist+=m.getWheelRotation();

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			//	  System.out.println("key press");
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE){

				System.exit(0);}

			if (e.getKeyCode() == KeyEvent.VK_W){
				Ry=Ry+2;   }
			if (e.getKeyCode() == KeyEvent.VK_S){
				Ry=Ry-2;}

			if (e.getKeyCode() == KeyEvent.VK_A){
				Rx=Rx-1;   }
			if (e.getKeyCode() == KeyEvent.VK_D){
				Rx=Rx+1;}

			if (e.getKeyCode() == KeyEvent.VK_L){
				//showLines = !showLines;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT){
				String joinName = scene.getExpectedPose().nextJoint();
				double[] coords = scene.getExpectedPose().getPose().getJointMap().get(joinName);
				xValue.setValue(coords[0]);
				yValue.setValue(coords[1]);
				zValue.setValue(coords[2]);
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT){
				//poseCanvas.previousJoint();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			GL2 gl = (GL2)drawable.getGL();

			final GLU glu = new GLU();

			gl.glViewport(0, 0, 300,this.getHeight());					// size of the window
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();

			int width =600,height=480;
			glu.gluPerspective(57,(300*1.0f)/height,1,3000);
			//45 = field of view ,width/height = aspect ratio , 1 = near clipping plane 20 = far clipping plane 
			//http://pyopengl.sourceforge.net/documentation/ref/glu/perspective.html
			gl.glEnable(GL2.GL_CULL_VERTEX_EYE_POSITION_EXT);
			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LESS);

			float[] eyePoint = newEyePoint(Dist);
			glu.gluLookAt(eyePoint[0],eyePoint[1],eyePoint[2],0.0f,1000f,0.0f,0.0f,1.0f,0.0f);
			System.out.println("Eye point y coord"+eyePoint[1]);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(1,1, 1, 1);
			/* X axel blue */
			gl.glColor3d(0, 0, 1);
			gl.glBegin(GL2.GL_LINE_STRIP);
			gl.glVertex3d(-100, 0, 0);
			gl.glVertex3d(100, 0, 0);
			gl.glEnd();

			/* Y axel green */
			gl.glColor3d(0, 1, 0);
			gl.glBegin(GL2.GL_LINE_STRIP);
			gl.glVertex3d(0, -100, 0);
			gl.glVertex3d(0,100, 0);
			gl.glEnd();

			/* Z axel red */
			gl.glColor3d(1, 0, 0);
			gl.glBegin(GL2.GL_LINE_STRIP);
			gl.glVertex3d(0, 0, -100);
			gl.glVertex3d(0, 0, 100);
			gl.glEnd();

			gl.glColor3d(0.8,0.8,0.8);
			gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex3d(-1,-1,0);
			gl.glVertex3d(-1,1,0);
			gl.glVertex3d(1,1,0);
			gl.glVertex3d(1,-1,0);
			gl.glEnd();
			scene.getExpectedPose().drawPose(drawable);
			gl.glViewport(300, 0, 300,this.getHeight());					// size of the window
			scene.getObservedPose().drawPose(drawable);
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void init(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub

		}

		private float[] newEyePoint(int r) {
			float[] points= new float[3];
			points[0] = (float) (r*Math.cos(Math.toRadians(Rx))*Math.cos(Math.toRadians(Ry)));
			points[2] = (float) (r*Math.sin(Math.toRadians(Rx))*Math.cos(Math.toRadians(Ry)));
			points[1] = (float) (r*Math.sin(Math.toRadians(Ry))+1000);
			System.out.println("x="+Rx+" y="+Ry+" z="+points[2]);
			return points;
		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			GL2 gl = drawable.getGL().getGL2(); 		
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			final GLU glu = new GLU();
			//glu.gluOrtho2D(0.0, 450.0, 0.0, 375.0);	
			glu.gluPerspective(57,(width*1.0f)/height,1,1000);
			float[] v= newEyePoint(Dist);
			glu.gluLookAt(v[0],v[1],v[2],0.0f,200f,0.0f,0.0f,1.0f,0.0f);
			//glu.gluOrtho2D(0.0, 20.0, 0.0, 40.0);
		}

		public Scence getScene() {
			return scene;
		}

		public void setTreeClassificationResult(String label) {
			System.out.println("Calculated label"+label);
			treeResult.setText(label);
		}	
	}
