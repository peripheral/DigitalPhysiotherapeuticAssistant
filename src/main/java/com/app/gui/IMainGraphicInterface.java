package com.app.gui;

import com.app.constants.Constants.VIEW_ID;
import com.app.entities.Exercise;
import com.app.entities.Posture;

public interface IMainGraphicInterface {
	/**
	 * Opens graphical tab.
	 * @param id
	 */
	public void openView(VIEW_ID id);
	/**
	 * Initiates graphical interface for exercising with posture p.
	 * @param p
	 */
	public void loadPosture(Posture p);
	/**
	 * Initiates graphical panel for exercising with with Exercise e.
	 * @param e
	 */
	public void loadExercise(Exercise e);
}
