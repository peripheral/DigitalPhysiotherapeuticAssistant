package com.app.classifiers.wrappers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core.FileNode;
import org.bytedeco.javacpp.opencv_core.FileStorage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_ml;
import org.bytedeco.javacpp.opencv_ml.ANN_MLP;
import org.bytedeco.javacpp.opencv_ml.KNearest;
import org.bytedeco.javacpp.opencv_ml.TrainData;

import com.app.classifiers.IClassifierCallbacks;
import com.app.classifiers.TargetModel;
import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;

import static org.bytedeco.javacpp.opencv_core.*;


public class KnnClassifierWrapper implements IClassifierWrapper {
	private int INPUT_VECTOR_SIZE = 19;
	private String knnOutDest;

	private boolean run = true;
	private String inputFile;
	private String targetFile;
	private int MATRIX_ROWS = 1000000;
	private String imageLocation ="images/knnTrained.ml";

	private KNearest knn = KNearest.create();
	public static void main(String[] args) {
		KnnClassifierWrapper knnWrap = new KnnClassifierWrapper();
		knnWrap.initiateFromFile();
		float[] featureVector = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Pointer p1 = new FloatPointer(featureVector);
		int rows = 1;
		Mat input = new Mat(rows, 19, CV_32F, p1);		
		float result = knnWrap.knn.predict(input);
		System.out.println(knnWrap.getLabel(knnWrap.predict(result)));
	}
	
	public void setInputLength(int i) {
		INPUT_VECTOR_SIZE = i;		
	}
	

