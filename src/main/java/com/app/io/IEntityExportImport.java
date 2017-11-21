package com.app.io;

import com.app.entities.Exercise;
import com.app.entities.Posture;

public interface IEntityExportImport {

	/**
	 * Exports provided posture, with use of filechooser as csv.
	 * @param p
	 */
	void export(Posture p);

	/**
	 * Exports provided Exercise, with use of filechooser as xml
	 * @param p
	 */
	void export(Exercise ex);

	/**
	 * Imports Posture entity, with use of filechooser. Reads from csv
	 * @param p
	 */
	Posture importPosture();

	/**
	 * Imports Exercise entity, with use of fileshooser. Reads from xml
	 * @return
	 */
	Exercise importExercise();

}
