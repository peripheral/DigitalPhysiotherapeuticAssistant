package com.app.classifiers.wrappers;

import com.app.classifiers.IClassifierCallbacks;

public interface IClassifierWrapper {
	public void initiate(String image);
	public void initiate();
	public void train();
	public void save(String fileName);
	public void startUpdates(int refreshRate,IClassifierCallbacks callbacks);
	public void stop(IClassifierCallbacks callbacks);
	public void setTrainingDataInputFile(String text);
	public void setTrainingDataTargetFile(String text);
	public void setImageOutDest(String string);
	public void setInputVectorLength(int inSize);
}