	public void initiate(String fName){
		FileStorage fs = new FileStorage(fName, FileStorage.READ);			
		FileNode node = fs.getFirstTopLevelNode();
		knn = KNearest.create();
		knn.read(node);
		try {
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INPUT_VECTOR_SIZE = knn.getVarCount();
		imageLocation = fName;
		System.out.println("Knn loaded from:"+imageLocation);
	}
	
	/**
	 * Initiates KNN from file, set by setKnnImageLocation(string)
	 */
	public void initiateFromFile(){
		FileStorage fs = new FileStorage(imageLocation, FileStorage.READ);			
		FileNode node = fs.getFirstTopLevelNode();
		knn = KNearest.create();
		knn.read(node);
		try {
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INPUT_VECTOR_SIZE = knn.getVarCount();
	}
	//Current feature vector that is being processed
	private float[] featureVector;
	public void startUpdates(int rate,IClassifierCallbacks callbacks) {
		Thread t = new Thread(){
			public void run() {
				System.out.println("Knn Started from:"+imageLocation);
				final IClassifierCallbacks callbackI = callbacks;
				final int delay = 1000/rate;
				int rows = 1;
				Pointer p1;
				run = true;
				while(run){					
					try {
						sleep(delay);
						featureVector = callbackI.getFeatureVector();
						p1 = new FloatPointer(featureVector);
						Mat input = new Mat(rows, INPUT_VECTOR_SIZE, CV_32F, p1);
						float result = knn.predict(input);
						callbacks.setClassificationResult(CLASSIFIER_TYPE.KNN,getLabel(predict(result)));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};		
		};
		t.start();
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
		return demesnions;
	}

	private String getLabel(int classIndex) {		
		return TargetModel.getLabel(classIndex);		
	}

	public void initiate() {
		knn = KNearest.create();
		knn.setDefaultK(3);
		knn.setIsClassifier(true);
	}
	
	@Override
	public void train() {
		float[][] matrix = new float[2][];
		matrix[0] = new float[MATRIX_ROWS*INPUT_VECTOR_SIZE];
		matrix[1] = new float[MATRIX_ROWS];
		Scanner inputScanner = null;
		Scanner targetScanner = null;
		try {
			inputScanner = new Scanner(new File(inputFile));
			targetScanner = new Scanner(new File(targetFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		double error = 1;
		int rowsRead = readNextRows(matrix,inputScanner,targetScanner,MATRIX_ROWS,INPUT_VECTOR_SIZE);
		Pointer pInput = new FloatPointer(matrix[0]);
		Pointer pResp = new FloatPointer( matrix[1]);
		Mat targetData = new Mat(rowsRead,1, CV_32F,pResp);
		Mat inputData = new Mat(rowsRead, INPUT_VECTOR_SIZE, CV_32F, pInput);
		TrainData td = TrainData.create(inputData,opencv_ml.ROW_SAMPLE, targetData);
		knn.train(td);

		knn.save(knnOutDest);
		
		System.out.println("Knn training complete, trained on"+inputFile+" image printed to:"
				+ knnOutDest);
	}

	public void stop(IClassifierCallbacks ea) {
		run = false;
	}

	private void calcClassificationRate() {	
		float[][] matrix = new float[2][];
		Scanner inputSc = null;
		Scanner respSc = null;
		try {
			inputSc = new Scanner(new File(inputFile));
			respSc = new Scanner(new File(targetFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int inCols = 19;
		int outCols = 1;
		matrix = new float[2][];
		matrix[0] = new float[MATRIX_ROWS*inCols];
		matrix[1] = new float[MATRIX_ROWS*outCols];
		int rowsRead = readNextRows(matrix, inputSc, respSc, MATRIX_ROWS, inCols);	
		Pointer input = new FloatPointer(matrix[0]);
		Pointer resp = new FloatPointer(matrix[1]);
		Mat inputMat = new Mat(MATRIX_ROWS, inCols,CV_32F, input);
		Mat tarMat = new Mat(MATRIX_ROWS, outCols,CV_32F, resp);
		Mat result = new Mat();
		int correctResults = 0;
		int samplesTested = 0;
		for(int i = 0; i<rowsRead;i+=(rowsRead/5)){
			int temp =0;
			while(temp < 10 && i+temp < MATRIX_ROWS){
				float resultF = knn.predict(inputMat.row(i+temp));
				if(compareResult(resultF,tarMat.row(i+temp))){
					correctResults++;
				}
				temp++;
				samplesTested ++;
			}				
		}
		System.out.println("Ratio correct/total: "+(double)correctResults/samplesTested);

	}

	private boolean compareResult(float result, Mat tarMat) {
		FloatBuffer fb = tarMat.createBuffer();
		float[] expected = new float[fb.capacity()];
		fb.get(expected);
		System.out.println("Result:"+result+" Expected:"+expected[0]);
		if(result == expected[0]){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public void save(String fileName) {
		knn.save(fileName);		
	}

	public void setTrainingDataInputFile(String fName) {
		inputFile = fName;
	}

	public void setTrainingDataTargetFile(String fName) {
		targetFile = fName;

	}

	private int readNextRows(float[][] matrix,Scanner inputScanner,Scanner respReader, int rows, int cols) {
		int rowCount = 0;
		String value = "";
		while(inputScanner.hasNextLine()&& respReader.hasNextLine() && rowCount < rows){
			String[] values = inputScanner.nextLine().split(",");
			for(int i = 0; i < values.length;i++){
				matrix[0][cols* rowCount+i] = Integer.decode(values[i]);
			}
			value = respReader.nextLine();
			matrix[1][rowCount] = Integer.decode(value);
			rowCount++;
		}
		return rowCount;	
	}

	public float[] getFeatureVector() {
		return featureVector;
	}

	public void setImageOutDest(String fName) {
		knnOutDest = fName;
	}

	@Override
	public void setInputVectorLength(int inSize) {
		INPUT_VECTOR_SIZE = inSize;		
	}
	
	private String testDataInputFile;
	private String testTar;
	public double getClassificationRatio() {
		float[][] matrix = new float[2][];
		matrix[0] = new float[MATRIX_ROWS*INPUT_VECTOR_SIZE];
		matrix[1] = new float[MATRIX_ROWS];
		Scanner inputScanner = null;
		Scanner targetScanner = null;
		try {
			inputScanner = new Scanner(new File(testDataInputFile));
			targetScanner = new Scanner(new File(testTar));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		

		int rowsRead = readNextRows(matrix,inputScanner,targetScanner,MATRIX_ROWS,INPUT_VECTOR_SIZE);
		Pointer pInput = new FloatPointer(matrix[0]);
		Mat inputData = new Mat(rowsRead, INPUT_VECTOR_SIZE, CV_32F, pInput);
		int counter = 0;
		int correct = 0;

		for(int i = 0;i < rowsRead;i++){			
			if(matrix[1][i] == predict(knn.predict(inputData.row(i)))){
				correct++;
			}
			counter++;		
		}
		try {
			inputData.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (double)correct/counter;
	}

	private int predict(float prediction) {
		if(prediction -(int)prediction <= 0.5){
			return (int)prediction;
		}else{
			return (int)prediction+1;
		}
	}

	public void setTestDataInput(String testDataFile) {
		this.testDataInputFile = testDataFile;
	}

	public void setTestDataTarget(String testTar) {
		this.testTar = testTar;		
	}
	
}