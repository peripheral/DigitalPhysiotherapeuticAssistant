package com.app.classifiers;

public class TargetModel {

	public static String getLabel(int index) {
		if(index == 1){			
			return	"0-40%";
		}
		if(index == 2){			
			return	"40-50%";
		}
		if(index == 3){
			return	"50-60%";
		}
		if(index == 4){
			return	"60-80%";
		}
		if(index == 5){
			return	"80-100%";
		}
		return "\"NA\"";
	}
	public static int getLabelId(double percentage) {
		if(percentage<=40){			
			return	1;
		}
		if(percentage<=50){			
			return	2;
		}
		if(percentage<=60){
			return	3;
		}
		if(percentage<=80){
			return	4;
		}
		if(percentage<=100){
			return	5;
		}
		return -1;
	}

}
