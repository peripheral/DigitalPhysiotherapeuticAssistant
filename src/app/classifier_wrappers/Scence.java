package app.classifier_wrappers;

import app.BodyModel;
import app.IBodyModel;
import app.PoseCanvas;

public class Scence {
	private PoseCanvas expectedPose = new PoseCanvas();
	private PoseCanvas observedPose = new PoseCanvas();
	public float[] getFeatureVector() {
		IBodyModel bm = new BodyModel();
		float[] vector = new float[38];
		vector[0] = (float)bm.getA1Angel(observedPose.getPose());
		vector[1] = (float)bm.getA2Angel(observedPose.getPose());
		vector[2] = (float)bm.getA3Angel(observedPose.getPose());
		vector[3] = (float)bm.getA4Angel(observedPose.getPose());
		vector[4] = (float)bm.getA5Angel(observedPose.getPose());
		vector[5] = (float)bm.getA6Angel(observedPose.getPose());
		vector[6] = (float)bm.getA7Angel(observedPose.getPose());
		vector[7] = (float)bm.getA8Angel(observedPose.getPose());
		vector[8] = (float)bm.getA9Angel(observedPose.getPose());
		vector[9] = (float)bm.getA10Angel(observedPose.getPose());
		vector[10] = (float)bm.getA11Angel(observedPose.getPose());
		vector[11] = (float)bm.getA12Angel(observedPose.getPose());
		vector[12] = (float)bm.getA13Angel(observedPose.getPose());
		vector[13] = (float)bm.getA14Angel(observedPose.getPose());
		vector[14] = (float)bm.getA15Angel(observedPose.getPose());
		vector[15] = (float)bm.getA16Angel(observedPose.getPose());
		vector[16] = (float)bm.getA17Angel(observedPose.getPose());
		vector[17] = (float)bm.getA18Angel(observedPose.getPose());
		vector[18] = (float)bm.getA19Angel(observedPose.getPose());
		vector[19] = (float)bm.getA1Angel(expectedPose.getPose());
		vector[20] = (float)bm.getA2Angel(expectedPose.getPose());
		vector[21] = (float)bm.getA3Angel(expectedPose.getPose());
		vector[22] = (float)bm.getA4Angel(expectedPose.getPose());
		vector[23] = (float)bm.getA5Angel(expectedPose.getPose());
		vector[24] = (float)bm.getA6Angel(expectedPose.getPose());
		vector[25] = (float)bm.getA7Angel(expectedPose.getPose());
		vector[26] = (float)bm.getA8Angel(expectedPose.getPose());
		vector[27] = (float)bm.getA9Angel(expectedPose.getPose());
		vector[28] = (float)bm.getA10Angel(expectedPose.getPose());
		vector[29] = (float)bm.getA11Angel(expectedPose.getPose());
		vector[30] = (float)bm.getA12Angel(expectedPose.getPose());
		vector[31] = (float)bm.getA13Angel(expectedPose.getPose());
		vector[32] = (float)bm.getA14Angel(expectedPose.getPose());
		vector[33] = (float)bm.getA15Angel(expectedPose.getPose());
		vector[34] = (float)bm.getA16Angel(expectedPose.getPose());
		vector[35] = (float)bm.getA17Angel(expectedPose.getPose());
		vector[36] = (float)bm.getA18Angel(expectedPose.getPose());
		vector[37] = (float)bm.getA19Angel(expectedPose.getPose());
		return vector;
	}
	public PoseCanvas getExpectedPose() {
		return expectedPose;
	}
	public void setExpectedPose(PoseCanvas expectedPose) {
		this.expectedPose = expectedPose;
	}
	public PoseCanvas getObservedPose() {
		return observedPose;
	}
	public void setObservedPose(PoseCanvas givenPose) {
		this.observedPose = givenPose;
	}
	
}
