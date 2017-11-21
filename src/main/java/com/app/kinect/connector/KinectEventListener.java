package com.app.kinect.connector;

import com.app.entities.Posture;

public interface KinectEventListener {
	public void onSkeletonFrameEvent(Posture p);
}
