package app.classifier_wrappers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_ml;
import org.bytedeco.javacpp.opencv_ml.RTrees;

import static org.bytedeco.javacpp.opencv_core.*;


public class K_NNClassifierWrapper {
	//static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	private static Mat samples;
	static float tResponse[];
	public static void main(String[] args) {
		//System.out.println("Welcome to OpenCV " + Core.VERSION);



		Scanner lineScanner = null;
		File input = new File("C:\\Users\\Macro_EL\\Desktop\\Apps\\EclipseVersions\\Luna\\eclipse\\dropins\\input.csv");
		File resp = new File("C:\\Users\\Macro_EL\\Desktop\\Apps\\EclipseVersions\\Luna\\eclipse\\dropins\\target.csv");

		int rows = 0;
		int cols = 0;
		Map<String,Integer> dims = getDimensions(input);
		rows = dims.get("rows");
		cols = dims.get("columns");
		FloatBuffer sampleBuffer ;
		System.out.println("Cols"+cols+" Rows"+rows);
		sampleBuffer =  FloatBuffer.allocate(rows*cols);
		int counter = 0;
		try {
			final RTrees rTrees = RTrees.create();
			lineScanner =  new Scanner(input);
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
			Pointer p = new FloatPointer(inputMat);
			samples = new Mat(rows, cols, CV_32F, p);

			dims = getDimensions(resp);
			rows = dims.get("rows");
			cols = dims.get("columns");
			System.out.println("Cols"+cols+" Rows"+rows);
			FloatBuffer buffResponse = FloatBuffer.allocate(rows);
			counter = 0;

			lineScanner =  new Scanner(resp);
			while(lineScanner.hasNextLine()){
				String nextLine = lineScanner.nextLine();
				String[] split = nextLine.split(",");
				float[] arr = new float[split.length];
				for(String val:split){
					arr[counter++] = new Integer(val);
				}
				float[] data = new float[]{arr[0]+arr[1]*2+arr[2]*3};
				counter=0;
				buffResponse.put(data);
			}
			lineScanner.close();

			
			Pointer pResp = new FloatPointer(buffResponse.array());
			Mat responses = new Mat(rows, 1, CV_32F, pResp);

			//	System.out.println("About to print"+samples.dump());

			//	System.out.println("OpenCV Mat data:\n" + samples.dump());
			//((StatModel)rTrees)
			//System.out.println("OpenCV Mat data:\n" + responses.dump());

			rTrees.train(samples,opencv_ml.ROW_SAMPLE, responses);	

			//Mat test = new Mat(4,samples.cols(),CV_32F, new Scalar(0));
			
		

			//			float[] dataF = new float[samples.cols()];
			/*samples.get(0, 0, dataF);
		test.put(0, 0, dataF);
		samples.get(3000, 0, dataF);
		test.put(1, 0, dataF);
		samples.get(3200, 0, dataF);
		test.put(2, 0, dataF);
		samples.get(6000, 0, dataF);
		test.put(3, 0, dataF);*/
			p = new FloatPointer(inputMat);
			Mat tSamples = new Mat(rows, cols, CV_32F, p);
			tResponse = new float[samples.rows()];
			Pointer x = new FloatPointer(tResponse);
			final Mat testResult = new Mat(samples.rows(),1,CV_32F, x);
			MatAllocator mAloc = testResult.allocator();
			
			System.out.println(inputMat);
			System.out.println(rTrees.predict(samples.row(3000)));
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
		return demesnions;
	}
}