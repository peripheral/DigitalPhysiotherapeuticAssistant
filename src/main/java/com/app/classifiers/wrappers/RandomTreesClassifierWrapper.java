package com.app.classifiers.wrappers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core.FileNode;
import org.bytedeco.javacpp.opencv_core.FileStorage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_ml;
import org.bytedeco.javacpp.opencv_ml.RTrees;
import org.bytedeco.javacpp.opencv_ml.TrainData;
import com.app.classifiers.IClassifierCallbacks;
import com.app.classifiers.TargetModel;
import com.app.classifiers.wrappers.AnnClassifierWrapper.CLASSIFIER_TYPE;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

import static org.bytedeco.javacpp.opencv_core.*;


public class RandomTreesClassifierWrapper implements IClassifierWrapper{

	private RTrees rTrees = RTrees.create();
	private String ImageOutputDest;
	private int MAX_ITERATIONS = 50;
	
	private Pointer inputData;
	private Pointer outputData;

	private int MATRIX_ROWS = 1000000;
	private int INPUT_VECTOR_LENGTH  = 19;
	private String imageLocation = "images/rTreesTrained.ml";
	private String imageOutputDest;
	private String testData;
	private String testTarget ;
	
	public RandomTreesClassifierWrapper(){}

	public static void main(String[] args) {
		RandomTreesClassifierWrapper rTreesWrap = new RandomTreesClassifierWrapper();
		rTreesWrap.initiateFromFile();
		float[] featureVector = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Pointer p1 = new FloatPointer(featureVector);
		int rows = 1;
		Mat input = new Mat(rows, 19, CV_32F, p1);		
		System.out.println(rTreesWrap.getTargetLabel((int)rTreesWrap.predict(input,false)));

	}

