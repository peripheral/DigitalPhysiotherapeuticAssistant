package app;

public class MathUtils {

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
	public static double getVectorAbsolute(double[] vector) {
		double absolute=0;
		for(int i = 0;i < vector.length;i++){
			absolute = absolute +Math.pow(vector[i], 2);
		}
		absolute = Math.sqrt(absolute);
		return absolute;
	}
	public static double[] getNormal(double[] vector, double[] vector1) {
		return getCrossProduct(vector,vector1);
	}
	public static double[] getCrossProduct(double[] vector, double[] vector1) {
		double[] crosProd = new double[vector.length];
		crosProd[0] = vector[1]*vector1[2];
		crosProd[1] = vector[0]*vector1[2];
		crosProd[2] = vector[0]*vector1[1];
		return crosProd;
	}
	
	/**
	 * Get angle between vectors.
	 * @param vector
	 * @param vector1
	 * @return angle in radians
	 */
	public static double getAngleV(double[] vector, double[] vector1) {
		double scalarProd = MathUtils.getScalarProd(vector,vector1);
		double angle = Math.acos(scalarProd/(getVectorAbsolute(vector)*getVectorAbsolute(vector1)));
		return angle;
	}
	
	/**
	 * Two vectors are used to determine angle comprised by dots, that is
	 * p ->p1, p -> p2
	 * @param p
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getAngle(double[] p,double[] p1,double[] p2){
		double[] vector1 = MathUtils.getVector(p,p1);
		double[] vector2 = MathUtils.getVector(p,p2);
		double scalarProd = MathUtils.getScalarProd(vector1,vector2);
		double angle = Math.acos(scalarProd/(MathUtils.getVectorAbsolute(vector1)*MathUtils.getVectorAbsolute(vector2)));
		return angle;
	}	
}
