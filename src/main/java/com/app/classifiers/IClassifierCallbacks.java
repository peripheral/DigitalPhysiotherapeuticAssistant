package com.app.classifiers;

import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;

public interface IClassifierCallbacks {

	public void setClassificationResult(CLASSIFIER_TYPE type, String label);

	public float[] getFeatureVector();

}
