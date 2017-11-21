/**
 * 
 */
package com.app.graphics.avatar;

import com.app.entities.Posture;

/**
 *
 */
public interface IBodyModel {
	/**
	 * Angle between left shoulder- shoulder center - head
	 * @param p
	 * @return
	 */
	public double getA1Angle(Posture p);
	/**
	 * Angle between right shoulder- shoulder center - head
	 * @param p
	 * @return
	 */
	public double getA2Angle(Posture p);
	/**
	 * Angle between head -shoulder center -spine
	 * @param p
	 * @return
	 */
	public double getA3Angle(Posture p);
	/**
	 * Angle between left shoulder -shoulder center -spine
	 * @param p
	 * @return
	 */
	public double getA4Angle(Posture p);
	/**
	 * Angle between right shoulder- shoulder center - spine
	 * @param p
	 * @return
	 */
	public double getA5Angle(Posture p);
	/**
	 * Angle between left shoulder - shoulder center and plane spanned by vectors 
	 * shoulder center -shoulder right and shoulder center -spine
	 * @param p
	 * @return
	 */
	public double getA6Angle(Posture p);
	/**
	 * Angle between right shoulder - shoulder center and plane that is spanned by vectors,
	 * shoulder center -> left shoulder ; shoulder center -> spine
	 * @param p
	 * @return
	 */
	public double getA7Angle(Posture p);
	/**
	 * Angle between right hip -spine -shoulder center
	 * @param p
	 * @return
	 */
	public double getA8Angle(Posture p);
	/**
	 * Angle between spine - shoulder center and plane spanned by vectors:
	 * Spine -> left hip, spine -> right hip
	 * @param p
	 * @return
	 */
	public double getA9Angle(Posture p);
	/**
	 * Angle between left hip - right hip- right knee
	 * @param p
	 * @return
	 */
	public double getA10Angle(Posture p);
	/**
	 * Angle between R hip -L hip and L hip - L Knee
	 * @param p
	 * @return
	 */
	public double getA11Angle(Posture p);
	/**
	 * Angle between L hip - left knee and plane spanned by vectors:
	 *  L hip -> R hip, L hip -> spine
	 * @param p
	 * @return
	 */
	public double getA12Angle(Posture p);
	/**
	 * Angle between R hip - R knee and plane spanned by vectors:
	 *  L hip -> R hip, L hip -> spine
	 * @param p
	 * @return
	 */
	public double getA13Angle(Posture p);
	/**
	 * Angle between right hip-right knee-right ankle 
	 * @param p
	 * @return
	 */
	public double getA14Angle(Posture p);
	/**
	 * Angle between left hip- left knee - left ankle 
	 * @param p
	 * @return
	 */
	public double getA15Angle(Posture p);
	/**
	 * Angle between L knee - L ankle - L foot
	 * @param p
	 * @return
	 */
	public double getA16Angle(Posture p);
	/**
	 * Angle between R knee - R ankle - R foot
	 * @param p
	 * @return
	 */
	public double getA17Angle(Posture p);
	/**
	 * Angle between vectors R foot -> R ankle and R hip -> L hip
	 * @param p
	 * @return
	 */
	public double getA18Angle(Posture p);
	/**
	 * Angle  between vectors L foot -> L ankle and R hip -> L hip
	 * @param p
	 * @return
	 */
	public double getA19Angle(Posture p);
	/**
	 * not specified
	 * @param p
	 * @return
	 */
	public double getA20Angle(Posture p);
	/**
	 * not specified
	 * @param p
	 * @return
	 */
	public double getA21Angle(Posture p);
}

