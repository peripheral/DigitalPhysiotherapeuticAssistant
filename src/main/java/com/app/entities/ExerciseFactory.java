package com.app.entities;

import javax.swing.JList;

public class ExerciseFactory {
	public static Exercise[] makeExercises(String[] names){
		Exercise[] list = new Exercise[names.length];
		for (int i = 0;i<names.length;i++){
			list[i] = new Exercise(names[i]);
		}
		return list;
	}

	public static JList<Exercise> makeExercisesList(String[] names) {
		Exercise[] list = new Exercise[names.length];
		for (int i = 0;i<names.length;i++){
			list[i] = new Exercise(names[i]);
		}
		JList<Exercise> jList = new JList<>(list);
		return jList;
	}

}
