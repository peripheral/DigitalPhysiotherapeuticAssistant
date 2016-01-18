package app.classifier_wrappers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_ml;
import org.bytedeco.javacpp.opencv_ml.RTrees;
import org.bytedeco.javacpp.opencv_ml.TrainData;

import app.Pose;
import app.ui.UserInterface;

import static org.bytedeco.javacpp.opencv_core.*;


public class RandomForestClassifierWrapper {
	//static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	private Mat samples;
	private Map<String,Float> labelMap = null;
	private float tResponse[];
	public static void main(String[] args) {
		//System.out.println("Welcome to OpenCV " + Core.VERSION);
		UserInterface ui = new UserInterface();
		RandomForestClassifierWrapper rFCWrapper =  new RandomForestClassifierWrapper();
		rFCWrapper.initiate();
		rFCWrapper.start(25,ui);
		rFCWrapper.test();		
	}

	public void start(int rate,UserInterface callback) {
		Thread t = new Thread(){
			public void run() {
				final UserInterface callbackI = callback;
				final Scence scene = callbackI.getScene();
				final int delay = 1000/rate;
				int rows = 1;
				int columns = 38;
				while(true){					
					try {
						sleep(delay);
						
						
						float[] featureVector = scene.getFeatureVector();
						final Pointer p1 = new FloatPointer(featureVector);
						Mat input = new Mat(1, 38, CV_32F, p1);
						callback.setTreeClassificationResult(getLabel(rTrees.predict(input)));
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};		
		};
		t.start();
	}

	private String getLabel(float classV) {
		int labelId = (int)classV;
		if(classV - labelId > 0.5){
			labelId++;
		}
		Set<String> keys = labelMap.keySet();
		for(String k:keys){
			if(labelMap.get(k) == labelId){
				return k;
			}
		}
		return "NA";		
	}

	private RTrees rTrees = null;
	
	private void test() {
		// TODO Auto-generated method stub
		
	}

	public void initiate(){
	
		Scanner lineScanner = null;
		File input = new File("iAnglesSetA.csv");
		File resp = new File("tLabelsSetA.csv");		
		Map<String,Integer> dims = getDimensions(input);
		int rows = 0;
		int cols = 0;
		rows = dims.get("rows");
		cols = dims.get("columns");
		FloatBuffer sampleBuffer =  FloatBuffer.allocate(rows*cols);
		try{
			rTrees = RTrees.create();
		}catch(Exception e){
			System.err.println("Failed to create RTrees");
			e.printStackTrace();
		}
		try {
			lineScanner =  new Scanner(input);
			int counter = 0;
			while(lineScanner.hasNextLine()){
				String nextLine = lineScanner.nextLine();
				String[] split = nextLine.split(",");
				float[] data = new float[cols];

				for(String val:split){
					data[counter++] = (new Float(val)).floatValue();
				}
				counter=0;
				sampleBuffer.put(data);
			}
			lineScanner.close();
			final float[] inputMat = sampleBuffer.array();
			final Pointer p = new FloatPointer(inputMat);
			samples = new Mat(rows, cols, CV_32F, p);

			dims = getDimensions(resp);
			rows = dims.get("rows");
			cols = dims.get("columns");
			String buffResponse = new String();
			counter = 0;
			lineScanner =  new Scanner(resp);
			float[] labelsFloats = new float[rows];
			String[] labels = new String[rows];
			float labelCount = 1;
			labelMap = new TreeMap<>();
			while(lineScanner.hasNextLine()){
				String nextLine = lineScanner.nextLine();
				String label = nextLine.substring(1, nextLine.length()-1).trim();
				labels[counter] = label;
				if(!labelMap.containsKey( label)){
					labelsFloats[counter++] = labelCount;
					labelMap.put(label, labelCount++);
				}else{
					labelsFloats[counter++] = labelMap.get(label);
				}
			}
			lineScanner.close();

		
			final Pointer pResp = new FloatPointer(labelsFloats);
			Mat responses = new Mat(rows, 1, CV_32F,pResp);

			//	System.out.println("About to print"+samples.dump());

			//	System.out.println("OpenCV Mat data:\n" + samples.dump());
			//((StatModel)rTrees)
			//System.out.println("OpenCV Mat data:\n" + responses.dump());
			TrainData tData = new TrainData();
			//tData.
			rTrees.train(samples,opencv_ml.ROW_SAMPLE, responses);	
			

			final Pointer p1 = new FloatPointer(inputMat);
			Mat tSamples = new Mat(rows, cols, CV_32F, p1);
			tResponse = new float[samples.rows()];
			Pointer x = new FloatPointer(tResponse);
			//final Mat testResults = new Mat(samples.rows(),1,CV_32F, x);
			//MatAllocator mAloc = testResults.allocator();
			
			System.out.println(inputMat);
			for(int i = 0 ; i < 75000;i+=100){
				System.out.println("Given:" + rTrees.predict(samples.row(i))+" Expected:"+labels[i]);
			}
		
			//System.out.println(rTrees.predict(tSamples,testResult,RTrees.PREDICT_MAX_VOTE));
			//	System.out.println(rTrees.predict(samples.row(6000),responses.row(1),RTrees.PREDICT_MAX_VOTE));
			System.out.println("done"+rTrees.isTrained()+rTrees.isClassifier()+/*testResult+*/" Err: ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Map<String, Integer> getDimensions(File input) {
		Scanner lineScanner = null;
		int rows = 0,cols = 0;
		try{
			lineScanner = new Scanner(input);
		}catch(Exception e){
			System.err.println("Inccorect file");
			System.exit(-1);
		}
		if(lineScanner.hasNextLine()){
			String[] rowV = lineScanner.nextLine().split(",");
			rows ++;
			cols = rowV.length;
		}
		while(lineScanner.hasNextLine()){
			lineScanner.nextLine();
			rows ++;
		}
		lineScanner.close();
		Map<String,Integer> demesnions = new TreeMap<String, Integer>();
		demesnions.put("rows", rows);
		demesnions.put("columns", cols);
		System.out.println("Dimensions of "+input.getName()+" :"+cols+"Cols and "+rows+" Rows.");
		return demesnions;
	}
}