package app;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class representing pose.
 * Coordinate alignment x,y z - depth 
 * Resolution of the Kinect depth camera - 640 x 480.
 * Body model based on idealistic type
 *  http://www.creativecomicart.com/uploads/4/1/8/5/4185864/9470541_orig.gif?228
 *  Foot size based on
 *  http://design.tutsplus.com/tutorials/human-anatomy-fundamentals-advanced-facial-features--cms-20683
 * @author Artur Vitt
 *
 */
public class Pose {

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
	private double hip_center[] = null;
	private double hip_left[] = null;
	private double hip_right[] = null;
	private double knee_left[] = null;
	private double knee_right[] = null;
	private double ankle_left[] = null;
	private double ankle_right[] = null;
	private double foot_left[] = null;
	private double foot_right[] = null;


	public double X_OFFSET = 0;
	public double Y_OFFSET = 0;
	public double Z_OFFSET = 0;

	private Map<String,double[]> jointMap = new TreeMap<String, double[]>();

	public Map<String, double[]> getJointMap() {
		jointMap.clear();
		jointMap.put(BodyModel.JOINT_TAG.HEAD.name(),head);
		jointMap.put(BodyModel.JOINT_TAG.SHOULDER_CENTER.name(),shoulder_center);
		jointMap.put(BodyModel.JOINT_TAG.SHOULDER_LEFT.name(),shoulder_left);
		jointMap.put(BodyModel.JOINT_TAG.SHOULDER_RIGHT.name(),shoulder_right);
		jointMap.put(BodyModel.JOINT_TAG.ELBOW_RIGHT.name(),elbow_right);
		jointMap.put(BodyModel.JOINT_TAG.ELBOW_LEFT.name(),elbow_left);
		jointMap.put(BodyModel.JOINT_TAG.HIP_LEFT.name(),hip_left);

		jointMap.put(BodyModel.JOINT_TAG.SPINE.name(),spine);
		jointMap.put(BodyModel.JOINT_TAG.HIP_RIGHT.name(),hip_right);
		jointMap.put(BodyModel.JOINT_TAG.WRIST_RIGHT.name(),wrist_right);
		jointMap.put(BodyModel.JOINT_TAG.WRIST_LEFT.name(),wrist_left);
		jointMap.put(BodyModel.JOINT_TAG.KNEE_LEFT.name(),knee_left);
		jointMap.put(BodyModel.JOINT_TAG.KNEE_RIGHT.name(),knee_right);
		jointMap.put(BodyModel.JOINT_TAG.ANKLE_LEFT.name(),ankle_left);
		jointMap.put(BodyModel.JOINT_TAG.ANKLE_RIGHT.name(),ankle_right);
		jointMap.put(BodyModel.JOINT_TAG.FOOT_LEFT.name(),foot_left);
		jointMap.put(BodyModel.JOINT_TAG.FOOT_RIGHT.name(),foot_right);
		return jointMap;
	}





	public void setJointList(Map<String, double[]> jointList) {
		this.jointMap = jointList;
	}

	private int[] targetArray = {0,0,0};

