package com.app.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JPanel;

import com.jogamp.opengl.util.gl2.GLUT;

import com.app.entities.Posture;
import com.app.graphics.avatar.BodyModelImpl;
import com.app.graphics.avatar.BodyModelImpl.JOINT_TAG;


public class PostureRenderer{

	private Posture posture = new Posture();
	/* Conversion to screen pixel ratio */
	private double ratio = 0.3;
	/*Joint is presented as square of pixel size */
	public final int SQUARE_HEIGHT = 40;
	/* Square radius as constant */
	public final int SQUARE_RADIUS = 5;

	/**
	 * Selected joint, initial is head
	 */
	private String selectedJoint = BodyModelImpl.JOINT_TAG.HEAD.name();
	/**
	 * Renders pose if true, default true
	 */
	private boolean render = true;
	private int head_color_idx = 0,
			shoulder_center_color_idx = 3,
			shoulder_left_color_idx = 6,
			shoulder_right_color_idx = 9,
			spine_color_idx = 12,
			elbow_left_color_idx= 15,
			elbow_right_color_idx= 18,
			wrist_left_color_idx = 21,
			wrist_right_color_idx = 24,
			hip_center_color_idx = 27,
			hip_left_color_idx = 30,
			hip_right_color_idx = 33,
			knee_left_color_idx = 36,
			knee_right_color_idx= 39,
			ankle_left_color_idx = 42,
			ankle_right_color_idx = 45,
			foot_left_color_idx = 48,
			foot_right_color_idx = 51,
			hand_left_color_idx = 54,
			hand_right_color_idx = 57;
	private double[] collorArray = {0,1,0, //Head square color idx = 0
			0,0,1, //Shoulder center square color idx = 3
			0,0,1, //Shoulder left square color idx = 6
			0,0,1, //Shoulder right square color idx = 9
			0,0,1, //Spine square color idx = 12
			0,0,1, //Elbow left square color idx = 15
			0,0,1, //Elbow right square color idx = 18
			0,0,1, //wrist left color square idx = 21
			0,0,1, //Wrist right square color idx = 24
			0,0,1, //Heap center square color idx = 27
			1,0,0, //Heap left square color idx = 30
			0,0,1, //Heap right color idx = 33
			0,0,1, //Knee left square color idx = 36
			0,0,1, //Knee right square color idx = 39
			0,0,1, //ankle left square color idx = 42
			0,0,1, //Ankle right square color idx = 45
			0,0,1, //Foot left square color idx = 48
			0,0,1, //Foot right square color idx = 51
			0,0,1,  //Hand left square color idx = 54
			0,0,1};	//Hand right square color idx = 57
	private double offsetX = 0;
	private double offsetY = 200;
	private double offsetZ= -100;



	public PostureRenderer(){
		new Thread(){
			public void run() {
				while(true){
					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(render){
						render = false;
					}else{
						render = true;
					}
				}
			};
		}.start();
	}

	public void setPosture(Posture posture) {
		this.posture = posture;
	}

	/**
	 * Scaling  x and y coordinates to fit screen 
	 * @param coords - coordinate
	 * @return - converted coords
	 */
	private int[] scale(double[] coords) {	
		int temp[] =  new int[]{(int)(coords[0]*ratio),
				(int)(coords[1]*ratio),(int)(coords[2]*ratio)};
		return temp;
	}

