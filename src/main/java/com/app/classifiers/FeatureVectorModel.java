package com.app.classifiers;

import java.util.Arrays;

import com.app.entities.Posture;
import com.app.graphics.avatar.IBodyModel;
import com.app.graphics.avatar.BodyModelImpl;

public class FeatureVectorModel {
	private Posture expectedPosture = new Posture();
	private Posture observedPosture = new Posture();
	
	private float[] innerLimit;
	public FeatureVectorModel(){
		BodyModelImpl bm = new BodyModelImpl();
		innerLimit = bm.getInnerLimit();
	}
	/**
	 * Returns array of floats, first half contains observed configuration of body
	 * @return
	 */
	public float[] getFeatureVectorAngles() {
		IBodyModel bm = new BodyModelImpl();
		float[] vector = new float[38];
		vector[0] = (float)bm.getA1Angle(observedPosture);
		vector[1] = (float)bm.getA2Angle(observedPosture);
		vector[2] = (float)bm.getA3Angle(observedPosture);
		vector[3] = (float)bm.getA4Angle(observedPosture);
		vector[4] = (float)bm.getA5Angle(observedPosture);
		vector[5] = (float)bm.getA6Angle(observedPosture);
		vector[6] = (float)bm.getA7Angle(observedPosture);
		vector[7] = (float)bm.getA8Angle(observedPosture);
		vector[8] = (float)bm.getA9Angle(observedPosture);
		vector[9] = (float)bm.getA10Angle(observedPosture);
		vector[10] = (float)bm.getA11Angle(observedPosture);
		vector[11] = (float)bm.getA12Angle(observedPosture);
		vector[12] = (float)bm.getA13Angle(observedPosture);
		vector[13] = (float)bm.getA14Angle(observedPosture);
		vector[14] = (float)bm.getA15Angle(observedPosture);
		vector[15] = (float)bm.getA16Angle(observedPosture);
		vector[16] = (float)bm.getA17Angle(observedPosture);
		vector[17] = (float)bm.getA18Angle(observedPosture);
		vector[18] = (float)bm.getA19Angle(observedPosture);
		vector[19] = (float)bm.getA1Angle(expectedPosture);
		vector[20] = (float)bm.getA2Angle(expectedPosture);
		vector[21] = (float)bm.getA3Angle(expectedPosture);
		vector[22] = (float)bm.getA4Angle(expectedPosture);
		vector[23] = (float)bm.getA5Angle(expectedPosture);
		vector[24] = (float)bm.getA6Angle(expectedPosture);
		vector[25] = (float)bm.getA7Angle(expectedPosture);
		vector[26] = (float)bm.getA8Angle(expectedPosture);
		vector[27] = (float)bm.getA9Angle(expectedPosture);
		vector[28] = (float)bm.getA10Angle(expectedPosture);
		vector[29] = (float)bm.getA11Angle(expectedPosture);
		vector[30] = (float)bm.getA12Angle(expectedPosture);
		vector[31] = (float)bm.getA13Angle(expectedPosture);
		vector[32] = (float)bm.getA14Angle(expectedPosture);
		vector[33] = (float)bm.getA15Angle(expectedPosture);
		vector[34] = (float)bm.getA16Angle(expectedPosture);
		vector[35] = (float)bm.getA17Angle(expectedPosture);
		vector[36] = (float)bm.getA18Angle(expectedPosture);
		vector[37] = (float)bm.getA19Angle(expectedPosture);
		return vector;
	}
	
	/**
	 * Returns array of floats, contains deviation according to formular
	 * observed - expected = result
	 * @return
	 */
	public float[] getFeatureVectorAngleDeviation() {
		float[] vector = getFeatureVectorAngles();
		float[] result = new float[vector.length/2];
		for(int i = 0; i< result.length;i++){
			result[i] = Math.abs(vector[i+result.length] - vector[i]);
		}
		return result;
	}
	
	/**
	 * Returns array of floats, contains deviations presented as percentage
	 * innerLimit - a variable used during conversion from deviation to percentage.
	 * It defines a value of of deviation at it maximum. lowering the value of innerlimit 
	 * will affect performance evaluation to require higher precision for higher
	 * success classification.
	 * @return array that contains deviation as percentage
	 */
	public float[] getFeatureVectorPercentages() {
		float[] angleDeviations = getFeatureVectorAngleDeviation();
		for(int i = 0; i< angleDeviations.length;i++){
			if(innerLimit[i]-angleDeviations[i] <0){
				angleDeviations[i] = 0;
			}else{
				angleDeviations[i] = ((innerLimit[i]-angleDeviations[i])/innerLimit[i])*100;
			}		
		}
		return angleDeviations;
	}
	
	/**
	 * Returns array of floats, contains deviations presented as percentage
	 * @return
	 */
	public float[] getFeatureVectorPercentagesSorted() {
		float[] result = getFeatureVectorPercentages();
		Arrays.sort(result);
		return result;
	}
	
	public Posture getExpectedPosture() {
		return expectedPosture;
	}
	public void setExpectedPosture(Posture expectedPosture) {
		this.expectedPosture = expectedPosture;
	}
	public Posture getObservedPosture() {
		return observedPosture;
	}
	public void setObservedPosture(Posture givenPosture) {
		this.observedPosture = givenPosture;
	}
}
