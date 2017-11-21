package com.app.classifiers;

import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;

public interface IClassifierManager {
	/**
	 * Subscribe to a classifier. The classification will occur through callbacks interface. 
	 * New thread runs per classifier and updates asynchronously.
	 * Refresh rate is 30 times/sec or less, bases on hardware speed and limitations
	 * @param type
	 * @param callbacks
	 */
	public void subcribeTo(CLASSIFIER_TYPE type, IClassifierCallbacks callbacks);
	
	/**
	 * Stops thread that executed updates
	 * @param type
	 * @param callbacks
	 */
	public void unsubscribe(CLASSIFIER_TYPE type,IClassifierCallbacks callbacks);
	
	/**
	 * Classifies sample 
	 * @param percentages - vector containing percentile deviation between 
	 * expected and observed angles in percents
	 * @return - label
	 */
	public String classifySampleAsLabelFromPercentages(float[] percentages);
	
	/**
	 * Stops all current threads tasked with running classifiers and updating the label on ui
	 * @param callbacks
	 */
	public void unsubscribeAll(IClassifierCallbacks callbacks);
}
