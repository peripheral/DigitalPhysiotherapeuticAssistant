package com.app.classifiers.wrappers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_ml;
import org.bytedeco.javacpp.opencv_ml.ANN_MLP;
import org.bytedeco.javacpp.opencv_ml.TrainData;

import static org.bytedeco.javacpp.opencv_core.*;

import com.app.classifiers.IClassifierCallbacks;
import com.app.classifiers.TargetModel;


public class AnnClassifierWrapper implements IClassifierWrapper {
	private static final double EPSILON = 0.01;
	private final int ITERATIONS = 1000;
	private final int MATRIX_ROWS = 1000000;	
	private boolean run = true;
	private int refresh_rate = 0;
	private IClassifierCallbacks callback = null;
	public static enum CLASSIFIER_TYPE{ANN,KNN,RANDOMTREES};
	private int INPUT_VECTOR_SIZE = 19;
	private int OUTPUT_VECTOR_SIZE = 5;
	private  ANN_MLP ann = ANN_MLP.create();
	private  Mat targetData;
	private  Mat inputData;
	private TrainData td;
	private float[] featureVector;

	private final int OUTPUT_COLS = 5;
	private String inputFile;
	private String targetFile;
	private String imageLocation="images/annTrained.ml";
	
	public static void main(String[] args) {
		AnnClassifierWrapper annWrap = new AnnClassifierWrapper();
		annWrap.initiateFromFile();
		float[] featureVector = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Pointer p1 = new FloatPointer(featureVector);
		int rows = 1;
		Mat input = new Mat(rows, 19, CV_32F, p1);
		Mat result = new Mat(rows,5,CV_32F);
		annWrap.ann.predict(input,result,ANN_MLP.RAW_OUTPUT);
		System.out.println(annWrap.getLabel(result));
	}

	public void setInputLength(int in) {
		INPUT_VECTOR_SIZE = in;
	}

