package com.app.graphics.avatar;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.app.entities.Posture;
import com.app.gui.GraphicalUserInterface;
import com.app.utility.MathUtils;

/** Provides model of the body, also specifies method of deviation of features */
public class BodyModelImpl implements IBodyModel{

	public static final double DEFAULT_INNER_LIMIT = 45;
	public static final double DEFAULT_OUTER_LIMIT = 180;
	/* HEad dimensions, HH - head height, HW - head width */
	public static int HH = 225;
	public static int HW = 200;

	/* Center of the skeleton */
	public static int CENTER= 0;

	/* Distance */
	private double head_to_center = 0;
	private double shoulder_center_to_shoulder = 0;
	private double shoulder_to_elbow = 0;
	private double elbow_to_wrist = 0;
	private double shoulder_center_to_spine = 0;
	private double spine_to_hip = 0;
	private double hip_to_knee = 0;
	private double knee_to_ankle = 0;
	private double ankle_to_foot = 0;

	private int maxA1 = 110;
	private int maxA2 = 110;
	private int maxA3 = 180;
	private int maxA4 = 110;
	private int maxA5 = 110;
	private int maxA6 = 20;
	private int maxA7 = 20;
	private int maxA8 = 180;
	private int maxA9 = 45;
	private int maxA10 = 180;
	private int maxA11 = 180;
	private int maxA12 = 180;
	private int maxA13 = 170;
	private int maxA14 = 170;
	private int maxA15 = 110;
	private int maxA16 = 110;
	private int maxA17 = 180;
	private int maxA18 = 180;
	private int maxA19 = 180;
	private float[] innerLimit = null;


	public double getHead_to_center() {
		return head_to_center;
	}

	public void setHead_to_center(double head_to_center) {
		this.head_to_center = head_to_center;
	}

	public double getShoulder_center_to_shoulder() {
		return shoulder_center_to_shoulder;
	}

	public void setShoulder_center_to_shoulder(double shoulder_center_to_shoulder) {
		this.shoulder_center_to_shoulder = shoulder_center_to_shoulder;
	}

	public double getShoulder_to_elbow() {
		return shoulder_to_elbow;
	}

	public void setShoulder_to_elbow(double shoulder_to_elbow) {
		this.shoulder_to_elbow = shoulder_to_elbow;
	}

	public double getElbow_to_wrist() {
		return elbow_to_wrist;
	}

	public void setElbow_to_wrist(double elbow_to_wrist) {
		this.elbow_to_wrist = elbow_to_wrist;
	}

	public double getHip_to_knee() {
		return hip_to_knee;
	}

	public void setHip_to_knee(double hip_to_knee) {
		this.hip_to_knee = hip_to_knee;
	}

	public double getKnee_to_ankle() {
		return knee_to_ankle;
	}

	public void setKnee_to_ankle(double knee_to_ankle) {
		this.knee_to_ankle = knee_to_ankle;
	}

	public double getAnkle_to_foot() {
		return ankle_to_foot;
	}

	public void setAnkle_to_foot(double ankle_to_foot) {
		this.ankle_to_foot = ankle_to_foot;
	}


	private double head[] = null;
	private double shoulder_left[] = null;
	private double shoulder_right[] = null;
	private double shoulder_center[] = null;
	private double elbow_left[] = null;
	private double elbow_right[] = null;
	private double hand_left[] = null;
	private double hand_right[] = null;
	private double wrist_left[] = null;
	private double wrist_right[] = null;
	private double spine[] = null;
	private double hip_left[] = null;
	private double hip_right[] = null;
	private double knee_left[] = null;
	private double knee_right[] = null;
	private double ankle_left[] = null;
	private double ankle_right[] = null;
	private double foot_left[] = null;
	private double foot_right[] = null;
	private List<Integer> outerLimit = new LinkedList<>();
	public static enum JOINT_TAG{HEAD,NECK,SHOULDER_LEFT,SHOULDER_RIGHT,SPINE_SHOULDER,
		ELBOW_LEFT,ELBOW_RIGHT,HAND_LEFT,HAND_RIGHT,WRIST_LEFT,WRIST_RIGHT,SPINE_BASE,
		HIP_LEFT,HIP_RIGHT,KNEE_LEFT,KNEE_RIGHT,ANKLE_LEFT,ANKLE_RIGHT,
		FOOT_LEFT,FOOT_RIGHT,HAND_TIP_LEFT,HAND_TIP_RIGHT, THUMB_LEFT, THUMB_RIGHT, SPINE_MID};