	public Pose(){
		/* Total height is 9 heads, position of the head
		 *  is in the last 9 section*/
		head = new double[3];
		head[0] = X_OFFSET + BodyModel.CENTER;
		head[1] = Y_OFFSET + BodyModel.HH*8 - BodyModel.HH/2;
		head[2] = Z_OFFSET + 0;


		shoulder_center = new double[3];
		shoulder_center[0] = X_OFFSET + BodyModel.CENTER;
		shoulder_center[1] = Y_OFFSET + BodyModel.HH*7 - BodyModel.HH/8 ;
		shoulder_center[2] = Z_OFFSET + 0;


		shoulder_left = new double [3];
		shoulder_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW*3/4;
		shoulder_left[1] = Y_OFFSET + BodyModel.HH*7 - BodyModel.HH/3;
		shoulder_left[2] = Z_OFFSET + 0;



		shoulder_right = new double [3];
		shoulder_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW*3/4;
		shoulder_right[1] = Y_OFFSET + BodyModel.HH*7 - BodyModel.HH/3;
		shoulder_right[2] = Z_OFFSET + 0;


		elbow_right = new double [3];
		elbow_right[0] = X_OFFSET + BodyModel.CENTER - ( BodyModel.HW +BodyModel.HW/5 );
		elbow_right[1] = Y_OFFSET + BodyModel.HH*6 - BodyModel.HH*4/5;
		elbow_right[2] = Z_OFFSET + 0;


		elbow_left = new double [3];
		elbow_left[0] = X_OFFSET + BodyModel.CENTER + (BodyModel.HW + BodyModel.HW/5);
		elbow_left[1] = Y_OFFSET + BodyModel.HH*6 - BodyModel.HH*4/5;
		elbow_left[2] = Z_OFFSET + 0;


		hip_left = new double [3];
		hip_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW/2;
		hip_left[1] = Y_OFFSET + BodyModel.HH*5 - BodyModel.HH/3;
		hip_left[2] = Z_OFFSET + 0;


		spine = new double [3];
		spine[0] = X_OFFSET + BodyModel.CENTER;
		spine[1] = Y_OFFSET + BodyModel.HH*5;
		spine[2] = Z_OFFSET + 0;
		
		hip_center = new double [3];
		hip_center[0] = X_OFFSET + BodyModel.CENTER;
		hip_center[1] = Y_OFFSET + BodyModel.HH*5;
		hip_center[2] = Z_OFFSET + 0;


		hip_right = new double [3];
		hip_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW/2;
		hip_right[1] = Y_OFFSET + BodyModel.HH*5 - BodyModel.HH/3;
		hip_right[2] = Z_OFFSET + 0;


		wrist_right = new double[3];
		wrist_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW;
		wrist_right[1] = Y_OFFSET + BodyModel.HH*4 - BodyModel.HH/2;
		wrist_right[2] = Z_OFFSET + 0;
		
		


		wrist_left = new double[3];
		wrist_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW ;
		wrist_left[1] = Y_OFFSET + BodyModel.HH*4 - BodyModel.HH/2;
		wrist_left[2] = Z_OFFSET + 0;
		
		hand_right = new double[3];
		hand_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW;
		hand_right[1] = Y_OFFSET + BodyModel.HH*4 - BodyModel.HH/2;
		hand_right[2] = Z_OFFSET + 0;
		
		hand_left = new double[3];
		hand_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW ;
		hand_left[1] = Y_OFFSET + BodyModel.HH*4 - BodyModel.HH/2;
		hand_left[2] = Z_OFFSET + 0;


		knee_left = new double[3];
		knee_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW/2 ;
		knee_left[1] = Y_OFFSET + BodyModel.HH*3 - BodyModel.HH/2;
		knee_left[2] = Z_OFFSET + 0;


		knee_right = new double[3];
		knee_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW/2 ;
		knee_right[1] = Y_OFFSET + BodyModel.HH*3 - BodyModel.HH/2;
		knee_right[2] = Z_OFFSET + 0;


		ankle_left = new double[3];
		ankle_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW/3 ;
		ankle_left[1] = Y_OFFSET + BodyModel.HH/4;
		ankle_left[2] = Z_OFFSET + 0;


		ankle_right = new double[3];
		ankle_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW/3 ;
		ankle_right[1] = Y_OFFSET + BodyModel.HH/4;
		ankle_right[2] = Z_OFFSET + 0;


		foot_left = new double[3];
		foot_left[0] = X_OFFSET + BodyModel.CENTER + BodyModel.HW/2;
		foot_left[1] = Y_OFFSET + BodyModel.HH/7;
		foot_left[2] = Z_OFFSET + 0 + BodyModel.HH - BodyModel.HH/10;


		foot_right = new double[3];
		foot_right[0] = X_OFFSET + BodyModel.CENTER - BodyModel.HW/2 ;
		foot_right[1] = Y_OFFSET + BodyModel.HH/7;
		foot_right[2] = Z_OFFSET + 0 + BodyModel.HH - BodyModel.HH/10;

	}





	public double[] getHead() {
		return head;
	}

	public void setHead(double[] head) {
		this.head = head;
	}

	public double[] getShoulder_left() {
		return shoulder_left;
	}

	public void setShoulder_left(double[] shoulder_left) {
		this.shoulder_left = shoulder_left;
	}

	public double[] getShoulder_right() {
		return shoulder_right;
	}

	public void setShoulder_right(double[] shoulder_right) {
		this.shoulder_right = shoulder_right;
	}

	public double[] getShoulder_center() {
		return shoulder_center;
	}

	public void setShoulder_center(double[] shoulder_center) {
		this.shoulder_center = shoulder_center;
	}

	public double[] getElbow_left() {
		return elbow_left;
	}

	public void setElbow_left(double[] elbow_left) {
		this.elbow_left = elbow_left;
	}

	public double[] getElbow_right() {
		return elbow_right;
	}

