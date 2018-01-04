package com.app.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JPanel;

import com.jogamp.opengl.util.gl2.GLUT;

import com.app.entities.Posture;
import com.app.graphics.avatar.BodyModelImpl;
import com.app.graphics.avatar.BodyModelImpl.JOINT_TAG;
import com.app.utility.MathUtils;


public class PostureRenderer{

	private Posture posture = new Posture();
	/* Conversion to screen pixel ratio */
	private double ratio = 0.3;
	/*Joint is presented as square of pixel size */
	public final int SQUARE_HEIGHT = 30;
	/* Square radius as constant */
	public final int SQUARE_RADIUS = 3;

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
			spine_base_color_idx = 12,
			elbow_left_color_idx= 15,
			elbow_right_color_idx= 18,
			wrist_left_color_idx = 21,
			wrist_right_color_idx = 24,
			spine_mid_color_idx = 27,
			hip_left_color_idx = 30,
			hip_right_color_idx = 33,
			knee_left_color_idx = 36,
			knee_right_color_idx= 39,
			ankle_left_color_idx = 42,
			ankle_right_color_idx = 45,
			foot_left_color_idx = 48,
			foot_right_color_idx = 51,
			hand_left_color_idx = 54,
			hand_right_color_idx = 57,
			neck_color_idx = 60;
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
			0,0,1,//Hand right square color idx = 57
			0,0,1};	//Neck square color idx = 57
	private double offsetX = 0;
	private double offsetY = 200;
	private double offsetZ = 0;



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


		/*Drawing neck square */
		gl.glPushMatrix();
		double[] neck = posture.getNeck();
		gl.glTranslated(neck[0],neck[1], neck[2]);
		gl.glColor3dv(collorArray,neck_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.NECK.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();
		gl.glPushMatrix();
		/*Drawing line between head and neck center */
		//g.drawLine(head[0], head[1],sc[0],sc[1]);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(head, 0);
		gl.glVertex3dv(neck, 0);
		gl.glEnd();

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
		gl.glVertex3dv(neck, 0);
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


		/* Drawing spine mid square */
		//g.setColor(Color.ORANGE);
		double sm[] = posture.getSpineMid();
		gl.glPushMatrix();
		gl.glTranslated(sm[0], sm[1], sm[2]);
		gl.glColor3dv(collorArray, spine_mid_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SPINE_MID.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();

		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sc, 0);
		gl.glVertex3dv(sm, 0);
		gl.glEnd();

		/* Drawing spine base square */
		//g.setColor(Color.ORANGE);
		double sb[] = posture.getSpineBase();
		gl.glPushMatrix();
		gl.glTranslated(sb[0], sb[1], sb[2]);
		gl.glColor3dv(collorArray, spine_base_color_idx);
		if(selectedJoint.equals(BodyModelImpl.JOINT_TAG.SPINE_BASE.name())&& render){
			glut.glutSolidCube(SQUARE_HEIGHT);
		}
		glut.glutWireCube(SQUARE_HEIGHT);
		gl.glPopMatrix();

		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3dv(sm, 0);
		gl.glVertex3dv(sb, 0);
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
		gl.glVertex3dv(sb, 0);
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
		gl.glVertex3dv(sb, 0);
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
	 * Selects joint from mouse click
	 * @param k 
	 * @param j 
	 * @param referencePoint 
	 * @param fovy 
	 * @param aspect 
	 * @param height 
	 * @param width 
	 * @param fs 
	 * @return
	 */
	public String selectJointWithMouse(int x, int y, float[] eye, float fovy, 
			float[] referencePoint, float aspect, int height, int width) {
		int clickRadius = (int) (15*700.0/height/2);
		System.out.println("Click box:"+clickRadius);
		System.out.println("X:"+x+" Y:"+y+" height:"+height);
		Map<String, double[]> joints = posture.getJointMap();	
		Set<String> keys = joints.keySet();
		int[] position;
		double[] joint = new double[3];

		for(String key :keys){
			joint= Arrays.copyOf(joints.get(key),joint.length);
			joint[0] = joint[0]+offsetX;
			joint[1] = joint[1]+offsetY;
			joint[2] = joint[2]+offsetZ;
			
			position = project(eye,referencePoint,fovy,joint,aspect,height,width);
			if(key.contains("HEAD")||key.contains("FOOT")||key.contains("WRIST")) {
				System.out.println("Joint name:"+key+" x:"+x+" y:"+y + " pos:"+Arrays.toString(position));
			}
			if(isWithinBoundary(x,y,position[0],position[1],clickRadius)) {
				selectedJoint = key;
			}
		}	
		return selectedJoint;
	}

	private boolean isWithinBoundary(int x, int y, int jointX, int jointY, int boxWidth) {
		if(jointX - boxWidth < x && jointX + boxWidth > x ) {
			if(jointY - boxWidth < y && jointY + boxWidth > y) {
				return true;

			}
		}
		return false;
	}

	private int[] project(float[] eye, float[] referencePoint, float fovy,
			double[] joint, float aspect, int height, int width) {
		int[] loc = {0,0};
		int portHeight = (int) (height * 0.93f);
		//	System.out.println("Eye:"+Arrays.toString(eye));
		//System.out.println("Referencepoint:"+Arrays.toString(referencePoint));
		//Calculate normal to the clipping plane
		float[] normalER = {referencePoint[0]-eye[0],referencePoint[1]-eye[1],
				referencePoint[2]-eye[2]};
		//	System.out.println("Normal:"+Arrays.toString(normal));

		//Vector from joint to eye
		float[] vectorJE = {eye[0]-(float)joint[0],eye[1]-(float)joint[1],eye[2]-(float)joint[2]};
		//Y vector
		float[] vecY = {0,1,0};

		//Get vector X clipping win
		float[] vecXClipUnit = MathUtils.getCrossProduct(normalER,vecY);
		vecXClipUnit = MathUtils.divideVectorWithConstant(vecXClipUnit, MathUtils.getVectorAbsolute(vecXClipUnit));
		//Get vector Y clipping win
		float[] vecYClipUnit = MathUtils.getCrossProduct(vecXClipUnit,normalER);
		vecYClipUnit = MathUtils.divideVectorWithConstant(vecYClipUnit, MathUtils.getVectorAbsolute(vecYClipUnit));

		//Project vectorJA on the normal
		float[] projecOnNorm = MathUtils.projectVectorOnVector(vectorJE,normalER);

		//	System.out.println("Project on norm:"+MathUtils.getVectorAbsolute(projecOnNorm)+" Normal abs:"+MathUtils.getVectorAbsolute(normal));
		//Calculate projection on clipping plane
		float[] projecOnPlane = MathUtils.subtract(vectorJE,projecOnNorm);

		//	System.out.println("Project On Plane:"+Arrays.toString(projecOnPlane)+" Abs:"+MathUtils.getVectorAbsolute(projecOnPlane));
		//Project on X vector of clipping plane
		float[] projectionX = MathUtils.projectVectorOnVector(projecOnPlane,vecXClipUnit);
		//		System.out.println("Project On xVector:"+Arrays.toString(projectionX)+" Abs:"+MathUtils.getVectorAbsolute(projectionX));
		//Project on Y vector of clipping plane
		float[] projectionY = MathUtils.projectVectorOnVector(projecOnPlane,vecYClipUnit);
		//		System.out.println("Project On yVector:"+Arrays.toString(projectionX)+" Abs:"+MathUtils.getVectorAbsolute(projectionY));
		float factorPerspective = MathUtils.getVectorAbsolute(projecOnNorm);	


		//	System.out.println("Port Height:"+portHeight);
		float portWidth = height*aspect*portHeight/700;
		//	System.out.println("Port Height:"+portHeight + " Port Width:"+portWidth+" Aspect:"+aspect);
		float scalarX = Math.signum(MathUtils.getScalarProd(projectionX, vecXClipUnit));
		float scalarY = Math.signum(MathUtils.getScalarProd(projectionY, vecYClipUnit));
		float xAbsolut = MathUtils.getVectorAbsolute(projectionX)/factorPerspective*scalarX;
		float yAbsolut = MathUtils.getVectorAbsolute(projectionY)/factorPerspective*scalarY;
		//		System.out.println("MaxWidth att abs(projectOnNorm):"+maxWidth+" projectY:"+MathUtils.getVectorAbsolute(projectionY));
		//		System.out.println("Projection on X absolute:"+ xAbsolut);
		//		System.out.println("Projection on Y absolute:"+ yAbsolut);
		//		System.out.println("Height:"+height);
		float xLoc = -xAbsolut;
		float yLoc = yAbsolut;
				System.out.println("xLoc:"+xLoc+" yLoc:"+yLoc);
		float Vc = height/2 + (portHeight*yLoc);
		//		System.out.println("ASpect:"+aspect);

		float Hc = width/2  + (portWidth*xLoc*1.2f);
		//	System.out.println("Width:"+width+" Port width:"+portWidth);
		
		//		System.out.println("Vc:"+Vc+" Hc:"+Hc);
		loc[0] = (int) Hc;loc[1] = (int) Vc;
		return loc;
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

	public void moveSelectedJoint(int[] direction,float[] referencePoint,float[] eye) {
		double[] joint = posture.getJointMap().get(selectedJoint);
		float[] normalER = {referencePoint[0]-eye[0],referencePoint[1]-eye[1],
				referencePoint[2]-eye[2]};

		//Y vector
		float[] vecY = {0,1,0};

		//Get vector X clipping win
		float[] vecXClipUnit = MathUtils.getCrossProduct(normalER,vecY);
		vecXClipUnit = MathUtils.divideVectorWithConstant(vecXClipUnit, MathUtils.getVectorAbsolute(vecXClipUnit));
		//Get vector Y clipping win
		float[] vecYClipUnit = MathUtils.getCrossProduct(vecXClipUnit,normalER);
		vecYClipUnit = MathUtils.divideVectorWithConstant(vecYClipUnit, MathUtils.getVectorAbsolute(vecYClipUnit));
		float slope = (float)direction[1]/direction[0];
		joint[0] = joint[0] + vecXClipUnit[0]*direction[0]+vecYClipUnit[0]*direction[1];
		joint[1] = joint[1] + vecXClipUnit[1]*direction[0]+vecYClipUnit[1]*direction[1];
		joint[2] = joint[2] + vecXClipUnit[2]*direction[0]+vecYClipUnit[2]*direction[1];
	}
}
