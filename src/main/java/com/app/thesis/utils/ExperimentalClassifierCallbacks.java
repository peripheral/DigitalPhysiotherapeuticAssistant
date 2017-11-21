package com.app.thesis.utils;

import javax.swing.JButton;

import com.app.classifiers.ClassifierManagerImpl;
import com.app.classifiers.FeatureVectorModel;
import com.app.classifiers.IClassifierCallbacks;
import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;
import com.app.entities.Posture;
import com.app.graphics.GLAvatarPanel;
import com.app.gui.GraphicalUserInterface;
import com.app.gui.view.ExerciseView;
import com.app.thesis.utils.ExperimentalClassifierCallbacks.FEATURE_VECTOR_TYPE;

public class ExperimentalClassifierCallbacks extends ExerciseView{

	private ExperimentFascility callbacks;
	public enum FEATURE_VECTOR_TYPE{ANGLES,ANGLE_DEVIATIONS,PERCENTAGES};
	public FEATURE_VECTOR_TYPE type = FEATURE_VECTOR_TYPE.PERCENTAGES;

	public ExperimentalClassifierCallbacks(GraphicalUserInterface gui) {
		super(gui);
	}
	private PostureChangeListener kinectSim  = null;
	public void addPoseChangeListener(PostureChangeListener listener){
		kinectSim = listener;
	}
	private long lastUpdate = System.currentTimeMillis();
	@Override
	public void setClassificationResult(CLASSIFIER_TYPE type, String result) {
		super.setClassificationResult(type, result);
		double time = 1/((System.currentTimeMillis()-lastUpdate)/1000.0);
		lastUpdate = System.currentTimeMillis();
		callbacks.addDataEntry(type,result,time+"");
	}
	
	@Override
	public void setExpectedPosture(Posture p){
		super.setExpectedPosture(p);
		if(kinectSim != null){
			kinectSim.onPostureChange(p);
		}		
	}

	public void setCallbacks(ExperimentFascility experimentFascility) {
		callbacks = experimentFascility;		
	}

	public synchronized void setObservedPosture(Posture p) {
		p.copyTo(((GLAvatarPanel)avatarContainer).getPosture());
	}

	public JButton getBeginButton() {
		return beginExercise;	
	}
	
	@Override
	public void resetControls(){
		super.resetControls();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		callbacks.onExerciseEnded();
	}

	public boolean isRunning() {
		return !exerciseTerminated && exerciseStarted ;		
	}

	@Override
	public synchronized float[] getFeatureVector() {
		FeatureVectorModel fvm = new FeatureVectorModel();
		fvm.setObservedPosture(((GLAvatarPanel)avatarContainer).getPosture());
		fvm.setExpectedPosture(((GLAvatarPanel)expectedPostureContainer).getPosture());
		switch(type){
		case ANGLE_DEVIATIONS:
			return fvm.getFeatureVectorAngleDeviation();
		case ANGLES:
			return fvm.getFeatureVectorAngles();
		case PERCENTAGES:
			return fvm.getFeatureVectorPercentages();
		default:
			return super.getFeatureVector();
		}
	}

	public void setFeatureVectorType(FEATURE_VECTOR_TYPE type) {
		this.type = type;		
	}
	
	private void setCalculatedResult() {
		ClassifierManagerImpl man = new ClassifierManagerImpl();
		manualResult.setText(man.classifySampleAsLabelFromPercentages(getFeatureVector()));
	}

	public FEATURE_VECTOR_TYPE getFeatureVectorType() {
		return type;
	}
}