	public void setElbow_right(double[] elbow_right) {
		this.elbow_right = elbow_right;
	}

	public double[] getHand_left() {
		return hand_left;
	}

	public void setHand_left(double[] hand_left) {
		this.hand_left = hand_left;
	}

	public double[] getHand_right() {
		return hand_right;
	}

	public void setHand_right(double[] hand_right) {
		this.hand_right = hand_right;
	}

	public double[] getWrist_left() {
		return wrist_left;
	}

	public void setWrist_left(double[] wrist_left) {
		this.wrist_left = wrist_left;
	}

	public double[] getWrist_right() {
		return wrist_right;
	}

	public void setWrist_right(double[] wrist_right) {
		this.wrist_right = wrist_right;
	}

	public double[] getSpine() {
		return spine;
	}

	public void setSpine(double[] spine) {
		this.spine = spine;
	}

	public double[] getHip_center() {
		return hip_center;
	}

	public void setHip_center(double[] hip_center) {
		this.hip_center = hip_center;
	}

	public double[] getHip_left() {
		return hip_left;
	}

	public void setHip_left(double[] hip_left) {
		this.hip_left = hip_left;
	}

	public double[] getHip_right() {
		return hip_right;
	}

	public void setHip_right(double[] hip_right) {
		this.hip_right = hip_right;
	}

	public double[] getKnee_left() {
		return knee_left;
	}

	public void setKnee_left(double[] knee_left) {
		this.knee_left = knee_left;
	}

	public double[] getKnee_right() {
		return knee_right;
	}

	public void setKnee_right(double[] knee_right) {
		this.knee_right = knee_right;
	}

	public double[] getAnkle_left() {
		return ankle_left;
	}

	public void setAnkle_left(double[] ankle_left) {
		this.ankle_left = ankle_left;
	}

	public double[] getAnkle_right() {
		return ankle_right;
	}

	public void setAnkle_right(double[] ankle_right) {
		this.ankle_right = ankle_right;
	}

	public double[] getFoot_left() {
		return foot_left;
	}

	public void setFoot_left(double[] foot_left) {
		this.foot_left = foot_left;
	}

	public double[] getFoot_right() {
		return foot_right;
	}

	public void setFoot_right(double[] foot_right) {
		this.foot_right = foot_right;
	}

	public int[] getTargetArray() {		
		return targetArray;
	}

	public void setJoinLocation(String key, double[] coords) {
		if(key.equals(BodyModel.JOINT_TAG.HEAD.name())){
			for(int i = 0;i <coords.length;i++){
				head[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.SHOULDER_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				shoulder_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.SHOULDER_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				shoulder_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.SHOULDER_CENTER.name())){
			for(int i = 0;i <coords.length;i++){
				shoulder_center[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.ELBOW_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				elbow_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.ELBOW_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				elbow_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.HAND_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				hand_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.HAND_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				hand_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.WRIST_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				wrist_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.WRIST_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				wrist_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.SPINE.name())){
			for(int i = 0;i <coords.length;i++){
				spine[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.HIP_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				hip_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.HIP_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				hip_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.KNEE_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				knee_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.KNEE_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				knee_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.ANKLE_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				ankle_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.ANKLE_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				ankle_right[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.FOOT_LEFT.name())){
			for(int i = 0;i <coords.length;i++){
				foot_left[i] = coords[i];
			}
		}
		else if(key.equals(BodyModel.JOINT_TAG.FOOT_RIGHT.name())){
			for(int i = 0;i <coords.length;i++){
				foot_right[i] = coords[i];
			}
		}		
		else if(key.equals(BodyModel.JOINT_TAG.HIP_CENTER.name())){
			for(int i = 0;i <coords.length;i++){
				hip_center[i] = coords[i];
			}
		}
	}

	public void setXOffset(double pose_x_offset) {
		X_OFFSET = pose_x_offset;		
	}

	public void setYOffset(double pose_y_offset) {
		Y_OFFSET = pose_y_offset;		
	}

	public void setZOffset(double pose_z_offset) {
		Z_OFFSET = pose_z_offset;		
	}

	public void setTargetArray(int[] targ){
		for(int i = 0;i< targ.length;i++){
			targetArray[i] = targ[i];
		}
	}

	public Pose copy() {
		Pose p = new Pose();
		Set<String> keys = jointMap.keySet();
		for(String key:keys){
			p.setJoinLocation(key,jointMap.get(key));
		}
		p.setTargetArray(targetArray);
		return p;
	}

}
