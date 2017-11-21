package com.app.ml.nn.mynn;

import com.app.ml.nn.mynn.ConstantsEnums.ActivationFuctionType;

public class Formula {

	public static double compute(float net, ActivationFuctionType aFType) {
		switch(aFType){
		case GAUSIAN : 
			return  Math.pow(Math.E,-(net*net));
		case SIGMOID : 
			return (1-Math.pow(Math.E,-net))/(1+Math.pow(Math.E,-net));
		default:
			System.err.println("Unrecognized activation function id");
			return 0;
		}
	}

}