		public BodyModelImpl(){
			/* Total height is 9 heads, position of the head
			 *  is in the last 9 section*/
			head = new double[3];
			head[0] = CENTER;
			head[1] = BodyModelImpl.HH*8 - BodyModelImpl.HH/2;
			head[2] = 0;

			shoulder_center = new double[3];
			shoulder_center[0] =  CENTER;
			shoulder_center[1] =  BodyModelImpl.HH*7 - BodyModelImpl.HH/8 ;
			shoulder_center[2] =  0;			

			shoulder_left = new double [3];
			shoulder_left[0] =  CENTER + BodyModelImpl.HW*3/4;
			shoulder_left[1] =  BodyModelImpl.HH*7 - BodyModelImpl.HH/3;
			shoulder_left[2] =  0;

			shoulder_right = new double [3];
			shoulder_right[0] =  CENTER - BodyModelImpl.HW*3/4;
			shoulder_right[1] =  BodyModelImpl.HH*7 - BodyModelImpl.HH/3;
			shoulder_right[2] =  0;

			elbow_right = new double [3];
			elbow_right[0] =  CENTER - ( BodyModelImpl.HW +BodyModelImpl.HW/5 );
			elbow_right[1] =  BodyModelImpl.HH*6 - BodyModelImpl.HH*4/5;
			elbow_right[2] =  0;

			elbow_left = new double [3];
			elbow_left[0] =  CENTER + (BodyModelImpl.HW + BodyModelImpl.HW/5);
			elbow_left[1] =  BodyModelImpl.HH*6 - BodyModelImpl.HH*4/5;
			elbow_left[2] =  0;

			hip_left = new double [3];
			hip_left[0] =  CENTER + BodyModelImpl.HW/2;
			hip_left[1] =  BodyModelImpl.HH*5 - BodyModelImpl.HH/3;
			hip_left[2] =  0;

			spine = new double [3];
			spine[0] =  CENTER;
			spine[1] =  BodyModelImpl.HH*5;
			spine[2] =  0;

			hip_right = new double [3];
			hip_right[0] =  CENTER - BodyModelImpl.HW/2;
			hip_right[1] =  BodyModelImpl.HH*5 - BodyModelImpl.HH/3;
			hip_right[2] =  0;

			wrist_right = new double[3];
			wrist_right[0] =  CENTER - BodyModelImpl.HW;
			wrist_right[1] =  BodyModelImpl.HH*4 - BodyModelImpl.HH/2;
			wrist_right[2] =  0;

			wrist_left = new double[3];
			wrist_left[0] =  CENTER + BodyModelImpl.HW ;
			wrist_left[1] =  BodyModelImpl.HH*4 - BodyModelImpl.HH/2;
			wrist_left[2] =  0;

			knee_left = new double[3];
			knee_left[0] =  CENTER + BodyModelImpl.HW/2 ;
			knee_left[1] =  BodyModelImpl.HH*3 - BodyModelImpl.HH/2;
			knee_left[2] =  0;

			knee_right = new double[3];
			knee_right[0] =  CENTER - BodyModelImpl.HW/2 ;
			knee_right[1] =  BodyModelImpl.HH*3 - BodyModelImpl.HH/2;
			knee_right[2] =  0;

			ankle_left = new double[3];
			ankle_left[0] =  CENTER + BodyModelImpl.HW/3 ;
			ankle_left[1] =  BodyModelImpl.HH/4;
			ankle_left[2] =  0;			

			ankle_right = new double[3];
			ankle_right[0] =  CENTER - BodyModelImpl.HW/3 ;
			ankle_right[1] =  BodyModelImpl.HH/4;
			ankle_right[2] =  0;

			foot_left = new double[3];
			foot_left[0] =  CENTER + BodyModelImpl.HW/2;
			foot_left[1] =  BodyModelImpl.HH/7;
			foot_left[2] =  0 - BodyModelImpl.HH - BodyModelImpl.HH/10;

			foot_right = new double[3];
			foot_right[0] =  CENTER - BodyModelImpl.HW/2 ;
			foot_right[1] =  BodyModelImpl.HH/7;
			foot_right[2] =  0 - BodyModelImpl.HH - BodyModelImpl.HH/10;

			head_to_center = MathUtils.getDistance(head,shoulder_center);
			shoulder_center_to_shoulder = MathUtils.getDistance(shoulder_left,shoulder_center);
			shoulder_to_elbow = MathUtils.getDistance(shoulder_right,elbow_right);
			shoulder_center_to_spine = MathUtils.getDistance(spine,shoulder_center);
			spine_to_hip = MathUtils.getDistance(spine,hip_left);
			elbow_to_wrist = MathUtils.getDistance(elbow_left,wrist_left);
			hip_to_knee = MathUtils.getDistance(hip_left,knee_left);
			knee_to_ankle = MathUtils.getDistance(knee_left,ankle_left);
			ankle_to_foot = MathUtils.getDistance(ankle_left,foot_left);

			outerLimit.add(maxA1);
			outerLimit.add(maxA2);
			outerLimit.add(maxA3);
			outerLimit.add(maxA4);
			outerLimit.add(maxA5);
			outerLimit.add(maxA6);
			outerLimit.add(maxA7);
			outerLimit.add(maxA8);
			outerLimit.add(maxA9);
			outerLimit.add(maxA10);
			outerLimit.add(maxA11);
			outerLimit.add(maxA12);
			outerLimit.add(maxA13);
			outerLimit.add(maxA14);
			outerLimit.add(maxA15);
			outerLimit.add(maxA16);
			outerLimit.add(maxA17);
			outerLimit.add(maxA18);
			outerLimit.add(maxA19);
			innerLimit = new float[outerLimit.size()];
			for(int i = 0; i < outerLimit.size();i++){
				innerLimit[i] = outerLimit.get(i)/2;
			}
		}

