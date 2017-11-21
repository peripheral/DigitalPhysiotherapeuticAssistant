package com.app.thesis.utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JFrame;

import com.app.classifiers.ClassifierManagerImpl;
import com.app.classifiers.IClassifierManager;
import com.app.classifiers.wrappers.AnnClassifierWrapper;
import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;
import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.classifiers.wrappers.KnnClassifierWrapper;
import com.app.classifiers.wrappers.RandomTreesClassifierWrapper;
import com.app.db.DAO;
import com.app.gui.GraphicalUserInterface;
import com.app.gui.view.ExerciseView;
import com.app.io.EntityExportImport;
import com.app.kinect.connector.KinectEventListener;
import com.app.thesis.utils.ExperimentalClassifierCallbacks.FEATURE_VECTOR_TYPE;

public class ExperimentFascility {

	private String exerciseFrom = "testExercise.xml";
	private String annImageA = "annTrainedA.ml";
	private String annImageD = "annTrainedD.ml";
	private String annImageP = "annTrainedP.ml";
	private String knnImageA = "knnTrainedA.ml";
	private String knnImageD = "knnTrainedD.ml";
	private String knnImageP = "knnTrainedP.ml";
	private String rTreesImageA = "rTreesTrainedA.ml";
	private String rTreesImageD = "rTreesTrainedD.ml";
	private String rTreesImageP = "rTreesTrainedP.ml";
	private boolean experimentRunning = false;
	private String bendDataFile = "forward_bending_R.csv";
	private String standing_pose = "standing_pose_R.csv";
	private String stretch_standing = "strech_standing_R.csv";
	private String statfile = "dataFinal.csv";

	public static void main(String[] args){
		ExperimentFascility eF = new ExperimentFascility();
		eF.setLogFileKnn("knnLogFile.csv");
		eF.setLogFileAnn("annLogfile.csv");
		eF.setLogFileRTrees("rTreesLogFile.csv");
		eF.setKinectFile("data.csv");
		eF.rename(".","1000000.");
		eF.runExperiment1();
		boolean experimentNotComplete = true;
		while(experimentNotComplete){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			experimentNotComplete = eF.isExperimentRunning();
		}

		experimentNotComplete = true;
		System.out.println("First experiment finished.");
		eF.runExperiment2();
		while(experimentNotComplete){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			experimentNotComplete = eF.isExperimentRunning();
		}
		eF.runExperiment3();
		System.out.println("Experiments completed");
		while(experimentNotComplete){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			experimentNotComplete = eF.isExperimentRunning();
		}
		eF.closeStatWriter();
		eF.terminateApp();
		System.exit(0);
	}