	private float calcClassificationRate() {	
		if(!ann.isTrained() && ann.isNull()){
			System.out.println("No Neural network initiated to calculate Classification Rate.");
		}
		Scanner inputSc = null;
		Scanner respSc = null;
		try {
			inputSc = new Scanner(new File(testInput));
			respSc = new Scanner(new File(testTarget));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		float[][] matrix = new float[2][];
		matrix[0] = new float[MATRIX_ROWS*INPUT_VECTOR_SIZE];
		matrix[1] = new float[MATRIX_ROWS*OUTPUT_VECTOR_SIZE];
		int rowsRead = readNextRows(matrix, inputSc, respSc, MATRIX_ROWS, INPUT_VECTOR_SIZE);	
		Pointer input = new FloatPointer(matrix[0]);
		Pointer resp = new FloatPointer(matrix[1]);
		Mat inputMat = new Mat(MATRIX_ROWS, INPUT_VECTOR_SIZE,CV_32F, input);
		Mat tarMat = new Mat(MATRIX_ROWS, OUTPUT_VECTOR_SIZE,CV_32F, resp);
		Mat result = new Mat();
		int correctResults = 0;
		int total = 0;
		for(int i = 0; i<rowsRead;i++){
			ann.predict(inputMat.row(i), result,ANN_MLP.RAW_OUTPUT);
			if(compareResult(result,tarMat.row(i))){
				correctResults++;
			}
			total++;

		}
		
		try {
			tarMat.close();
			inputMat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		float classificationRatio =(float)correctResults/total;
		return classificationRatio;
	}

	private void printMatrises(Mat result, Mat expTest) {
		FloatBuffer fb = result.createBuffer();
		float[] resultArr = new float[fb.capacity()];
		fb.get(resultArr);
		fb = expTest.createBuffer();
		float[] expected = new float[fb.capacity()];
		fb.get(expected);
		printArrs(expected,resultArr);
	}

	private boolean compareResult(Mat result, Mat tarMat) {
		int neuronResultId = getMostActiveNeuronId(result);
		int neuronExpectedId = getMostActiveNeuronId(tarMat);
		if(neuronExpectedId == neuronResultId){
			return true;
		}else{
			return false;
		}		
	}

	private int getMostActiveNeuronId(Mat result) {
		final int rows = result.rows();
		final int cols = result.cols();
		FloatBuffer fb = result.createBuffer();
		float[] arr = new float[cols];
		fb.get(arr);
		int index = -1;
		float max = -2;
		for(int i = 0;i < cols*rows;i++){
			if(max < arr[i]){
				max =  arr[i];
				index = i;
			}
		}
		return index;
	}

	private void printArrs(float[] expected,float[] resultArr){
		System.out.print("Predicted: ");
		for(int i = 0; i < expected.length;i++){
			System.out.print(" "+resultArr[i]);
		}
		System.out.print(" Expected: ");
		for(int i = 0; i < expected.length;i++){
			System.out.print(" "+expected[i]);
		}

		System.out.println();
	}

	public boolean start(){
		if(refresh_rate > 0 && callback != null){
			startUpdates(refresh_rate, callback);
			return true;
		}else{
			System.out.println("start Failed test 'refresh_rate > 0 && callback != null'");
			return false;
		}		
	}
	
	@Override
	public void stop(IClassifierCallbacks ea) {
		run = false;
	}

	public void startUpdates(int rate, IClassifierCallbacks callback) {
		Thread t = new Thread(){

			public void run() {
				System.out.println("Ann started from:"+imageLocation);
				final IClassifierCallbacks callbackI = callback;
				final int delay = 1000/rate;
				int rows = 1;
				run = true;
				Pointer p1;
				Mat input;
				Mat result;
				while(run){					
					try {
						sleep(delay);
						featureVector = callbackI.getFeatureVector();
						p1 = new FloatPointer(featureVector);
						input = new Mat(rows, INPUT_VECTOR_SIZE, CV_32F, p1);
						result = new Mat(rows,5,CV_32F);
						ann.predict(input,result,ANN_MLP.RAW_OUTPUT);
						callback.setClassificationResult(CLASSIFIER_TYPE.ANN,getLabel(result));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Ann stopped:"+!run);
			};		
		};
		t.start();		
	}

	public AnnClassifierWrapper(){

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


	
	private void printLayers(Mat layerSizes) {
		BytePointer bp = layerSizes.arrayData();
		System.out.println(layerSizes.type()+" "+ CV_32S);
		byte[] bArr = new byte[layerSizes.rows()*layerSizes.cols()*4];
		bp.get(bArr);
		ByteBuffer wrap = ByteBuffer.wrap(bArr);
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		System.out.print("Layers: ");		
		for(int i = 0;i < layerSizes.cols()*layerSizes.rows();i++){
			System.out.print(" " +wrap.getInt());		
		}
		System.out.println();
	}

	private void printWeights(Mat weightsL0, Mat weightsL1, Mat weightsL2, Mat weightsL3) {
		BytePointer bp = weightsL0.data();
		byte[] bArr = new byte[weightsL0.rows()*weightsL0.cols()*8];
		bp.get(bArr);
		ByteBuffer wrap = ByteBuffer.wrap(bArr);
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		System.out.print("Layer 1:");
		for(int i = 0;i < weightsL0.cols()*weightsL0.rows();i++){
			System.out.print(" " +wrap.getDouble());		
		}
		System.out.println();
		bp = weightsL1.data();
		bArr = new byte[weightsL1.rows()*weightsL1.cols()*8];
		bp.get(bArr);
		wrap = ByteBuffer.wrap(bArr);
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		System.out.print("Layer 2:");
		for(int i = 0;i < weightsL1.cols()*weightsL1.rows();i++){
			System.out.print(" " +wrap.getDouble());		
		}
		System.out.println();
		bp = weightsL2.data();
		bArr = new byte[weightsL2.rows()*weightsL2.cols()*8];
		bp.get(bArr);
		wrap = ByteBuffer.wrap(bArr);
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		System.out.print("Layer 3:");
		for(int i = 0;i < weightsL2.cols()*weightsL2.rows();i++){
			System.out.print(" " +wrap.getDouble());		
		}
		System.out.println();
		bp = weightsL3.data();
		bArr = new byte[weightsL3.cols()*weightsL3.rows()*8];
		bp.get(bArr);
		wrap = ByteBuffer.wrap(bArr);
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		System.out.print("Layer 4 Output layer:");
		for(int i = 0;i < weightsL3.cols()*weightsL3.rows();i++){
			System.out.print(" " +wrap.getDouble());		
		}
		System.out.println();
	}

	private float[] printResult(Mat mat, Mat expMat,float[] expArr) {
		System.out.print("Printing result: ");
		FloatBuffer fb = expMat.createBuffer();
		float[] expected = new float[fb.capacity()];
		fb.get(expected);
		fb = mat.createBuffer();
		float[] fArr = new float[fb.capacity()];
		fb.get(fArr);
		//wrap.order(ByteOrder.BIG_ENDIAN);
		//wrap.order(ByteOrder.LITTLE_ENDIAN);
		DecimalFormat df = new DecimalFormat("#.###");
		for(int i = 0;i < fb.capacity();i++){
			System.out.print(" " + df.format(fArr[i]));		
		}
		System.out.println();
		System.out.print("Expected Result:");
		for(float f:expected){
			System.out.print(" " +f);	
		}
		System.out.println();
		System.out.print("Expected Result:");
		for(float f:expArr){
			System.out.print(" " +f);	
		}
		System.out.println();		
		return fArr;
	}

	private float[] readNext(Scanner lineScanner,int defaultSize,int cols){
		FloatBuffer sampleBuffer =  FloatBuffer.allocate(defaultSize*cols);
		int counter = 0;
		int rowCounter = 0;
		while(lineScanner.hasNextLine() && rowCounter < defaultSize){
			String nextLine = lineScanner.nextLine();
			String[] split = nextLine.split(",");
			float[] data = new float[cols];

			for(String val:split){
				data[counter++] = (new Float(val)).floatValue();
			}
			counter=0;
			sampleBuffer.put(data);
			rowCounter++;
		}
		return sampleBuffer.array(); 
	}

	private String getLabel(Mat result) {
		final int rows = result.rows();
		final int cols = result.cols();
		FloatBuffer fb = result.createBuffer();
		float[] arr = new float[cols];
		fb.get(arr);
		int index = 0;
		float max = -2;
		for(int i = 0;i < cols*rows;i++){
			if(max < arr[i]){
				max =  arr[i];
				index = i;
			}
		}
		index++;
		return TargetModel.getLabel(index);
	}

	private float calcMean(float[] fs) {
		float sum = 0;
		for(int i = 0;i < fs.length;i+=5){
			for(int j = 0;j<5;j++){
				if(fs[i+j]!= 0 ){
					sum = sum + j+1;
					break;
				}
				if( j ==4){
					sum = sum+j+1;
				}
			}
		}
		return sum/(fs.length/5);
	}

	public void initiate() {
		double paramB = 1;
		double paramA = 1;
		int[] topology = new int[]{INPUT_VECTOR_SIZE,39,39,20,OUTPUT_VECTOR_SIZE};
		try{
			Pointer top = new IntPointer(topology);
			Mat topMat = new Mat(1,topology.length,CV_32S,top);
			ann.setLayerSizes(topMat);
			ann.setTrainMethod(ANN_MLP.BACKPROP);
			ann.setTermCriteria(new TermCriteria(TermCriteria.COUNT|TermCriteria.EPS,ITERATIONS,EPSILON));
			ann.setActivationFunction(ANN_MLP.SIGMOID_SYM,paramA,paramB);
		}catch(Exception e){
			System.err.println("Failed to create ANN");
			e.printStackTrace();
		}
	}
	
	public void initiate(String fileName){
		File image = new File(fileName);
		if(!image.exists()){
			throw new RuntimeException("Ann image file:"+image.getAbsolutePath()+" doesn't exists.");
		}
		FileStorage fs = new FileStorage(fileName, FileStorage.READ);			
		FileNode node = fs.getFirstTopLevelNode();
		ann = ANN_MLP.create();
		ann.read(node);
		try {
			fs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Mat layerSizes = ann.getLayerSizes();
		IntBuffer ib = layerSizes.createBuffer();
		int sizes[] = new int[ib.limit()];
		ib.get(sizes);
		INPUT_VECTOR_SIZE = sizes[0];
		imageLocation = fileName;
		System.out.println("Ann loaded:"+imageLocation);
	}

	public void initiateFromFile(){
		File image = new File(imageLocation);
		if(!image.exists()){
			throw new RuntimeException("Ann image file:"+image.getAbsolutePath()+" doesn't exists.");
		}
		FileStorage fs = new FileStorage(imageLocation, FileStorage.READ);			
		FileNode node = fs.getFirstTopLevelNode();
		ann.read(node);
		try {
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void train() {
		float[][] matrix = new float[2][];
		matrix[0] = new float[MATRIX_ROWS*INPUT_VECTOR_SIZE];
		matrix[1] = new float[MATRIX_ROWS*OUTPUT_COLS];
		Scanner inputScanner = null;
		Scanner targetScanner = null;
		try {
			inputScanner = new Scanner(new File(inputFile));
			targetScanner = new Scanner(new File(targetFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		int rowsRead = readNextRows(matrix,inputScanner,targetScanner,MATRIX_ROWS,INPUT_VECTOR_SIZE);
		ann.setTermCriteria(new TermCriteria(TermCriteria.COUNT|TermCriteria.EPS,ITERATIONS,EPSILON));
		while(rowsRead >0){
			Pointer pInput = new FloatPointer(matrix[0]);
			Pointer pResp = new FloatPointer( matrix[1]);
			targetData = new Mat(rowsRead, OUTPUT_COLS, CV_32F,pResp);
			inputData = new Mat(rowsRead, INPUT_VECTOR_SIZE, CV_32F, pInput);
			td = TrainData.create(inputData,opencv_ml.ROW_SAMPLE, targetData);
			if(ann.isTrained()){
				System.out.println("ANN is being updated.");
				ann.train(td,ANN_MLP.UPDATE_WEIGHTS+ANN_MLP.UPDATE_MODEL);
			}else{
				ann.train(td);
			}

			rowsRead = readNextRows(matrix,inputScanner,targetScanner,MATRIX_ROWS,INPUT_VECTOR_SIZE);
		}

		ann.save(imageLocation);
		System.out.println("Ann training complete, input set:"+inputFile+" image exported to:"
				+ imageLocation);
	}
	
	@Override
	public void save(String fileName) {
		ann.save(fileName);		
	}

	private int readNextRows(float[][] matrix,Scanner inputScanner,Scanner respReader, int rows, int cols) {
		int rowCount = 0;
		int tarCols = 5;
		float[] temp = new float[cols];
		while(inputScanner.hasNextLine()&& respReader.hasNextLine() && rowCount < rows){
			String[] values = inputScanner.nextLine().split(",");
			for(int i = 0; i < temp.length;i++){
				temp[i] = Integer.decode(values[i]);
			}
			//java.util.Arrays.sort(temp);
			for(int i = 0; i < values.length;i++){
				matrix[0][cols* rowCount+i] = temp[i];
			}
			values = respReader.nextLine().split(",");
			for(int i = 0; i < values.length; i++){
				matrix[1][tarCols*rowCount+i] = Integer.decode(values[i]);
			}

			rowCount++;
		}
		return rowCount;	
	}

	public void setTrainingDataInputFile(String text) {
		inputFile = text;
	}

	public void setTrainingDataTargetFile(String text) {
		targetFile = text;		
	}

	public double MSE(Mat input,Mat resp){
		Mat result = new Mat();
		double err = 0;
		for(int i = 0; i < input.rows();i++){
			ann.predict(input.row(i), result, ANN_MLP.RAW_OUTPUT);
			err+= (long) calcErr(result,resp.row(i));
		}
		return err/input.rows();
	}

	private double calcErr(Mat result, Mat expected) {
		FloatBuffer fb = result.createBuffer();
		float[] resultA = new float[fb.capacity()];
		fb.get(resultA);
		fb = expected.createBuffer();
		float[] expectedA = new float[fb.capacity()];
		fb.get(expectedA);
		double sum = 0;
		for(int i = 0; i < resultA.length;i++){
			sum += Math.pow(resultA[i] - expectedA[i],2);
		}
		return sum;
	}

	public float[] getFeatureVector() {
		return featureVector;		
	}

	public void setImageOutDest(String fName) {
		imageLocation = fName;		
	}

	@Override
	public void setInputVectorLength(int inSize) {
		INPUT_VECTOR_SIZE = inSize;		
	}

	private double ratio =0;
	private String testInput = "";
	private String testTarget;
	public double getClassificationRatio() {
		ratio = calcClassificationRate();
		return ratio;
	}

	public void setTestDataInput(String testInput) {
		this.testInput = testInput;		
	}

	public void setTestDataTarget(String testAnnTar) {
		testTarget = testAnnTar;		
	}
}