		public double getSpine_to_hip() {
			return spine_to_hip;
		}

		public void setSpine_to_hip(double spine_to_hip) {
			this.spine_to_hip = spine_to_hip;
		}

		public double getShoulder_center_spine() {
			return shoulder_center_to_spine;
		}

		public void setShoulder_center_spine(double shoulder_center_spine) {
			this.shoulder_center_to_spine = shoulder_center_spine;
		}

		/**
		 * Note implemented
		 * Multiplying all points of the joints with 
		 * @param p
		 * @param scale
		 */
		public Posture resizePose(Posture p, double scale) {
			//TODO Not implemented
			Map<String,double[]> joints = p.getJointMap();
			Set<String> keys = joints.keySet();
			for(String key:keys){
				//p
			}
			throw new RuntimeException("Not implemented");
		}




		@Override
		public double getA1Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HEAD.name()));
		}

		@Override
		public double getA2Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.HEAD.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));			
		}

		@Override
		public double getA3Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.HEAD.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name()));	
		}

		@Override
		public double getA4Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));	
		}

		@Override
		public double getA5Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));	
		}

		@Override
		public double getA6Angle(Posture p) {
			double[] vector1 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));
			double[] normal = MathUtils.getNormal(vector1,vector2);
			return MathUtils.getAngleBetweenVectors(normal,vector3);	
		}



		@Override
		public double getA7Angle(Posture p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleBetweenVectors(normal,vector3);	
		}

		@Override
		public double getA8Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.SPINE_BASE.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
		}

		@Override
		public double getA9Angle(Posture p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_BASE.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_BASE.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE_BASE.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_SHOULDER.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleBetweenVectors(normal,vector3);	
		}

		@Override
		public double getA10Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name()));
		}

		@Override
		public double getA11Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name()));
		}

		@Override
		public double getA12Angle(Posture p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleBetweenVectors(normal,vector3);	
		}

		@Override
		public double getA13Angle(Posture p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.SPINE_BASE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleBetweenVectors(normal,vector3);	
		}

		@Override
		public double getA14Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name()));
		}

		@Override
		public double getA15Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.ANKLE_LEFT.name()));
		}

		@Override
		public double getA16Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.ANKLE_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.FOOT_LEFT.name()));
		}

		@Override
		public double getA17Angle(Posture p) {
			return MathUtils.getAngleFromPoints(p.getJointMap().get(JOINT_TAG.ANKLE_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.FOOT_RIGHT.name()));
		}

		@Override
		public double getA18Angle(Posture p) {
			double[] vector1 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.FOOT_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.ANKLE_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name()));
			return MathUtils.getAngleBetweenVectors(vector1,vector2);
		}

		@Override
		public double getA19Angle(Posture p) {
			double[] vector1 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.FOOT_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.ANKLE_LEFT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name()));
			return MathUtils.getAngleBetweenVectors(vector1,vector2);
		}

		@Override
		public double getA20Angle(Posture p) {
			// TODO Body model, 20th angle
			throw new RuntimeException("Not implemented");
		}

		@Override
		public double getA21Angle(Posture p) {
			// TODO Body model, 21th angle
			throw new RuntimeException("Not implemented");
		}

		public static void main(String[] args){
			GraphicalUserInterface ui = new GraphicalUserInterface();
			Posture posture = new Posture();
			ui.loadPosture(posture);
			Posture p = ui.getUserAvatar();
			IBodyModel bm = new BodyModelImpl();
			while(true){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print("angle 1:"+Math.toDegrees(bm.getA1Angle(p)));
				System.out.print("| angle 2:"+Math.toDegrees(bm.getA2Angle(p)));
				System.out.print("| angle 3:"+Math.toDegrees(bm.getA3Angle(p)));
				System.out.print("| angle 4:"+Math.toDegrees(bm.getA4Angle(p)));
				System.out.print("| angle 5:"+Math.toDegrees(bm.getA5Angle(p)));
				System.out.print("| angle 6:"+Math.toDegrees(bm.getA6Angle(p)));
				System.out.print("| angle 7:"+Math.toDegrees(bm.getA7Angle(p)));
				System.out.print("| angle 8:"+Math.toDegrees(bm.getA8Angle(p)));
				System.out.print("| angle 9:"+Math.toDegrees(bm.getA9Angle(p)));
				System.out.print("| angle 10:"+Math.toDegrees(bm.getA10Angle(p)));
				System.out.print("| angle 11:"+Math.toDegrees(bm.getA11Angle(p)));
				System.out.print("| angle 12:"+Math.toDegrees(bm.getA12Angle(p)));
				System.out.print("| angle 13:"+Math.toDegrees(bm.getA13Angle(p)));
				System.out.print("| angle 14:"+Math.toDegrees(bm.getA14Angle(p)));
				System.out.print("| angle 15:"+Math.toDegrees(bm.getA15Angle(p)));
				System.out.print("| angle 16:"+Math.toDegrees(bm.getA16Angle(p)));
				System.out.print("| angle 17:"+Math.toDegrees(bm.getA17Angle(p)));
				System.out.print("| angle 18:"+Math.toDegrees(bm.getA18Angle(p)));
				System.out.println("| angle 19:"+Math.toDegrees(bm.getA19Angle(p)));
			}
		}

		public List<Integer> getOuterLimit() {
			return 	outerLimit ;
		}

		private int angleBase = 160;
		/**
		 * Maximal size an angle can take
		 * @return
		 */
		public int getAngleBase() {
			return angleBase;
		}

		public float[] getInnerLimit() {
			return innerLimit;
		}


}
