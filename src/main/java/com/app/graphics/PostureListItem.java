package com.app.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.app.entities.Posture;
import java.awt.event.KeyListener;

public class PostureListItem extends GLJPanel implements GLEventListener,KeyListener,MouseWheelListener {

	private JPanel parent = null;
	/*
	 * Contains Canvases with poses
	 */
	private PostureRenderer postureRenderer = new PostureRenderer();
	private Dimension size = new Dimension(100,100);
	private Dimension sizeLarge = new Dimension(100,100);
	private double Ry=90,Rx=0;
	private int Dist=2100;

	public PostureListItem(JPanel parent,Posture p){
		this.parent = parent;
		JButton btn = new JButton();
		Icon i = new ImageIcon("cross.png");
		btn.setIcon(i);
		btn.setAlignmentX(LEFT_ALIGNMENT);
		btn.setAlignmentY(TOP_ALIGNMENT);
		btn.setMaximumSize(new Dimension(16,16));
		PostureListItem item = this;
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(parent != null){
					((PostureList)parent).removeListItem(item);	
				}
			}
		});
		
		addGLEventListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
		postureRenderer.setPosture(p);
		setSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		Border b = BorderFactory.createLineBorder(Color.black);
		setBorder(b);
		setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		add(btn);
	}


	public PostureListItem(JPanel parent,Posture p, int width,int height) {
		this(parent,p);
		//add(new JLabel(label),BorderLayout.NORTH);
		size.setSize(width, height);
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent m) {
		Dist+=m.getWheelRotation();
	}

	public PostureRenderer getScene() {
		return postureRenderer;
	}	

	private int rotationStepX = 2;
	private int rotationStepY = 2;
	@Override
	public void keyPressed(KeyEvent e)
	{
		System.out.println("key press");
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){

			System.exit(0);}

		if (e.getKeyCode() == KeyEvent.VK_W){			
			rotatePoseRoundXAxis(-rotationStepX);
		}
		if (e.getKeyCode() == KeyEvent.VK_S){
			rotatePoseRoundXAxis(rotationStepX);
		}

		if (e.getKeyCode() == KeyEvent.VK_A){ 
			rotatePoseRoundYAxis(rotationStepY);	
		}
		if (e.getKeyCode() == KeyEvent.VK_D){
			rotatePoseRoundYAxis(-rotationStepY);			
		}

		if (e.getKeyCode() == KeyEvent.VK_L){
			//showLines = !showLines;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			String joinName = postureRenderer.selectNextJoint();
			double[] coords = postureRenderer.getPosture().getJointMap().get(joinName);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			//poseCanvas.previousJoint();
		}

	}

	/**
	 * Rotation around X axis in degree
	 * @param degree
	 */
	private void rotatePoseRoundXAxis(int degree) {
		Rx=Rx+degree;  		
	}

	/**
	 * Rotation around X axis in degree
	 * @param degree
	 */
	private void rotatePoseRoundYAxis(double degree) {
		Ry=Ry+degree;		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

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

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = (GL2)drawable.getGL();

		final GLU glu = new GLU();
		if(parent != null){
			parent.repaint();
		}

		gl.glViewport(0, 0, size.width,size.height);					// size of the window
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();


		glu.gluPerspective(57,(size.getWidth()*1.0f)/size.getHeight(),1,3000);
		//45 = field of view ,width/height = aspect ratio , 1 = near clipping plane 20 = far clipping plane 
		//http://pyopengl.sourceforge.net/documentation/ref/glu/perspective.html
		gl.glEnable(GL2.GL_CULL_VERTEX_EYE_POSITION_EXT);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);

		float[] eyePoint = newEyePoint(Dist);
		glu.gluLookAt(eyePoint[0],eyePoint[1],eyePoint[2],0.0f,1000f,0.0f,0.0f,1.0f,0.0f);
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
		postureRenderer.drawPosture(drawable);
	}

	private float[] newEyePoint(int r) {
		float[] points= new float[3];
		points[0] = (float) (r*Math.cos(Math.toRadians(Ry))*Math.cos(Math.toRadians(Rx)));
		points[2] = (float) (r*Math.sin(Math.toRadians(Ry))*Math.cos(Math.toRadians(Rx)));
		points[1] = (float) (r*Math.sin(Math.toRadians(Rx))+1000);
		return points;
	}

	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Posture[] postures = new Posture[5];
		for(int i = 0;i <postures.length;i++){
			postures[i] = new Posture();
		}		

		PostureListItem postureItem = new PostureListItem(null,postures[0]);
		frame.add(postureItem,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		postureItem.display();
	}

	public Posture getPosture() {
		return postureRenderer.getPosture();
	}
}


