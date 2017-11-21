package com.app.io;

import com.app.entities.Posture;

public interface IPostureFileReader {

	/**
	 * Reads next entity.
	 * @return - Posture object or null
	 */
	Posture nextSample();

	/**
	 * 
	 * @return true if there is next entity in input, else false
	 */
	boolean hasNext();

	/**
	 * Closing the reader
	 */
	void closeScanner();

}