	private void initiateFromFile() {
		FileStorage fs = new FileStorage(imageLocation, FileStorage.READ);			
		FileNode node = fs.getFirstTopLevelNode();
		rTrees = RTrees.create();
		rTrees.read(node);
		try {
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INPUT_VECTOR_LENGTH = rTrees.getVarCount();
		System.out.println("Random trees  loaded:"+imageLocation+" vector length:"+INPUT_VECTOR_LENGTH);
	}


	public void setRTreesOutputFile(String dest) {
		imageOutputDest = dest;		
	}

	public double getClassificationRatio() {
		Scanner inputReader = null;
		Scanner respReader = null;

		try {
			inputReader =  new Scanner(new File(testData));
			respReader = new Scanner(new File(testTarget));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int inputColLength = 1;
		final float[][] matrix = new float[2][];
		matrix[0] = new float[INPUT_VECTOR_LENGTH*MATRIX_ROWS];
		matrix[1] = new float[inputColLength*MATRIX_ROWS];
		int rowsRead = readNextData(matrix, inputReader, respReader, MATRIX_ROWS, INPUT_VECTOR_LENGTH);
		final Pointer inputData = new FloatPointer(matrix[0]);
		final Pointer outputData = new FloatPointer(matrix[1]);
		final Mat inputMatrix = new Mat(rowsRead,INPUT_VECTOR_LENGTH,CV_32F,inputData);
		final Mat outputMatrix = new Mat(rowsRead,1,CV_32F,outputData);

		return calcClassificationRatio(inputMatrix, outputMatrix);
	}

	private String getErrorMessage() {
		return errMessage;
	}
	
	private float[] featureVector;
	public float[] getFeatureVector(){
		return featureVector;
	}
	
	public void startUpdates(int rate,IClassifierCallbacks callbacks) {
		Thread t = new Thread(){
			public void run() {
				System.out.println("Random trees started");
				final IClassifierCallbacks callbackI = callbacks;
				final int delay = 1000/rate;
				int rows = 1;
				run = true;
				Mat input;
				while(run){					
					try {
						sleep(delay);
						featureVector = callbackI.getFeatureVector();
						final Pointer p1 = new FloatPointer(featureVector);
						input = new Mat(rows, INPUT_VECTOR_LENGTH, CV_32F, p1);
						callbacks.setClassificationResult(CLASSIFIER_TYPE.RANDOMTREES,getTargetLabel((int)predict(input,false)));

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}


		};
		t.start();
	}
	
	private double threashold = 0.5;
	public double predict(Mat input,boolean raw) {
		double result = rTrees.predict(input);
		if(raw){
			return result;
		}else{
			if(result - (int)result > threashold){
				result = result +1;
				return (int)result;
			}else{
				return (int)result;
			}
		}
	}
	
	public double predict(float[] inputF,boolean raw){
		int rows = 1;
		final Pointer p1 = new FloatPointer(inputF);
		Mat input = new Mat(rows, inputF.length, CV_32F, p1);
		
		double result = rTrees.predict(input);
		if(raw){
			return result;
		}else{
			if(result - (int)result > threashold){
				result = result +1;
				return (int)result;
			}else{
				return (int)result;
			}
		}
	}

	public String getTargetLabel(int classVal) {
		return TargetModel.getLabel(classVal);
	}


	private int threadCount = 0;
	private boolean run = true;
	private String inputFile;
	private String targetFile;
	private String META_DATA_FILE ="metaData.dat";
	private String errMessage;

	public void initiate(){
		rTrees.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER + TermCriteria.EPS
				, MAX_ITERATIONS, 0.1));
	}
	
	public void initiate(String fName){
		FileStorage fs = new FileStorage(fName, FileStorage.READ);			
		FileNode node = fs.getFirstTopLevelNode();
		rTrees = RTrees.create();
		rTrees.read(node);
		try {
			fs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INPUT_VECTOR_LENGTH = rTrees.getVarCount();
		System.out.println("Random trees  loaded:"+fName+" vector length:"+INPUT_VECTOR_LENGTH);
	}
	
	@Override
	public void train() {
		if(rTrees == null){
			System.out.println("Random trees are not initiated!");
			return;
		}
		Scanner respReader = null;
		Scanner inputReader = null;
		File input = new File(inputFile);
		File resp = new File(targetFile);


		int tarCols = 1;
		float matrix[][] = new float[2][];
		matrix[0] = new float[INPUT_VECTOR_LENGTH*MATRIX_ROWS];
		matrix[1] = new float[tarCols*MATRIX_ROWS];

		try {
			respReader =  new Scanner(resp);
			inputReader = new Scanner(input);

			Mat inMat;
			Mat outMat;
			int rowsRead = readNextData(matrix,inputReader,respReader,MATRIX_ROWS,INPUT_VECTOR_LENGTH);

			inputData = new FloatPointer(matrix[0]);
			outputData = new FloatPointer(matrix[1]);
			inMat= new Mat(rowsRead,INPUT_VECTOR_LENGTH,CV_32F,inputData);
			outMat = new Mat(rowsRead,1,CV_32F,outputData);
			TrainData td = TrainData.create(inMat, opencv_ml.ROW_SAMPLE, outMat);
			rTrees.train(td);				
			rowsRead = readNextData(matrix,inputReader,respReader,rowsRead,INPUT_VECTOR_LENGTH);

			rTrees.save(imageOutputDest);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("RTree trained with input:"+inputFile+" Image outputed at:"+imageOutputDest+
				" Forest Size:"+rTrees.getRoots().limit());

	}

	private float calculateError(float[][] matrix, Mat inputMatrix, int cols) {
		float prediction;
		float error = 0;
		for(int i = 0 ; i < inputMatrix.rows();i+=100){
			prediction = (float) predict(inputMatrix.row(i),false);
			System.out.println("Given:" + prediction+"Given Label: "+getTargetLabel((int)prediction)+" Expected:"+getTargetLabel((int)matrix[1][i]));
			error = error + Math.abs(prediction-matrix[1][i]);
		}
		return error;
	}

	private float calculateMean(float[] fs) {
		float sum = 0;
		for(float v: fs){
			sum =  (sum +v);
		}
		return sum/fs.length;
	}

	private RTrees loadRTrees(LinkedList<RTrees> rTrees) {
		int treesToLoad= 1000;
		File f = null;
		FileStorage fs =  null;
		RTrees tree = null;
		for(int i = 0;i< treesToLoad;i++){
			f = new File("RTrees"+i+".dat");
			if(f.exists()){
				fs = new FileStorage(f.getAbsolutePath(), FileStorage.READ);
				tree = RTrees.create();
				tree.read(fs.getFirstTopLevelNode());
				rTrees.add(tree);
			}

		}
		return tree;
	}

	private void tempFunction(){
		int inputColLength = 1;
		int rows = 10000;
		int cols = 38;
		final float[][] matrix = new float[2][];
		matrix[0] = new float[cols*rows];
		matrix[1] = new float[inputColLength*rows];
		//PoseWriter.generateNextRandomTrainingsetTree(matrix, rows, 180, 90);
		writeToFile(matrix, rows, cols,"Data.arff");
		BufferedReader reader = null;
		BufferedReader reader1 = null;
		try {
			reader = new BufferedReader(
					new FileReader("Data.arff"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Instances train = null;
		Instances test = null;
		try {
			train = new Instances(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// setting class attribute
		train.setClassIndex(train.numAttributes() - 1);
		//PoseWriter.generateNextRandomTrainingsetTree(matrix, rows, 180, 90);
		writeToFile(matrix, rows, cols,"Test.arff");
		try {
			reader1 = new BufferedReader(
					new FileReader("Test.arff"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			test = new Instances(reader1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// setting class attribute
		test.setClassIndex(test.numAttributes() - 1);
		// filter
		Remove rm = new Remove();
		//	rm.setAttributeIndices("1");  // remove 1st attribute
		// classifier
		J48 j48 = new J48();
		j48.setUnpruned(true);        // using an unpruned J48

		// meta-classifier
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(rm);
		fc.setClassifier(j48);
		// train and make predictions
		try {
			fc.buildClassifier(train);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < test.numInstances(); i++) {
			double pred = -1;
			try {
				pred = fc.classifyInstance(test.instance(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.print("ID: " + test.instance(i).value(0));
			System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
			System.out.println(", predicted: " + test.classAttribute().value((int) pred));
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader1.close();
		} catch (IOException e) {
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

	public double calcMSE(Mat input,float[] target){
		double result = 0;
		double temp = 0;
		boolean raw = true;

		for(int i = 0;i < input.rows();i++){
			temp = predict(input.row(i),raw);
			temp = (target[i] -temp);
			result = result + temp*temp;
		}
		result = result/input.rows();
		return result;		
	}

	/**
	 * 
	 * @param input - feature vectors to classify
	 * @param target - array with expected target value
	 * @param target 
	 * @return
	 */
	public double calcClassificationRatio(Mat input,Mat target){
		double result = 0;
		double temp = 0;
		boolean raw = true;
		FloatBuffer fb = null;
		int correctlyClassified =0;
		float[] expected = new float[1];
		for(int i = 0;i < input.rows();i++){
			fb = target.row(i).createBuffer();
			fb.get(expected);
			fb.clear();
			temp = predict(input.row(i),raw);
			if(temp-(int)temp > 0.5){
				temp = (int)temp+1;
			}
			if((int)temp == expected[0]){
				correctlyClassified++;
			}
		}
		result = (double)correctlyClassified/input.rows();
		return result;		
	}

	@Override
	protected void finalize() throws Throwable{
		super.finalize();
		rTrees.deallocate();
	}



	private String getHeader(){
		String entry = "% 1. Title: Iris Plants Database \n"+
				"%\n"+ 
				"% 2. Sources:\n"+
				"%      (a) Creator: R.A. Fisher\n"+
				"%      (b) Donor: Michael Marshall (MARSHALL%PLU@io.arc.nasa.gov)\n"+
				"%      (c) Date: July, 1988\n"+
				"%\n"+
				"@RELATION iris\n"+
				"\n"+	
				"@ATTRIBUTE ef1  NUMERIC\n"+
				"@ATTRIBUTE ef2   NUMERIC\n"+
				"@ATTRIBUTE ef3  NUMERIC\n"+
				"@ATTRIBUTE ef4   NUMERIC\n"+
				"@ATTRIBUTE ef5  NUMERIC\n"+
				"@ATTRIBUTE ef6   NUMERIC\n"+
				"@ATTRIBUTE ef7  NUMERIC\n"+
				"@ATTRIBUTE ef8   NUMERIC\n"+
				"@ATTRIBUTE ef9  NUMERIC\n"+
				"@ATTRIBUTE ef10   NUMERIC\n"+
				"@ATTRIBUTE ef11  NUMERIC\n"+
				"@ATTRIBUTE ef12   NUMERIC\n"+
				"@ATTRIBUTE ef13  NUMERIC\n"+
				"@ATTRIBUTE ef14   NUMERIC\n"+
				"@ATTRIBUTE ef15  NUMERIC\n"+
				"@ATTRIBUTE ef16   NUMERIC\n"+
				"@ATTRIBUTE ef17  NUMERIC\n"+
				"@ATTRIBUTE ef18   NUMERIC\n"+
				"@ATTRIBUTE ef19  NUMERIC\n"+
				"@ATTRIBUTE ef20   NUMERIC\n"+
				"@ATTRIBUTE ef21  NUMERIC\n"+
				"@ATTRIBUTE ef22   NUMERIC\n"+
				"@ATTRIBUTE ef23  NUMERIC\n"+
				"@ATTRIBUTE ef24   NUMERIC\n"+
				"@ATTRIBUTE ef25  NUMERIC\n"+
				"@ATTRIBUTE ef26   NUMERIC\n"+
				"@ATTRIBUTE ef27  NUMERIC\n"+
				"@ATTRIBUTE ef28   NUMERIC\n"+
				"@ATTRIBUTE ef29  NUMERIC\n"+
				"@ATTRIBUTE ef30   NUMERIC\n"+
				"@ATTRIBUTE ef31  NUMERIC\n"+
				"@ATTRIBUTE ef32   NUMERIC\n"+
				"@ATTRIBUTE ef33  NUMERIC\n"+
				"@ATTRIBUTE ef34   NUMERIC\n"+
				"@ATTRIBUTE ef35  NUMERIC\n"+
				"@ATTRIBUTE ef36   NUMERIC\n"+
				"@ATTRIBUTE ef37  NUMERIC\n"+
				"@ATTRIBUTE ef38   NUMERIC\n"+
				"@ATTRIBUTE class     {1.0,2.0,3.0,4.0,5.0}\n"+
				"\n"+
				"@DATA\n";
		return entry;
	}

	private void writeToFile(float[][] matrix,int rows,int columns, String string){
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(string));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw.write(getHeader());
			for(int r = 0; r < rows;r+=38){
				for(int col = 0; col<columns;col++){
					fw.write(matrix[0][r+col]+",");
				}
				fw.write("\""+matrix[1][r/38]+"\"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop(IClassifierCallbacks ea) {
		run = false;		
	}



	

	private int readNextData(float[][] matrix,Scanner inputScanner, Scanner respReader,  int rows,int cols) {
		int rowCount = 0;
		while(inputScanner.hasNextLine()&& respReader.hasNextLine() && rowCount < rows){
			String[] values = inputScanner.nextLine().split(",");
			for(int i = 0; i < values.length;i++){
				matrix[0][cols* rowCount+i] = Integer.decode(values[i]);
			}
			matrix[1][rowCount] = Integer.decode(respReader.nextLine());
			rowCount++;
		}
		return rowCount;		
	}

	@Override
	public void save(String fileName) {
		rTrees.save(fileName);
		System.out.println("treeSaved at:"+fileName);
	}

	public void setTrainingDataInputFile(String fName) {
		inputFile = fName;

	}

	public void setTrainingDataTargetFile(String fName) {
		targetFile = fName;		
	}

	public boolean testDataFiles(String inputFile,String targetFile,int desiredVectorLength){
		Scanner scInputFile = null;
		Scanner scTargetFile = null;
		try {
			scInputFile = new Scanner(new File(inputFile));
			scTargetFile = new Scanner(new File(targetFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if(scInputFile.hasNextLine() && scTargetFile.hasNextLine()){
			String[] values = scInputFile.nextLine().split(",");
			scTargetFile.nextLine();
			if( desiredVectorLength != values.length){
				errMessage = "Incorrect length of value";
				scInputFile.close();
				scTargetFile.close();
				return false;
			}
		}
		while(scInputFile.hasNextLine() && scTargetFile.hasNextLine()){
			scInputFile.nextLine();
			scTargetFile.nextLine();
		}
		if(scInputFile.hasNextLine() || scTargetFile.hasNextLine()){
			scInputFile.close();
			scTargetFile.close();
			errMessage = "Incorrect number of rows";
			return false;
		}else{
			scInputFile.close();
			scTargetFile.close();
			return true;
		}
	}

	public void setImageLocation(String imgLoc) {
		imageLocation = imgLoc;
	}

	@Override
	public void setImageOutDest(String string) {
		imageOutputDest = string;		
	}

	@Override
	public void setInputVectorLength(int inSize) {
		INPUT_VECTOR_LENGTH = inSize;		
	}


	public void setTestDataInput(String testDataA) {
		this.testData = testDataA; 
	}

	public void setTestDataTarget(String testRTreesTar) {
		this.testTarget = testRTreesTar;		
	}
}