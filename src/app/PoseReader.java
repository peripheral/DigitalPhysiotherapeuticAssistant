package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class PoseReader {
	private Scanner sc = null;
	private String[] headers = null;
	private final double[] center =new double[]{0,0,0};
	//private boolean  
//	private 
	public PoseReader(File f,int skipRows,int skipColumns){
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public PoseReader(File f){
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headers = sc.nextLine().split(",");
	}
	
	public Pose nextSample(){
		if(!sc.hasNextLine()){
			return null;
		}
		Pose p = new Pose();
		String line = sc.nextLine();
		String[] attributes = line.split(",");
		int index = 1;
		double[] hip_center = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.HIP_CENTER.name(), hip_center);
		double[] spine = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.SPINE.name(), spine);
		double[] shoulder_center = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.SHOULDER_CENTER.name(), shoulder_center);
		double[] head = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.HEAD.name(), head);
		double[] shoulder_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.SHOULDER_LEFT.name(), shoulder_left);
		double[] elbow_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.ELBOW_LEFT.name(), elbow_left);
		double[] wrist_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.WRIST_LEFT.name(), wrist_left);
		double[] hand_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.HAND_LEFT.name(), hand_left);
		double[] shoulder_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.SHOULDER_RIGHT.name(), shoulder_right);
		double[] elbow_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.ELBOW_RIGHT.name(), elbow_right);
		double[] wrist_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.WRIST_RIGHT.name(), wrist_right);
		double[] hand_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.HAND_RIGHT.name(), hand_right);		
		double[] hip_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.HIP_LEFT.name(), hip_left);
		double[] knee_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.KNEE_LEFT.name(), knee_left);
		double[] ankle_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.ANKLE_LEFT.name(), ankle_left);
		double[] foot_left = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.FOOT_LEFT.name(), foot_left);
		double[] hip_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.HIP_RIGHT.name(), hip_right);
		double[] knee_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.KNEE_RIGHT.name(), knee_right);
		double[] ankle_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.ANKLE_RIGHT.name(), ankle_right);
		double[] foot_right = {new Double(attributes[index++])*1000,
				new Double(attributes[index++])*1000,new Double(attributes[index++])*1000
		};
		p.setJoinLocation(BodyModel.JOINT_TAG.FOOT_RIGHT.name(), foot_right);
		Map<String,double[]> joints = p.getJointMap();
		for(String key:joints.keySet()){
			double[] location = joints.get(key);
			location[2] = location[2] - 2000;
			location[1] = location[1] + 500;
			p.setJoinLocation(key, location);
			p.setZOffset(2000);
		}
		return p;
	}
	
	public void closeScanner(){
		sc.close();
	}
	
	public static void main(String[] args){
		PoseReader pReader = new PoseReader(new File("data.csv"));
		Pose p = pReader.nextSample();
		pReader.closeScanner();
		BodyModel bm = new BodyModel();     
		System.out.println("Done");
	}
}
