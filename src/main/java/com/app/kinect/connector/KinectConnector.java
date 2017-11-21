package com.app.kinect.connector;

import java.util.LinkedList;
import java.util.List;

import com.app.entities.Posture;
import com.app.graphics.avatar.BodyModelImpl;
import com.app.graphics.avatar.BodyModelImpl.JOINT_TAG;

import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;


/*
 * Copyright 2011-2014, Digital Worlds Institute, University of 
 * Florida, Angelos Barmpoutis.
 * All rights reserved.
 *
 * When this program is used for academic or research purposes, 
 * please cite the following article that introduced this Java library: 
 * 
 * A. Barmpoutis. "Tensor Body: Real-time Reconstruction of the Human Body 
 * and Avatar Synthesis from RGB-D', IEEE Transactions on Cybernetics, 
 * October 2013, Vol. 43(5), Pages: 1347-1356. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain this copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce this
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class KinectConnector extends J4KSDK{


	boolean mask_players=false;
	public void maskPlayers(boolean flag){mask_players=flag;}

	public KinectConnector()
	{
		super();
	}

	public KinectConnector(byte type)
	{
		super(type);
	}




	private boolean use_infrared=false;
	private List<KinectEventListener> listeners = new LinkedList<KinectEventListener>();

	public void updateTextureUsingInfrared(boolean flag)
	{
		use_infrared=flag;
	}

	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
		float a[]=getAccelerometerReading();

		DepthMap map=new DepthMap(getDepthWidth(),getDepthHeight(),XYZ);
		map.setMaximumAllowedDeltaZ(0.5);

		if(UV!=null && !use_infrared) map.setUV(UV);
		else if(use_infrared) map.setUVuniform();
		if(mask_players)
		{
			map.setPlayerIndex(depth_frame, player_index);
			map.maskPlayers();
		}
	}

	/**
	 * Updates posture of listeners. The posture with id 0 is used
	 */
	@Override
	public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {
		Skeleton s = null;
		for(int i=0;i<getSkeletonCountLimit();i++)
		{
			s  = Skeleton.getSkeleton(i, flags,positions, orientations,state,this);	
			break;
		}	
		for(KinectEventListener listener:listeners){
			listener.onSkeletonFrameEvent(converToPosture(s));
		}
	}

	private Posture converToPosture(Skeleton s) {
		Posture p = new Posture();
		p.setJoinLocation(JOINT_TAG.HEAD.name(), s.get3DJoint(Skeleton.HEAD));
		p.setJoinLocation(JOINT_TAG.NECK.name(), s.get3DJoint(Skeleton.NECK));
		p.setJoinLocation(JOINT_TAG.SPINE_SHOULDER.name(), s.get3DJoint(Skeleton.SPINE_SHOULDER));
		p.setJoinLocation(JOINT_TAG.SHOULDER_LEFT.name(), s.get3DJoint(Skeleton.SHOULDER_LEFT));
		p.setJoinLocation(JOINT_TAG.SHOULDER_RIGHT.name(), s.get3DJoint(Skeleton.SHOULDER_RIGHT));
		p.setJoinLocation(JOINT_TAG.ELBOW_LEFT.name(), s.get3DJoint(Skeleton.ELBOW_LEFT));
		p.setJoinLocation(JOINT_TAG.ELBOW_RIGHT.name(), s.get3DJoint(Skeleton.ELBOW_RIGHT));
		p.setJoinLocation(JOINT_TAG.WRIST_LEFT.name(), s.get3DJoint(Skeleton.WRIST_LEFT));
		p.setJoinLocation(JOINT_TAG.WRIST_RIGHT.name(), s.get3DJoint(Skeleton.WRIST_RIGHT));
		p.setJoinLocation(JOINT_TAG.HAND_LEFT.name(), s.get3DJoint(Skeleton.HAND_LEFT));
		p.setJoinLocation(JOINT_TAG.HAND_RIGHT.name(), s.get3DJoint(Skeleton.HAND_RIGHT));
		p.setJoinLocation(JOINT_TAG.HAND_TIP_LEFT.name(), s.get3DJoint(Skeleton.HAND_TIP_LEFT));
		p.setJoinLocation(JOINT_TAG.HAND_TIP_RIGHT.name(), s.get3DJoint(Skeleton.HAND_TIP_RIGHT));
		p.setJoinLocation(JOINT_TAG.THUMB_LEFT.name(), s.get3DJoint(Skeleton.THUMB_LEFT));
		p.setJoinLocation(JOINT_TAG.THUMB_RIGHT.name(), s.get3DJoint(Skeleton.THUMB_RIGHT));
		p.setJoinLocation(JOINT_TAG.SPINE_MID.name(), s.get3DJoint(Skeleton.SPINE_MID));
		p.setJoinLocation(JOINT_TAG.SPINE_BASE.name(), s.get3DJoint(Skeleton.SPINE_BASE));
		p.setJoinLocation(JOINT_TAG.HIP_LEFT.name(), s.get3DJoint(Skeleton.HIP_LEFT));
		p.setJoinLocation(JOINT_TAG.HIP_RIGHT.name(), s.get3DJoint(Skeleton.HIP_RIGHT));
		p.setJoinLocation(JOINT_TAG.KNEE_LEFT.name(), s.get3DJoint(Skeleton.KNEE_LEFT));
		p.setJoinLocation(JOINT_TAG.KNEE_RIGHT.name(), s.get3DJoint(Skeleton.KNEE_RIGHT));
		p.setJoinLocation(JOINT_TAG.ANKLE_LEFT.name(), s.get3DJoint(Skeleton.ANKLE_LEFT));
		p.setJoinLocation(JOINT_TAG.ANKLE_RIGHT.name(), s.get3DJoint(Skeleton.ANKLE_RIGHT));
		p.setJoinLocation(JOINT_TAG.FOOT_LEFT.name(), s.get3DJoint(Skeleton.FOOT_LEFT));
		p.setJoinLocation(JOINT_TAG.FOOT_RIGHT.name(), s.get3DJoint(Skeleton.FOOT_RIGHT));
		return p;
	}

	@Override
	public void onColorFrameEvent(byte[] data) {
		
	}

	@Override
	public void onInfraredFrameEvent(short[] data) {

	}

	public void addKinectEventListener(KinectEventListener listener) {
		listeners.add(listener);		
	}

}
