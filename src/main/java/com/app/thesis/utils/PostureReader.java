package com.app.thesis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import com.app.entities.Posture;
import com.app.graphics.avatar.BodyModelImpl;
import com.app.io.IPostureFileReader;

public class PostureReader implements IPostureFileReader{
	private Scanner sc = null;
	private String[] headers = null;
	
	public PostureReader(File f,int skipRows,int skipColumns){
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

	public PostureReader(String filename){
		try {
			sc = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headers = sc.nextLine().split(",");
	}
	
	@Override
	public Posture nextSample(){
		Posture p = new Posture();
		String line = sc.nextLine();
		String[] attributes = line.split(",");
		int index = 1; // Starts from second columns, HipCenter
		/*TimeStamp,HipCenterX,HipCenterY,HipCenterZ,SpineX,SpineY,SpineZ,ShoulderCenterX,ShoulderCenterY,
		 * ShoulderCenterZ,HeadX,HeadY,HeadZ,ShoulderLeftX,ShoulderLeftY,ShoulderLeftZ,ElbowLeftX,ElbowLeftY,
		 * ElbowLeftZ,WristLeftX,WristLeftY,WristLeftZ,HandLeftX,HandLeftY,HandLeftZ,ShoulderRightX,ShoulderRightY,
		 * ShoulderRightZ,ElbowRightX,ElbowRightY,ElbowRightZ,WristRightX,WristRightY,WristRightZ,HandRightX,HandRightY,HandRightZ,HipLeftX,HipLeftY,HipLeftZ,KneeLeftX,
		 * KneeLeftY,KneeLeftZ,AnkleLeftX,AnkleLeftY,AnkleLeftZ,FootLeftX,FootLeftY,FootLeftZ,HipRightX,HipRightY,HipRightZ,KneeRightX,KneeRightY,KneeRightZ,AnkleRightX,
		 * AnkleRightY,AnkleRightZ,FootRightX,FootRightY,FootRightZ,
		 */
		double[] hip_center = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.SPINE_BASE.name(), hip_center);
		double[] spine_mid = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.SPINE_MID.name(), spine_mid);
		double[] shoulder_center = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.SPINE_SHOULDER.name(), shoulder_center);
		double[] head = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.HEAD.name(), head);
		double[] shoulder_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.SHOULDER_LEFT.name(), shoulder_left);
		double[] elbow_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.ELBOW_LEFT.name(), elbow_left);
		double[] wrist_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.WRIST_LEFT.name(), wrist_left);
		double[] hand_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.HAND_LEFT.name(), hand_left);
		double[] shoulder_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.SHOULDER_RIGHT.name(), shoulder_right);
		double[] elbow_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.ELBOW_RIGHT.name(), elbow_right);
		double[] wrist_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.WRIST_RIGHT.name(), wrist_right);
		double[] hand_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.HAND_RIGHT.name(), hand_right);		
		double[] hip_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.HIP_LEFT.name(), hip_left);
		double[] knee_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.KNEE_LEFT.name(), knee_left);
		double[] ankle_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.ANKLE_LEFT.name(), ankle_left);
		double[] foot_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.FOOT_LEFT.name(), foot_left);
		double[] hip_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.HIP_RIGHT.name(), hip_right);
		double[] knee_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.KNEE_RIGHT.name(), knee_right);
		double[] ankle_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.ANKLE_RIGHT.name(), ankle_right);
		double[] foot_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModelImpl.JOINT_TAG.FOOT_RIGHT.name(), foot_right);
		Map<String,double[]> joints = p.getJointMap();
		for(String key:joints.keySet()){
			double[] location = joints.get(key);
			location[2] = location[2] - 1000;
			location[1] = location[1] + 500;
			p.setJoinLocation(key, location);
			p.setYOffset(400);
		}
		return p;
	}
	
	@Override
	public void closeScanner(){
		sc.close();
	}
	
	public static void main(String[] args){
		PostureReader pReader = new PostureReader("data.csv");
		PostureReader pReader1 = new PostureReader("data.csv");
		new Thread(){
			public void run(){
				while(pReader.hasNext()){
					pReader.nextSample();
				}
			}
		}.start();
		new Thread(){
			public void run(){
				while(pReader1.hasNext()){
					pReader1.nextSample();
				}
			}
		}.start();
		BodyModelImpl bm = new BodyModelImpl();     
		System.out.println("Done");
	}

	@Override
	public boolean hasNext() {
		return sc.hasNextLine();		
	}
}
