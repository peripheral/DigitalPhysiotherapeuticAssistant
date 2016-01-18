/**
 * 
 */
package app;

/**
 *
 */
public interface IBodyModel {
	/**
	 * Angle between left shoulder- shoulder center - head
	 * @param p
	 * @return
	 */
	public double getA1Angel(Pose p);
	/**
	 * Angle between right shoulder- shoulder center - head
	 * @param p
	 * @return
	 */
	public double getA2Angel(Pose p);
	/**
	 * Angle between head -shoulder center -spine(hip center)
	 * @param p
	 * @return
	 */
	public double getA3Angel(Pose p);
	/**
	 * Angle between left shoulder -shoulder center -spine
	 * @param p
	 * @return
	 */
	public double getA4Angel(Pose p);
	/**
	 * Angle between right shoulder- shoulder center - spine
	 * @param p
	 * @return
	 */
	public double getA5Angel(Pose p);
	/**
	 * Angle between left shoulder - shoulder center and plane spanned by vectors 
	 * shoulder center -shoulder right and shoulder center -spine
	 * @param p
	 * @return
	 */
	public double getA6Angel(Pose p);
	/**
	 * Angle between right shoulder - shoulder center and plane that is spanned by vectors,
	 * shoulder center -> left shoulder ; shoulder center -> spine
	 * @param p
	 * @return
	 */
	public double getA7Angel(Pose p);
	/**
	 * Angle between right hip -spine -shoulder center
	 * @param p
	 * @return
	 */
	public double getA8Angel(Pose p);
	/**
	 * Angle between spine - shoulder center and plane spanned by vectors:
	 * Spine -> left hip, spine -> right hip
	 * @param p
	 * @return
	 */
	public double getA9Angel(Pose p);
	/**
	 * Angle between left hip - right hip- right knee
	 * @param p
	 * @return
	 */
	public double getA10Angel(Pose p);
	/**
	 * Angle between R hip -L hip and L hip - L Knee
	 * @param p
	 * @return
	 */
	public double getA11Angel(Pose p);
	/**
	 * Angle between L hip - left knee and plane spanned by vectors:
	 *  L hip -> R hip, L hip -> spine
	 * @param p
	 * @return
	 */
	public double getA12Angel(Pose p);
	/**
	 * Angle between R hip - R knee and plane spanned by vectors:
	 *  L hip -> R hip, L hip -> spine
	 * @param p
	 * @return
	 */
	public double getA13Angel(Pose p);
	/**
	 * Angle between right hip-right knee-right ankle 
	 * @param p
	 * @return
	 */
	public double getA14Angel(Pose p);
	/**
	 * Angle between left hip- left knee - left ankle 
	 * @param p
	 * @return
	 */
	public double getA15Angel(Pose p);
	/**
	 * Angle between L knee - L ankle - L foot
	 * @param p
	 * @return
	 */
	public double getA16Angel(Pose p);
	/**
	 * Angle between R knee - R ankle - R foot
	 * @param p
	 * @return
	 */
	public double getA17Angel(Pose p);
	/**
	 * Angle between vectors R foot -> R ankle and R hip -> L hip
	 * @param p
	 * @return
	 */
	public double getA18Angel(Pose p);
	/**
	 * Angle  between vectors L foot -> L ankle and R hip -> L hip
	 * @param p
	 * @return
	 */
	public double getA19Angel(Pose p);
	/**
	 * not specified
	 * @param p
	 * @return
	 */
	public double getA20Angel(Pose p);
	/**
	 * not specified
	 * @param p
	 * @return
	 */
	public double getA21Angel(Pose p);
}
