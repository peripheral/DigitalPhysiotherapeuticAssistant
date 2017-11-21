package com.app.graphics;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import com.app.entities.Posture;
import com.app.gui.view.PhysicianView;
import com.jogamp.opengl.util.FPSAnimator;

public class GLAvatarPanel extends GLJPanel implements GLEventListener,KeyListener,
									MouseWheelListener{
	//private PoseCanvas canvas = new PoseCanvas();

	private GLCanvas canvas;
	private JPanel parent = null;
	/*
	 * Contains Canvases with poses
	 */
	//private Scene scene = new Scene();
	private PostureRenderer postureRenderer = new PostureRenderer();
	private double Ry= 90,Rx=0;
	int Dist=2000;
	private GLJPanel self;

	public GLAvatarPanel(JPanel parent){
		this.parent = parent;
		self = this;
		addMouseListener(new MouseListener() {
			
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
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				self.requestFocus();				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				self.requestFocus();
			}
		});
		GLCapabilities capabilities = new GLCapabilities(null);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseWheelListener(this);
		add(canvas,BorderLayout.CENTER);
	}


	public GLAvatarPanel(JPanel parent, String label) {
		this(parent);
		add(new JLabel(label),BorderLayout.NORTH);
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent m) {
		Dist+=m.getWheelRotation();

	}

	private int rotationStepX = 2;
	private int rotationStepY = 2;
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W){			
			rotatePoseRoundXAxis(-rotationStepX);
		}
		if (e.getKeyCode() == KeyEvent.VK_S){
			rotatePoseRoundXAxis(rotationStepX);
		}

		if (e.getKeyCode() == KeyEvent.VK_A){ 
			rotatePoseRoundYAxis(rotationStepY);	}
		if (e.getKeyCode() == KeyEvent.VK_D){
			rotatePoseRoundYAxis(-rotationStepY);			
		}

		if (e.getKeyCode() == KeyEvent.VK_L){
			//showLines = !showLines;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			String jointName = postureRenderer.selectNextJoint();
			double[] coords = postureRenderer.getPosture().getJointMap().get(jointName);
			((PhysicianView)parent).setSelectedJoint(coords,jointName);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			String  jointName = postureRenderer.selectPreviousJoint();
			double[] coords = postureRenderer.getPosture().getJointMap().get(jointName);
			((PhysicianView)parent).setSelectedJoint(coords,jointName);
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
		int width = getWidth();
		int height = getHeight();
		gl.glViewport(0, 0, width,height);					// size of the window
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(57,(width*0.8f)/height,1,3000);
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
	
	public Posture getPosture(){
		return postureRenderer.getPosture();
	}

	public PostureRenderer getPostureRenderer() {
		return postureRenderer;
	}

	public void setPosture(Posture posture) {
		postureRenderer.setPosture(posture);
	}


	public void setRotationAngleY(int i) {
		Ry = i;
	}
}