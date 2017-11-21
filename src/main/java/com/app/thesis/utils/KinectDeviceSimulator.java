package com.app.thesis.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.app.entities.Posture;
import com.app.kinect.connector.KinectEventListener;

public class KinectDeviceSimulator implements PostureChangeListener{
	private List<KinectEventListener> listeners = new LinkedList<KinectEventListener>();
	private String dataFile;
	private PostureReader postureFileReader;
	private Posture current = new Posture();
	private boolean running = true;
	private List<String> files = new LinkedList<>();
	
	public void addDataFile(String filename){
		getFiles().add(filename);
	}

	public void addKinectEventListener(KinectEventListener l){
		listeners.add(l);
	}
	public void setKinectDataFile(String string) {
		dataFile = string;	
	}
	private boolean threadStopped = false;
	private int fileCounter = 0;
	private int fps = 10;
	public void run(){
		new Thread(){		

			

			@Override
			public void run() {
				postureFileReader = new PostureReader(dataFile);
				running = true;
				threadStopped= false;
				Posture p;
				while(running ){
					if(!postureFileReader.hasNext()){
						if(files.size()>1){
							break;
						}
						postureFileReader.closeScanner();
						postureFileReader = new PostureReader(files.get(fileCounter));
						fileCounter = (++fileCounter)%files.size();
					}
					p = postureFileReader.nextSample();
					for(KinectEventListener l:listeners){
						l.onSkeletonFrameEvent(p);
					}
					try {
						sleep(1000/fps);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				postureFileReader.closeScanner();
				running = false;
				threadStopped = true;
			}
		}.start();
	}
	
	public void stop() {
		running = false;		
	}
	public boolean isRunning() {
		return running;
	}
	@Override
	public void onPostureChange(Posture p) {
		for(String filename:getFiles()){
			System.out.println("Posture name:"+p.getName()+", File name:"+filename);
			if(filename.toLowerCase().contains(p.getName().toLowerCase())){
				dataFile = filename;
				stop();
				start();
			}
		}
	}

	private void start() {
		while(!threadStopped){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		run();		
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
	
}
