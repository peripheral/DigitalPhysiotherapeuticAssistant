package com.app.entities;

import java.awt.List;
import java.util.LinkedList;

public class Exercise extends Entity {

	private int latestProgress;
	private int highestProgress;
	private LinkedList<Posture> postureList = new LinkedList<>();

	public Exercise(String name) {
		super.name = name;
	}
	
	public Exercise() {
	}

	public int getLatestProgress() {
		return latestProgress;
	}
	
	public void setLatestProgress(int latestProgress){
		this.latestProgress = latestProgress;		
	}

	public int getHighestProgress() {
		return highestProgress;
	}

	public void addPosture(Posture p) {
		postureList.add(p);
		
	}
	
	public double calculateAverage(){
		double sum = 0;
		for(Posture p:postureList){
			sum = sum + p.getLatestProgress();
		}
		
		return 1;
	}

	public void setHighestProgress(int highestProgress) {
		this.highestProgress = highestProgress;
	}

	public LinkedList<Posture> getPostures() {
		return postureList;
		
	}
	
	public String toString(){
		return "Exercise:[Name="+name+",id:"+id+",latest progress:"+latestProgress+
				",highestProgress:"+highestProgress+",posture size=:"+postureList.size()+"]\n";
	}
}
