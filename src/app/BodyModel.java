package app;

import java.util.Map;
import java.util.Set;

/** Provides model of the body */
public class BodyModel implements IBodyModel{

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
	public static enum JOINT_TAG{HEAD,SHOULDER_LEFT,SHOULDER_RIGHT,SHOULDER_CENTER,
		ELBOW_LEFT,ELBOW_RIGHT,HAND_LEFT,HAND_RIGHT,WRIST_LEFT,WRIST_RIGHT,SPINE,
		HIP_CENTER,HIP_LEFT,HIP_RIGHT,KNEE_LEFT,KNEE_RIGHT,ANKLE_LEFT,ANKLE_RIGHT,
		FOOT_LEFT,FOOT_RIGHT};

		public BodyModel(){
			/* Total height is 9 heads, position of the head
			 *  is in the last 9 section*/
			head = new double[3];
			head[0] = CENTER;
			head[1] = BodyModel.HH*8 - BodyModel.HH/2;
			head[2] = 0;

			shoulder_center = new double[3];
			shoulder_center[0] =  CENTER;
			shoulder_center[1] =  BodyModel.HH*7 - BodyModel.HH/8 ;
			shoulder_center[2] =  0;			

			shoulder_left = new double [3];
			shoulder_left[0] =  CENTER + BodyModel.HW*3/4;
			shoulder_left[1] =  BodyModel.HH*7 - BodyModel.HH/3;
			shoulder_left[2] =  0;
		
			shoulder_right = new double [3];
			shoulder_right[0] =  CENTER - BodyModel.HW*3/4;
			shoulder_right[1] =  BodyModel.HH*7 - BodyModel.HH/3;
			shoulder_right[2] =  0;

			elbow_right = new double [3];
			elbow_right[0] =  CENTER - ( BodyModel.HW +BodyModel.HW/5 );
			elbow_right[1] =  BodyModel.HH*6 - BodyModel.HH*4/5;
			elbow_right[2] =  0;
			
			elbow_left = new double [3];
			elbow_left[0] =  CENTER + (BodyModel.HW + BodyModel.HW/5);
			elbow_left[1] =  BodyModel.HH*6 - BodyModel.HH*4/5;
			elbow_left[2] =  0;

			hip_left = new double [3];
			hip_left[0] =  CENTER + BodyModel.HW/2;
			hip_left[1] =  BodyModel.HH*5 - BodyModel.HH/3;
			hip_left[2] =  0;

			spine = new double [3];
			spine[0] =  CENTER;
			spine[1] =  BodyModel.HH*5;
			spine[2] =  0;
			
			hip_right = new double [3];
			hip_right[0] =  CENTER - BodyModel.HW/2;
			hip_right[1] =  BodyModel.HH*5 - BodyModel.HH/3;
			hip_right[2] =  0;

			wrist_right = new double[3];
			wrist_right[0] =  CENTER - BodyModel.HW;
			wrist_right[1] =  BodyModel.HH*4 - BodyModel.HH/2;
			wrist_right[2] =  0;
			
			wrist_left = new double[3];
			wrist_left[0] =  CENTER + BodyModel.HW ;
			wrist_left[1] =  BodyModel.HH*4 - BodyModel.HH/2;
			wrist_left[2] =  0;

			knee_left = new double[3];
			knee_left[0] =  CENTER + BodyModel.HW/2 ;
			knee_left[1] =  BodyModel.HH*3 - BodyModel.HH/2;
			knee_left[2] =  0;
			
			knee_right = new double[3];
			knee_right[0] =  CENTER - BodyModel.HW/2 ;
			knee_right[1] =  BodyModel.HH*3 - BodyModel.HH/2;
			knee_right[2] =  0;

			ankle_left = new double[3];
			ankle_left[0] =  CENTER + BodyModel.HW/3 ;
			ankle_left[1] =  BodyModel.HH/4;
			ankle_left[2] =  0;			

			ankle_right = new double[3];
			ankle_right[0] =  CENTER - BodyModel.HW/3 ;
			ankle_right[1] =  BodyModel.HH/4;
			ankle_right[2] =  0;

			foot_left = new double[3];
			foot_left[0] =  CENTER + BodyModel.HW/2;
			foot_left[1] =  BodyModel.HH/7;
			foot_left[2] =  0 - BodyModel.HH - BodyModel.HH/10;

			foot_right = new double[3];
			foot_right[0] =  CENTER - BodyModel.HW/2 ;
			foot_right[1] =  BodyModel.HH/7;
			foot_right[2] =  0 - BodyModel.HH - BodyModel.HH/10;
		
			head_to_center = MathUtils.getDistance(head,shoulder_center);
			shoulder_center_to_shoulder = MathUtils.getDistance(shoulder_left,shoulder_center);
			shoulder_to_elbow = MathUtils.getDistance(shoulder_right,elbow_right);
			shoulder_center_to_spine = MathUtils.getDistance(spine,shoulder_center);
			spine_to_hip = MathUtils.getDistance(spine,hip_left);
			elbow_to_wrist = MathUtils.getDistance(elbow_left,wrist_left);
			hip_to_knee = MathUtils.getDistance(hip_left,knee_left);
			knee_to_ankle = MathUtils.getDistance(knee_left,ankle_left);
			ankle_to_foot = MathUtils.getDistance(ankle_left,foot_left);
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
		 * Multiplying all points of the joints with 
		 * @param p
		 * @param scale
		 */
		public Pose resizePose(Pose p, double scale) {
			Map<String,double[]> joints = p.getJointMap();
			Set<String> keys = joints.keySet();
			for(String key:keys){
				//p
			}
			return null;
		}



		public double getAngleWithPlane(double[] normal, double[] vector){
			double scalarProd = MathUtils.getScalarProd(normal, vector);
			double angle = Math.asin(scalarProd/(MathUtils.getVectorAbsolute(normal)*MathUtils.getVectorAbsolute(vector)));
			return angle;
		}

		@Override
		public double getA1Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HEAD.name()));
		}

		@Override
		public double getA2Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.HEAD.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));			
		}

		@Override
		public double getA3Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.HEAD.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));	
		}

		@Override
		public double getA4Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));	
		}

		@Override
		public double getA5Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));	
		}

		@Override
		public double getA6Angel(Pose p) {
			double[] vector1 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));
			double[] normal = MathUtils.getNormal(vector1,vector2);
			return MathUtils.getAngleV(normal,vector3);	
		}



		@Override
		public double getA7Angel(Pose p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_LEFT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SPINE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_RIGHT.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleV(normal,vector3);	
		}

		@Override
		public double getA8Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.SPINE.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
		}

		@Override
		public double getA9Angel(Pose p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.SPINE.name())
					,p.getJointMap().get(JOINT_TAG.SHOULDER_CENTER.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleV(normal,vector3);	
		}

		@Override
		public double getA10Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name()));
		}

		@Override
		public double getA11Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name()));
		}

		@Override
		public double getA12Angel(Pose p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.SPINE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleV(normal,vector3);	
		}

		@Override
		public double getA13Angel(Pose p) {
			double[] vector = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.SPINE.name()));
			double[] vector3 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name()));
			double[] normal = MathUtils.getNormal(vector,vector2);
			return MathUtils.getAngleV(normal,vector3);	
		}

		@Override
		public double getA14Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name()));
		}

		@Override
		public double getA15Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.ANKLE_LEFT.name()));
		}

		@Override
		public double getA16Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.ANKLE_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.FOOT_LEFT.name()));
		}

		@Override
		public double getA17Angel(Pose p) {
			return MathUtils.getAngle(p.getJointMap().get(JOINT_TAG.ANKLE_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.KNEE_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.FOOT_RIGHT.name()));
		}

		@Override
		public double getA18Angel(Pose p) {
			double[] vector1 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.FOOT_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.ANKLE_RIGHT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name()));
			return MathUtils.getAngleV(vector1,vector2);
		}

		@Override
		public double getA19Angel(Pose p) {
			double[] vector1 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.FOOT_LEFT.name())
					,p.getJointMap().get(JOINT_TAG.ANKLE_LEFT.name()));
			double[] vector2 = MathUtils.getVector(p.getJointMap().get(JOINT_TAG.HIP_RIGHT.name())
					,p.getJointMap().get(JOINT_TAG.HIP_LEFT.name()));
			return MathUtils.getAngleV(vector1,vector2);
		}

		@Override
		public double getA20Angel(Pose p) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getA21Angel(Pose p) {
			// TODO Auto-generated method stub
			return 0;
		}


}
