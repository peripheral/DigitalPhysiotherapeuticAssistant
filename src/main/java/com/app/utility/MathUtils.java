package com.app.utility;

public class MathUtils {

	/**
	 * Shortest distance between two points
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getDistance(double[] p1,double[] p2){
		double result= Math.sqrt(Math.pow(p1[0] - p2[0],2) +
				Math.pow(p1[1] - p2[1],2) +Math.pow(p1[2] - p2[2],2));
		return result;
	}
	/**
	 * Formula applied p1 - p = vector;
	 * @param p
	 * @param p1
	 * @return vector  p -> p1
	 */
	public static double[] getVector(double[] p, double[] p1) {
		double[] vector = new double[p.length];
		for(int i = 0; i < p.length;i++){
			vector[i] = p1[i] - p[i];
		}
		return  vector;
	}
	public static double getScalarProd(double[] vector1, double[] vector2) {
		double result = 0;
		for(int i = 0; i < vector1.length;i++){
			result = result + vector1[i]*vector2[i];
		}
		return result;
	}
	
	public static float getScalarProd(float[] vector1, float[] vector2) {
		float result = 0;
		for(int i = 0; i < vector1.length;i++){
			result = result + vector1[i]*vector2[i];
		}
		return result;
	}
	
	public static double getVectorAbsolute(double[] vector) {
		double absolute=0;
		for(int i = 0;i < vector.length;i++){
			absolute = absolute +Math.pow(vector[i], 2);
		}
		absolute = Math.sqrt(absolute);
		return absolute;
	}
	
	public static float getVectorAbsolute(float[] vector) {
		float absolute=0;
		for(int i = 0;i < vector.length;i++){
			absolute = (float) (absolute +Math.pow(vector[i], 2));
		}
		absolute = (float) Math.sqrt(absolute);
		return absolute;
	}
	
	public static double[] getNormal(double[] vector, double[] vector1) {
		return getCrossProduct(vector,vector1);
	}
	public static double[] getCrossProduct(double[] vector1, double[] vector2) {
		double[] crosProd = new double[vector1.length];
		crosProd[0] = vector1[1]*vector2[2] - vector1[2]*vector2[1];
		crosProd[1] = vector1[2]*vector2[0] - vector1[0]*vector2[2];
		crosProd[2] = vector1[0]*vector2[1] - vector1[1]*vector2[0];
		return crosProd;
	}
	
	public static float[] getCrossProduct(float[] vector1, float[] vector2) {
		float[] crosProd = new float[vector1.length];
		crosProd[0] = vector1[1]*vector2[2] - vector1[2]*vector2[1];
		crosProd[1] = vector1[2]*vector2[0] - vector1[0]*vector2[2];
		crosProd[2] = vector1[0]*vector2[1] - vector1[1]*vector2[0];
		return crosProd;
	}
	
	/**
	 * Get angle between vectors.
	 * @param vector2
	 * @param vector2
	 * @return angle in degrees
	 */
	public static double getAngleBetweenVectors(double[] vector1, double[] vector2) {
		double scalarProd = MathUtils.getScalarProd(vector1,vector2);
		if(MathUtils.getVectorAbsolute(vector1)*MathUtils.getVectorAbsolute(vector2) == 0) {
			throw new RuntimeException("Vector1 or Vector2 equal zero");
		}
		double cosin = scalarProd/(MathUtils.getVectorAbsolute(vector1)*MathUtils.getVectorAbsolute(vector2));
		if(cosin > 1) {
			cosin = 1;
		}
		double angle = Math.acos(cosin);
		return Math.toDegrees(angle);
	}
	
	/**
	 * Two vectors are used to determine angle comprised by dots, that is
	 * p1 ->p2, p1 -> p3
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return returns angle in degrees
	 */
	public static double getAngleFromPoints(double[] p1,double[] p2,double[] p3){
		double[] vector1 = MathUtils.getVector(p1,p2);
		double[] vector2 = MathUtils.getVector(p1,p3);
		double scalarProd = MathUtils.getScalarProd(vector1,vector2);
		if(MathUtils.getVectorAbsolute(vector1)*MathUtils.getVectorAbsolute(vector2) == 0) {
			throw new RuntimeException("Vector1 or Vector2 equal zero");
		}
		double cosin = scalarProd/(MathUtils.getVectorAbsolute(vector1)*MathUtils.getVectorAbsolute(vector2));
		if(cosin > 1) {
			cosin = 1;
		}
		double angle = Math.acos(cosin);
		
		return Math.toDegrees(angle);
	}	
	
	public static double getAngleWithPlane(double[] normal, double[] vector){
		double scalarProd = MathUtils.getScalarProd(normal, vector);
		double angle = Math.asin(scalarProd/(MathUtils.getVectorAbsolute(normal)*MathUtils.getVectorAbsolute(vector)));
		return Math.toDegrees(angle);
	}

	public static float[] divideVectorWithConstant(float[] vec, float divider) {
		float[] vector = new float[vec.length];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = vec[i] /divider;
		}
		return vector;
	}

	public static float[] multiplyVectorWithConstant(float[] vec, float multiplier) {
		float[] vector = new float[vec.length];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = vec[i] * multiplier;
		}
		return vector;
	}
	
	public static float[] subtract(float[] vectorPA, float[] projecOnNorm) {
		float[] vector = new float[vectorPA.length];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = vectorPA[i] - projecOnNorm[i];
		}
		return vector;
	}
	public static float[] projectVectorOnVector(float[] vec, float[] normal) {
		float[] unitVector = divideVectorWithConstant(normal, getVectorAbsolute(normal));
		float scalarOfNormalAndVec = MathUtils.getScalarProd(vec, normal);
		float[] result = multiplyVectorWithConstant(unitVector, scalarOfNormalAndVec/getVectorAbsolute(normal));
		return result;
	}

}
