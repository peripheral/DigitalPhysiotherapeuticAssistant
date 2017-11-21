package com.app.ml.decision_tree;

public class Split {
	public static enum TYPE{GE,LE,EQ,GT,LT};
	private String attritbuteName = "";
	private float threshold = 0;
	
	public String getAttritbuteName() {
		return attritbuteName;
	}
	public void setAttritbuteName(String attritbuteName) {
		this.attritbuteName = attritbuteName;
	}
	public float getThreshold() {
		return threshold;
	}
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
}