	private void rename(String oldStr, String newStr) {
		annImageA =  annImageA.replace(oldStr, newStr);
		annImageD = annImageD.replace(oldStr, newStr);
		annImageP = annImageP.replace(oldStr, newStr);
		knnImageA =  knnImageA.replace(oldStr, newStr);
		knnImageD = knnImageD.replace(oldStr, newStr);
		knnImageP = knnImageP.replace(oldStr, newStr);
		rTreesImageA =  rTreesImageA.replace(oldStr, newStr);
		rTreesImageD =  rTreesImageD.replace(oldStr, newStr);
		rTreesImageP =  rTreesImageP.replace(oldStr, newStr);
		statfile = statfile.replace(oldStr, newStr);

		try {
			statsWriter.close();
			statsWriter = new FileWriter(new File(statfile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void terminateApp() {
		gui.dispose();
	}

	private void closeStatWriter() {
		try {
			statsWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isExperimentRunning() {
		return experimentRunning;
	}

	private boolean isExerciseRunning() {
		if(exercisePanel == null || kinect == null){
			return false;
		}
		return exercisePanel.isRunning() || kinect.isRunning();
	}

	private String logFileRTrees = "";
	private String logFileAnn = "";
	private String logFileKnn;
	private String kinectDataBuffer;
	private String kinectDataBufferStanding = "standing_pose_R.csv";
	private String kinectDataBufferSForwardBending = "forward_bending_R.csv";
	private String kinectDataBufferStretchStanding = "stretch_standing_R.csv";

	private void setLogFileRTrees(String destination) {
		logFileRTrees  = destination;

	}

	private void setLogFileAnn(String destination) {
		logFileAnn = destination;

	}

	private void setLogFileKnn(String destination) {
		logFileKnn = destination;		
	}

	private void setKinectFile(String from) {
		kinectDataBuffer = from;	
	}

	private GraphicalUserInterface gui;
	private KinectDeviceSimulator kinect;
	private AnnClassifierWrapper annWrapper;
	private KnnClassifierWrapper knnWrapper;
	private RandomTreesClassifierWrapper rTreesWrapper;
	private String annOutDestination = "annOut.csv";
	private String knnOutDestination =  "knnOut.csv";
	private String rTreesOutDestination =  "rTreesOut.csv";
	private FileWriter annFileWriter = null;
	private FileWriter knnFileWriter = null;
	private FileWriter rTreesFileWriter = null;
	private ClassifierManagerImpl cManager;
	private ExperimentalClassifierCallbacks exercisePanel;

	public ExperimentFascility(){
		/*	JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(400,400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);*/
		gui = new GraphicalUserInterface();
		exercisePanel = new ExperimentalClassifierCallbacks(gui);
		exercisePanel.setCallbacks(this);
		gui.setExercisePanel(exercisePanel);
		cManager = exercisePanel.getClassifierManager();
		annWrapper = cManager.getAnnCWrapper();
		knnWrapper = cManager.getKnnCWrapper();
		rTreesWrapper = cManager.getRTreesCWrapper();

		exercisePanel.getBeginButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				kinect.run();
			}
		});
		try {
			statsWriter = new FileWriter(new File(statfile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void addDataEntry(CLASSIFIER_TYPE type, String result,String frequency) {
		switch(type){
		case ANN:
			write(annFileWriter,annWrapper.getFeatureVector(),result,frequency);
			break;
		case KNN:
			write(knnFileWriter,knnWrapper.getFeatureVector(),result,frequency);
			break;
		case RANDOMTREES:
			write(rTreesFileWriter,rTreesWrapper.getFeatureVector(),result,frequency);
			break;
		}
	}

	private void write(FileWriter writer, float[] featureVector, String result,String frequency) {
		String expected = "";
		switch(exercisePanel.getFeatureVectorType()){
		case ANGLE_DEVIATIONS:
			expected = cManager.classifySampleAsLabelFromAngleDevs(featureVector);
			break;
		case ANGLES:
			expected = cManager.classifyAsLabelFromAngleArray(featureVector);
			break;
		case PERCENTAGES:
			expected = cManager.classifyAsLabelFromPercentsArray(featureVector);
			break;
		}
		try {
			for(int i = 0; i < featureVector.length;i++){
				//writer.write(featureVector[i]+",");
			}
			writer.write(frequency+",");
			writer.write(result+","+expected+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private CLASSIFIER_TYPE activeClassifier = null;
	private String postfix;
	private FileWriter statsWriter;
	public void onExerciseEnded() {
		try {
			switch(activeClassifier){
			case ANN:
				annFileWriter.close();
				break;
			case KNN:
				knnFileWriter.close();
				break;
			case RANDOMTREES:
				rTreesFileWriter.close();
				break;
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		Scanner sc = null;
		TreeMap<String,Integer> result = new TreeMap<>();
		switch(activeClassifier){
		case ANN:
			try {
				sc = new Scanner(new File(annOutDestination.replace(".", postfix+".")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			runStats(sc,result);
			sc.close();
			System.out.println("Correctly calssified by ann:"+result.get("correct")+" Total entries:"
					+result.get("total"));
			break;
		case KNN:
			try {
				sc = new Scanner(new File(knnOutDestination.replace(".", postfix+".")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			runStats(sc,result);
			sc.close();
			System.out.println("Correctly calssified by KNN:"+result.get("correct")+" Total entries:"
					+result.get("total"));
			break;
		case RANDOMTREES:
			try {
				sc = new Scanner(new File(rTreesOutDestination.replace(".", postfix+".")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			runStats(sc,result);
			sc.close();
			System.out.println("Correctly calssified by RandomTrees:"+result.get("correct")+" Total entries:"
					+result.get("total"));
			break;
		}
		writeStats(statsWriter,result,activeClassifier);
	}

	private void writeStats(FileWriter statsWriter, TreeMap<String, Integer> result,
			CLASSIFIER_TYPE activeClassifier) {
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			statsWriter.write(activeClassifier.name()+",");
			statsWriter.write(postfix+",");
			statsWriter.write(df.format((double)result.get("correct")/result.get("total"))
					.replaceAll(",", ".")+",");
			statsWriter.write(result.get("averageFrequency")+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void runStats(Scanner sc, TreeMap<String, Integer> result) {
		String[] cols ;
		int count = 0;
		int total = 0;
		double frequency = 0;
		while(sc.hasNextLine())	{
			cols = sc.nextLine().split(",");
			if(cols[cols.length-2].equals(cols[cols.length-1])){
				count ++;
			}
			frequency = frequency + Double.parseDouble(cols[cols.length-3]);
			total++;
		}
		frequency= frequency/total;
		result.put("correct", count);
		result.put("total",total);
		result.put("averageFrequency",(int) frequency);

	}

	public void runExperiment1(){
		experimentRunning = true;
		cManager.setAnnImageFile(annImageA);
		cManager.setKnnImageFile(knnImageA);
		cManager.setrTreesImageFile(rTreesImageA);
		// Part one 1 begins
		//Specify feature vector
		exercisePanel.setFeatureVectorType(FEATURE_VECTOR_TYPE.ANGLES);
		exercisePanel.getRadioBtnAnn().doClick();
		prepareFileWriters("ex1",CLASSIFIER_TYPE.ANN);



		//	load Exercise from file
		EntityExportImport io = new EntityExportImport();
		Exercise e = io.importExerciseFromFile(exerciseFrom);
		gui.loadExercise(e);
		// Load kinect simulator
		kinect = new KinectDeviceSimulator();
		kinect.setKinectDataFile(kinectDataBuffer);
		addAllDataFiles(kinect.getFiles());
		kinect.addKinectEventListener(new KinectEventListener() {
			@Override
			public void onSkeletonFrameEvent(Posture p) {
				exercisePanel.setObservedPosture(p);				
			}
		});	
		//Add kinect change listener
		exercisePanel.addPoseChangeListener(kinect);


		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		boolean exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		//End of part 1 of experiment
		System.out.println("Experiment part 1 complete");
		System.out.println(isExerciseRunning());
		exercisePanel.getRadioBtnKnn().doClick();
		prepareFileWriters("ex1",CLASSIFIER_TYPE.KNN);
		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		System.out.println("Experiment part 2 complete");

		exercisePanel.getRadioBtnRTrees().doClick();
		prepareFileWriters("ex1",CLASSIFIER_TYPE.RANDOMTREES);
		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		System.out.println("Experiment part 3 complete");
		experimentRunning = false;
	}

	public void runExperiment2(){
		experimentRunning = true;
		cManager.setAnnImageFile(annImageD);
		cManager.setKnnImageFile(knnImageD);
		cManager.setrTreesImageFile(rTreesImageD);

		// Part one 1 begins
		//Specify feature vector
		exercisePanel.setFeatureVectorType(FEATURE_VECTOR_TYPE.ANGLE_DEVIATIONS);
		exercisePanel.getRadioBtnAnn().doClick();
		prepareFileWriters("ex2",CLASSIFIER_TYPE.ANN);	

		//	load Exercise from file
		EntityExportImport io = new EntityExportImport();
		Exercise e = io.importExerciseFromFile(exerciseFrom);
		gui.loadExercise(e);
		// Load kinect simulator
		kinect = new KinectDeviceSimulator();
		kinect.setKinectDataFile(kinectDataBuffer);
		addAllDataFiles(kinect.getFiles());
		kinect.addKinectEventListener(new KinectEventListener() {
			@Override
			public void onSkeletonFrameEvent(Posture p) {
				exercisePanel.setObservedPosture(p);				
			}
		});	
		//Add kinect change listener
		exercisePanel.addPoseChangeListener(kinect);


		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		boolean exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		//End of part 1 of experiment
		System.out.println("Experiment part 1 complete");
		System.out.println(isExerciseRunning());
		exercisePanel.getRadioBtnKnn().doClick();
		prepareFileWriters("ex2",CLASSIFIER_TYPE.KNN);
		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		System.out.println("Experiment part 2 complete");

		exercisePanel.getRadioBtnRTrees().doClick();
		prepareFileWriters("ex2",CLASSIFIER_TYPE.RANDOMTREES);
		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		System.out.println("Experiment part 3 complete");
		experimentRunning = false;
	}

	public void runExperiment3(){
		experimentRunning = true;
		// Part one 1 begins
		//Specify feature vector
		exercisePanel.setFeatureVectorType(FEATURE_VECTOR_TYPE.PERCENTAGES);
		exercisePanel.getRadioBtnAnn().doClick();
		prepareFileWriters("ex3",CLASSIFIER_TYPE.ANN);

		cManager.setAnnImageFile(annImageP);
		cManager.setKnnImageFile(knnImageP);
		cManager.setrTreesImageFile(rTreesImageP);

		//	load Exercise from file
		EntityExportImport io = new EntityExportImport();
		Exercise e = io.importExerciseFromFile(exerciseFrom);
		gui.loadExercise(e);
		// Load kinect simulator
		kinect = new KinectDeviceSimulator();
		kinect.setKinectDataFile(kinectDataBuffer);
		addAllDataFiles(kinect.getFiles());
		kinect.addKinectEventListener(new KinectEventListener() {
			@Override
			public void onSkeletonFrameEvent(Posture p) {
				exercisePanel.setObservedPosture(p);				
			}
		});	
		//Add kinect change listener
		exercisePanel.addPoseChangeListener(kinect);


		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		boolean exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		//End of part 1 of experiment
		System.out.println("Experiment part 1 complete");
		System.out.println(isExerciseRunning());
		exercisePanel.getRadioBtnKnn().doClick();
		prepareFileWriters("ex3",CLASSIFIER_TYPE.KNN);
		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		System.out.println("Experiment part 2 complete");

		exercisePanel.getRadioBtnRTrees().doClick();
		prepareFileWriters("ex3",CLASSIFIER_TYPE.RANDOMTREES);
		//Start kinect
		kinect.run();
		//Start exercise
		exercisePanel.getBeginButton().doClick();
		exerciseRunning = true;
		while(exerciseRunning){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			exerciseRunning = isExerciseRunning();
		}
		System.out.println("Experiment part 3 complete");
		experimentRunning = false;
	}

	public void addAllDataFiles(List<String> list){
		list.add("data.csv");
		list.add(standing_pose);
		list.add(bendDataFile);
		list.add(stretch_standing);	
	}

	private void prepareFileWriters(String string,CLASSIFIER_TYPE type) {
		activeClassifier = type;
		postfix = string;
		//Prepare file writers
		try {
			switch(type){
			case ANN:
				annFileWriter = new FileWriter(new File(annOutDestination.replace(".",string+".")));
				break;
			case KNN:
				knnFileWriter = new FileWriter(new File(knnOutDestination.replace(".",string+".")));
				break;
			case RANDOMTREES:
				rTreesFileWriter = new FileWriter(new File(rTreesOutDestination.replace(".",string+".")));
				break;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
