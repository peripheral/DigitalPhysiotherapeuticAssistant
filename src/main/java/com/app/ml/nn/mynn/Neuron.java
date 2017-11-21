package com.app.ml.nn.mynn;

import com.app.ml.nn.mynn.ConstantsEnums.ActivationFuctionType;
import com.app.ml.nn.mynn.ConstantsEnums.NeuronType;

public class Neuron {
	private float threshold = 1;
	
	//private float threshold = 0;
	
	public float getThreshold() {
		return threshold;
	}
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
}
