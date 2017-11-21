package com.app.ml.nn.mynn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.bytedeco.javacpp.avformat.AVOutputFormat.Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer;

import com.app.gui.GUIANN;
import com.app.ml.nn.mynn.ConstantsEnums.ActivationFuctionType;
import com.app.ml.nn.mynn.ConstantsEnums.NeuronType;

public class MyANN {
	private NeuronLayer[] layers;

	/**
	 * 
	 * @param layers - number of hidden layers
	 * @param layerSize - size of a hidden layer
	 * @param inputSize - number of features
	 * @param outputSize - number of output neurons
	 */
	public MyANN(int hiddenLayers,int layerSize, int inputSize, int outputSize){
		/* Creating hiddenLayers + 2, where 2 are in the input and output layers */
		this.layers = new NeuronLayer[hiddenLayers+2];
		/* Initiating the input layer */
		this.layers[0] = new NeuronLayer(NeuronType.INPUT, ActivationFuctionType.GAUSIAN,  inputSize);
		/* Initiating output layer */
		this.layers[layers.length-1] = new NeuronLayer(NeuronType.OUTPUT, ActivationFuctionType.GAUSIAN,  outputSize);
		/* Initiating hidden layers */
		for(int i = 1; i < layers.length-1;i++){
			this.layers[i] = new NeuronLayer(NeuronType.HIDDEN, ActivationFuctionType.GAUSIAN,  layerSize);
			if(i > 0){
				/* Initiating weights, weight array size is set and each weight initiated to 1 */
				layers[i-1].setUpperLayer(this.layers[i]);
			}
		}
		/* Initiating weights, weight array size is set and each weight initiated to 1 */
		layers[layers.length -2].setUpperLayer(layers[layers.length -1]); 
	}

	public  void printAnn() {
		for(int l = 0; l < layers.length;l++){
			System.out.println("Layer:"+ (l+1)+" Weight array size:"+layers[l].getWeights().length);
			layers[l].printWeights();
		}
	}

	public void train(File in,File target,int iterations){
		if(test(in,target)){
			System.out.println("Incorrect number or rows");
			System.exit(-1);
		}
		Scanner input = null;
		Scanner tar = null;
		try {
			input = new Scanner(in);
			tar = new Scanner(target);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s ;
		String result;
		while(input.hasNextLine()){
			s = input.nextLine();
			result = tar.nextLine();
			runNetwork(getFloats(s.split(",")),getFloats(result.split(",")));
		}
	}

	private float[] getFloats(String[] split) {
		float[] result = new float[split.length];
		for(int i = 0;i < split.length;i++){
			result[i] = Float.parseFloat(split[i]);
		}
		return result;
	}

	private void runNetwork(float[] input, float[] output) {
		layers[0].setNetInput(input);
		layers[0].printInput();
		for(int i = 0;i < layers.length; i++){
			layers[i].execute();
			if(i<layers.length-1){
				layers[i].propagate(layers[i+1]);
			}
		}
		layers[layers.length-1].printOutput();
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
	}

	private boolean test(File in, File target) {
		Scanner input = null;
		Scanner tar = null;
		try {
			input = new Scanner(in);
			tar = new Scanner(target);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(input.hasNextLine()){
			if(!tar.hasNextLine()){
				return false;
			}
			input.nextLine();
			tar.nextLine();
		}
		if(!tar.hasNextLine()){
			return false;
		}		
		return true;
	}

	public static void main(String[] args){
		int hiddenLayers = 2;
		int layerSize = 5;
		int inputSize = 38;
		int outputSize = 5;
		MyANN ann = new MyANN(hiddenLayers,layerSize,inputSize,outputSize);
		File in = new File("iAnglesSetA.csv");
		File target = new File("tNNVectorSetA.csv");
		GUIANN ui = new GUIANN();
		ui.setAnn(ann);
		ann.train(in, target,1);
	}

	public int layerCount() {		
		return layers.length;
	}

	/**
	 * Returns size of particular layer, layer is index in the array.
	 *  Starts with 0
	 * @param layer
	 * @return
	 */
	public int getLayerSize(int layer) {		
		return layers[layer].getSize();
	}

	public float getWeight(int layer, int i) {
		if(layers[layer].getWeights().length > i){
			return layers[layer].getWeights()[i];
		}
		else return 1;
	}

	public float getNormalizedWeight(int layer, int i) {
		if(layers[layer].isOutputLayer()){
			return 0;
		}
		if(getMaximalWeight(layer) == 0){
			return 0;
		}
		return getWeight(layer, i)/getMaximalWeight(layer);
	}

	private float getMaximalWeight(int layer) {
		float large = 0;
		for(float weight:layers[layer].getWeights()){
			if(Math.abs(large)<Math.abs(weight)){
				large = weight;
			}
		}
		return large;
	}
}