	public void drawPosture(GLAutoDrawable drawable){
		GL2 gl = (GL2)drawable.getGL();
		GLUT glut = new GLUT();
		gl.glTranslated(posture.getXOffset()+offsetX,posture.getYOffset()+offsetY, posture.getZOffset()+offsetZ);
		/*Drawing head square */
		gl.glPushMatrix();
		double head[] = posture.getHead();
		gl.glTranslated(head[0],head[1], head[2]);
		gl.glColor3dv(collorArray,head_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.HEAD.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		gl.glPushMatrix();
		double sc[] = posture.getShoulder_center();
		/* Drawing shoulder center square */
		//g.fillRect(sc[0]-SQUARE_RADIUS, sc[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glTranslated(sc[0],sc[1], sc[2]);
		gl.glColor3dv(collorArray, shoulder_center_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SPINE_SHOULDER.name())&& render){glut.glutSolidCube(SQUARE_HEIGHT);}glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/*Drawing line between head and shoulder center */
		//g.drawLine(head[0], head[1],sc[0],sc[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(head, 0);
		gl.glVertex3dv(sc, 0);
		gl.glEnd();

		/*Drawing left shoulder */
		double sl[] = posture.getShoulder_left();
		/* Drawing the left shoulder */
		//g.drawRect(sl[0]-SQUARE_RADIUS, sl[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(sl[0], sl[1], sl[2]);
		gl.glColor3dv(collorArray, shoulder_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SHOULDER_LEFT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from shoulder center to left shoulder */
		//g.drawLine(sc[0], sc[1],sl[0],sl[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sc, 0);
		gl.glVertex3dv(sl, 0);
		gl.glEnd();

		/* Drawing the right shoulder */
		double sr[] = posture.getShoulder_right();
		//g.fillRect(sr[0]-SQUARE_RADIUS, sr[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(sr[0], sr[1], sr[2]);
		gl.glColor3dv(collorArray, shoulder_right_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SHOULDER_RIGHT.name())&& render){glut.glutSolidCube(SQUARE_HEIGHT);}glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		//g.drawLine(sc[0], sc[1],sr[0],sr[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sc, 0);
		gl.glVertex3dv(sr, 0);
		gl.glEnd();

		/* Drawing spine square */
		//g.setColor(Color.ORANGE);
		double s[] = posture.getSpineBase();
		gl.glPushMatrix();
		gl.glTranslated(s[0], s[1], s[2]);
		gl.glColor3dv(collorArray, spine_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SPINE_BASE.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		//g.fillRect(s[0]-SQUARE_RADIUS, s[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		/*Drawing line between shoulder center and spine */
		//g.drawLine(sc[0], sc[1],s[0],s[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sc, 0);
		gl.glVertex3dv(s, 0);
		gl.glEnd();
		
		/* Drawing hip center square */
		//g.setColor(Color.ORANGE);
		double hc[] = posture.getHip_center();
		gl.glPushMatrix();
		gl.glTranslated(hc[0], hc[1], hc[2]);
		gl.glColor3dv(collorArray, hip_center_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SPINE_BASE.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(s, 0);
		gl.glVertex3dv(hc, 0);
		gl.glEnd();
		/* Drawing left hip square */
		//g.setColor(Color.ORANGE);
		double hl[] = posture.getHip_left();
		//g.fillRect(hl[0]-SQUARE_RADIUS, hl[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(hl[0], hl[1], hl[2]);
		gl.glColor3dv(collorArray, hip_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.HIP_LEFT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		gl.glColor3f(0,0, 1);
		/*Drawing line between spine and left heap */
		//g.drawLine(s[0], s[1],hl[0],hl[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(hc, 0);
		gl.glVertex3dv(hl, 0);
		gl.glEnd();
		/* Drawing right hip square */
		//g.setColor(Color.ORANGE);
		double hr[] = posture.getHip_right();
		//g.fillRect(hr[0]-SQUARE_RADIUS, hr[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(hr[0], hr[1], hr[2]);
		gl.glColor3dv(collorArray, hip_right_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.HIP_RIGHT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/*Drawing line between spine and right heap */
		//g.drawLine(s[0], s[1],hr[0],hr[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(hc, 0);
		gl.glVertex3dv(hr, 0);
		gl.glEnd();
		/* Drawing the left knee */
		/* Scaling coordin[0]s */
		double kl[] = posture.getKnee_left();
		//g.setColor(Color.GRAY);
		//g.fillRect(kl[0]-SQUARE_RADIUS, kl[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(kl[0], kl[1], kl[2]);
		gl.glColor3dv(collorArray, knee_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.KNEE_LEFT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from left hip to left knee */
		//g.drawLine(hl[0], hl[1],kl[0],kl[1]);gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(hl, 0);
		gl.glVertex3dv(kl, 0);
		gl.glEnd();
		/* Drawing the right knee */
		/* Scaling coordinates */
		double kr[] =posture.getKnee_right();
		//g.setColor(Color.GRAY);
		//g.fillRect(kr[0]-SQUARE_RADIUS, kr[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(kr[0], kr[1], kr[2]);
		gl.glColor3dv(collorArray, knee_right_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.KNEE_RIGHT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right hip to right knee */
		//g.drawLine(hr[0], hr[1],kr[0],kr[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(hr, 0);
		gl.glVertex3dv(kr, 0);
		gl.glEnd();
		/* Drawing the right ankle */
		/* Scaling coordinates */
		double ar[] =posture.getAnkle_right();
		//g.setColor(Color.GRAY);
		//g.fillRect(ar[0]-SQUARE_RADIUS, ar[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);gl.glPushMatrix();
		gl.glPushMatrix();
		gl.glTranslated(ar[0], ar[1], ar[2]);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.ANKLE_RIGHT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right knee to right ankle */
		//g.drawLine(kr[0], kr[1],ar[0],ar[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(kr, 0);
		gl.glVertex3dv(ar, 0);
		gl.glEnd();
		/* Drawing the right ankle */
		/* Scaling coordinates */
		double al[] =posture.getAnkle_left();
		//g.setColor(Color.GRAY);
		//g.fillRect(al[0]-SQUARE_RADIUS, al[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(al[0], al[1], al[2]);
		gl.glColor3dv(collorArray, ankle_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.ANKLE_LEFT.name())&& render){glut.glutSolidCube(SQUARE_HEIGHT);}glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right knee to right ankle */
		//g.drawLine(kl[0], kl[1],al[0],al[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(kl, 0);
		gl.glVertex3dv(al, 0);
		gl.glEnd();
		/* Drawing the right foot */
		/* Scaling coordinates */
		double fr[] =posture.getFoot_right();
		//g.setColor(Color.GRAY);
		//g.fillRect(fr[0]-SQUARE_RADIUS, fr[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(fr[0], fr[1], fr[2]);
		gl.glColor3dv(collorArray, foot_right_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.FOOT_RIGHT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right knee to right ankle */
		//g.drawLine(ar[0], ar[1],fr[0],fr[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(ar, 0);
		gl.glVertex3dv(fr, 0);
		gl.glEnd();
		/* Drawing the left foot */
		/* Scaling coordinates */
		double fl[] = posture.getFoot_left();
		//g.setColor(Color.GRAY);
		//g.fillRect(fl[0]-SQUARE_RADIUS, fl[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(fl[0], fl[1], fl[2]);
		gl.glColor3dv(collorArray, foot_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.FOOT_LEFT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right knee to right ankle */
		//g.drawLine(al[0], al[1],fl[0],fl[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(al, 0);
		gl.glVertex3dv(fl, 0);
		gl.glEnd();


		/*Drawing left elbow */
		/* Scaling left elbow coordinates */
		double el[] = posture.getElbow_left();
		//g.setColor(Color.MAGENTA);
		//g.drawRect(el[0]-SQUARE_RADIUS, el[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(el[0], el[1], el[2]);
		gl.glColor3dv(collorArray, elbow_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.ELBOW_LEFT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from left shoulder to left elbow */
		//g.drawLine(sl[0], sl[1],el[0],el[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sl, 0);
		gl.glVertex3dv(el, 0);
		gl.glEnd();

		/* Drawing the right elbow */
		double er[] =posture.getElbow_right();	
		//g.setColor(Color.CYAN);
		//g.fillRect(er[0]-SQUARE_RADIUS, er[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(er[0], er[1], er[2]);
		gl.glColor3dv(collorArray, elbow_right_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.ELBOW_RIGHT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right shoulder to right elbow */
		//g.drawLine(sr[0], sr[1],er[0],er[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sr, 0);
		gl.glVertex3dv(er, 0);
		gl.glEnd();
		/* Drawing the right wrist */
		/* Scaling coordinates */
		double wr[] =posture.getWrist_right();
		//g.setColor(Color.GRAY);
		//g.fillRect(wr[0]-SQUARE_RADIUS, wr[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(wr[0], wr[1], wr[2]);
		gl.glColor3dv(collorArray, wrist_right_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.WRIST_RIGHT.name())&& render){glut.glutSolidCube(SQUARE_HEIGHT);}glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right elbow to right wrist */
		//g.drawLine(er[0], er[1],wr[0],wr[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(er, 0);
		gl.glVertex3dv(wr, 0);
		gl.glEnd();
		/* Drawing the left wrist */
		/* Scaling coordinates */
		double wl[] =posture.getWrist_left();
		//g.setColor(Color.GRAY);
		//g.fillRect(wl[0]-SQUARE_RADIUS, wl[1]-SQUARE_RADIUS,SQUARE_HEIGHT,SQUARE_HEIGHT);
		gl.glPushMatrix();
		gl.glTranslated(wl[0], wl[1], wl[2]);
		gl.glColor3dv(collorArray, wrist_left_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.WRIST_LEFT.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		/* Drawing line from right elbow to right wrist */
		//g.drawLine(el[0], el[1],wl[0],wl[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(el, 0);
		gl.glVertex3dv(wl, 0);
		gl.glEnd();
	}
	
	public void selectJoint(int x, int y) {
		Map<String,double[]> jointMap = posture.getJointMap();
		Set<String> keys = posture.getJointMap().keySet();
		for(String key:keys){
			double[] coords = jointMap.get(key);
			if(isInBoundaryBox(scale(coords),SQUARE_HEIGHT/2,x,y)){
				selectedJoint = key;
			}
		}
		throw new RuntimeException("Not implemented");
	}

	public boolean isInBoundaryBox(int[] center, int radius,int x,int y){
		System.out.println("Given:" +center[0]+":"+center[1] + " Mouse:"+x+":"+y );
		if(x> center[0] -radius && x<center[0]+radius){
			if(y> center[1] -radius && y<center[1]+radius){
				return true;
			}
		}
		return false;
	}

	/**
	 * Selects next joint in map
	 * @return
	 */
	public String selectNextJoint() {
		Set<String> keys = posture.getJointMap().keySet();	
		String[] keysA = keys.toArray(new String[keys.size()]);

		for(int i=0;i< keysA.length;i++){
			if(keysA[i].equals(selectedJoint)){
				if(i==keysA.length-1){
					selectedJoint = keysA[0];
				}else{
					selectedJoint = keysA[i+1];
				}
				break;
			}
		}	
		return selectedJoint;
	}

	/**
	 * Returns pose object drawn on canvas
	 * 
	 */
	public Posture getPosture() {
		return posture;
	}

	public void setXValueOfSelectedJoint(double value) {
		double[] coords = posture.getJointMap().get(selectedJoint);
		coords[0] = value;
		posture.setJoinLocation(selectedJoint, coords);	
	}
	public void setYValueOfSelectedJoint(double value) {
		double[] coords = posture.getJointMap().get(selectedJoint);
		coords[1] = value;
		posture.setJoinLocation(selectedJoint, coords);	
	}
	public void setZValueOfSelectedJoint(double value) {
		double[] coords = posture.getJointMap().get(selectedJoint);
		coords[2] = value;
		posture.setJoinLocation(selectedJoint, coords);	
	}

	public String selectPreviousJoint() {
		Set<String> keys = posture.getJointMap().keySet();	
		String[] keysA = keys.toArray(new String[keys.size()]);

		for(int i=keysA.length-1;i> -1;i--){
			if(keysA[i].equals(selectedJoint)){
				if(i==0){
					selectedJoint = keysA[keysA.length-1];
				}else{
					selectedJoint = keysA[i-1];
				}
				break;
			}
		}	
		return selectedJoint;
	}

	public String getActiveJoint() {
		return selectedJoint;
	}
}
