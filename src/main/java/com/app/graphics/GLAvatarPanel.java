package com.app.graphics;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
									MouseWheelListener,MouseListener,MouseMotionListener{
	private GLCanvas canvas;
	private JPanel parent = null;
	private int height = 700;
	private int width = 0;
	/*
	 * Contains Canvases with poses
	 */
	private PostureRenderer postureRenderer = new PostureRenderer();
	private double Ry= 90,Rx=0;
	private float[] referencePoint = {0.0f,1000f,0.0f};
	private float fovy = 57; //Field of view
	private float aspect,zNear = 1000,zFar = 3000,widthFactor = 0.8f;
	private int dist=2000;
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
		dist+=m.getWheelRotation();

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
		

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}


	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		aspect = (width*widthFactor)/height;
		this.height = height;
		this.width = width;
		GL2 gl = drawable.getGL().getGL2(); 		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		final GLU glu = new GLU();	
		glu.gluPerspective(fovy,aspect,zNear,zFar);
		float[] v= newEyePoint(dist);
		glu.gluLookAt(v[0],v[1],v[2],referencePoint[0],referencePoint[1],referencePoint[2],0.0f,1.0f,0.0f);
		gl.glEnable(GL2.GL_CULL_VERTEX_EYE_POSITION_EXT);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glClearColor(1,1, 1, 1);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = (GL2)drawable.getGL();

		final GLU glu = new GLU();
		if(parent != null){
			parent.repaint();
		}
	//	gl.glViewport(0, 0, width,height);					// size of the window
	//	gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		glu.gluPerspective(fovy,aspect,zNear,zFar);
		

		float[] eyePoint = newEyePoint(dist);
		glu.gluLookAt(eyePoint[0],eyePoint[1],eyePoint[2],referencePoint[0],referencePoint[1]
				,referencePoint[2],0.0f,1.0f,0.0f);

		
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


	@Override
	public void mouseClicked(MouseEvent e) {
		String jointName = postureRenderer.selectJointWithMouse(e.getX(),e.getY(),
				newEyePoint(dist),fovy,referencePoint,aspect,height,width);
		double[] coords = postureRenderer.getPosture().getJointMap().get(jointName);
		((PhysicianView)parent).setSelectedJoint(coords,jointName);
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private int previousX = 0,previousY = 0;
	private boolean notSet = true;
	@Override
	public void mouseDragged(MouseEvent e) {
		int[] direction = new int[2];
		if(notSet) {
			previousX = e.getX();
			previousY = e.getY();
			notSet = false;
		}else {
			direction[0] = e.getX() -	previousX  ;
			direction[1] = e.getY() - previousY;
			previousX = e.getX();
			previousY = e.getY();
			postureRenderer.moveSelectedJoint(direction,referencePoint,newEyePoint(dist));
		}
		((PhysicianView)parent).updateTextFields(postureRenderer.getPosture().getJointMap()
				.get(postureRenderer.getActiveJoint()));
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}