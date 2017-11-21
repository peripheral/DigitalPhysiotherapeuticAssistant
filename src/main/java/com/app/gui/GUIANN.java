package com.app.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.app.ml.nn.mynn.MyANN;

public class GUIANN extends JPanel implements MouseWheelListener,MouseMotionListener,MouseListener{
	private MyANN ann = null;
	private double zoom = 1;
	private int startX = 100,startY = 20;	
	private int neuronHeight = 5,neuronWidth = 5;
	private int distanceBetweenLayers = 16;
	private int weightCellWidth = 2,weightCellHeight= 6,weightHeight = 5,weightWidth =1;
	private Color neuronColor = Color.BLUE;
	private Color weightColor = Color.MAGENTA;
	private int maximumSize;

	public GUIANN() {	
		
		JFrame panel = new JFrame();
		panel.add(this);
		panel.pack();
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setSize(600,400);
		panel.setVisible(true);
		panel.addMouseWheelListener(this);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
	}

	public void paint(Graphics g){
		super.paint(g);	
		if(ann == null){
			return;
		}
		maximumSize = getMaximuSize(ann);
		int layerSize = 5;
		int neuronStart = 0;
		int previousLayerSize = 0;
		int size = 0;
		int center = startX,localStartY=startY,weightStart= 0 ;
		/* Finding starting point for neurons */
		center = (int) (startX+(maximumSize*maximumSize*(weightCellWidth)*zoom)/2-layerSize*weightCellWidth*zoom/2);
		int layerCount = ann.layerCount();

		boolean layerIsInput = false;

		for(int layer = 0; layer < layerCount ; layer++){
			layerSize = ann.getLayerSize(layer);

			/* Drawing neurons */
			neuronStart = (int) (center - layerSize*neuronHeight*zoom/2);
			for(int i = 0; i < layerSize; i++){
				g.setColor(Color.BLACK);				
				g.setColor(neuronColor);
				/*Drawing neuron as oval */
				g.fillOval(neuronStart+i*(int)(neuronHeight*zoom), localStartY, (int)((neuronHeight)*zoom),(int) ((neuronHeight)*zoom));
			}
			/* Drawing edges downwards */
			if(layer < layerCount-1){
				weightStart = (int) (center	-(ann.getLayerSize(layer) > ann.getLayerSize(layer+1)
						?ann.getLayerSize(layer)*ann.getLayerSize(layer):ann.getLayerSize(layer+1)*ann.getLayerSize(layer+1))
						*weightCellWidth*zoom/2);
				for(int i = 0; i < layerSize*layerSize; i++){
					g.setColor(Color.BLACK);
					g.drawLine(neuronStart+(int)(neuronHeight/2 *zoom)+(i%layerSize)*(int)(neuronWidth *zoom), /* X1 */
							localStartY+(int)(neuronHeight *zoom), /* Y1 */
							(int)(weightStart+i*weightCellWidth *zoom), /* X2 */
							(int)( localStartY + (distanceBetweenLayers*zoom)));/* Y2 */
				}
			}

			localStartY = (int) (localStartY + (distanceBetweenLayers*zoom));
			if(layer < layerCount-1){
				weightStart = (int) (center	-(ann.getLayerSize(layer) > ann.getLayerSize(layer+1)
						?ann.getLayerSize(layer)*ann.getLayerSize(layer):ann.getLayerSize(layer+1)*ann.getLayerSize(layer+1))
						*weightCellWidth*zoom/2);
				layerSize = ann.getLayerSize(layer);
				for(int i = 0; i < layerSize*layerSize; i++){
					g.setColor(weightColor);
					/* Weights */
					g.fillRect((int)(weightStart+i*weightCellWidth*zoom), localStartY 
							, weightWidth,(int) ((ann.getNormalizedWeight(layer,i)== 0 ? 0:neuronHeight*zoom/2*ann.getNormalizedWeight(layer,i) )));
					System.out.println("Layer: "+(layer+1)+" Norm Weight:"+ann.getNormalizedWeight(layer,i)+" Result:"+(ann.getNormalizedWeight(layer,i)== 0 ? 0:(neuronHeight*zoom/2*ann.getNormalizedWeight(layer,i) )));

				}
			}
			if(layer >0){
				if(previousLayerSize> layerSize){
					size = previousLayerSize;
				}else{
					size = layerSize;
				}
				weightStart = (int) (center	-size*size*weightCellWidth*zoom/2);
				for(int i = 0; i < size*size; i++){
					g.setColor(Color.BLACK);

					/* Edges upwards */
					g.drawLine((int)(neuronStart+neuronWidth/2*zoom+(i%layerSize)*neuronWidth*zoom),
							(int)( localStartY - (distanceBetweenLayers*zoom)),
							(int)(weightStart+i*weightCellWidth*zoom),
							(int)(localStartY -(distanceBetweenLayers*2-weightCellHeight)*zoom));

				}
			}
			previousLayerSize = layerSize;
			localStartY = (int) (localStartY + (distanceBetweenLayers*zoom));
			Scanner sc = new Scanner(System.in);
			//sc.nextLine();
			}		
		}

		private int getMaximuSize(MyANN nn) {
			int max = 0,temp;
			for(int i = 0; i < nn.layerCount();i++){
				temp = nn.getLayerSize(i);
				if(max < temp){
					max = temp;
				}
			}
			return max;
		}

		public MyANN getAnn() {
			return ann;
		}

		public void setAnn(MyANN ann) {
			this.ann = ann;
		}

		public static void main(String[] args){
			MyANN ann = new MyANN(3,5, 38, 5);
			ann.printAnn();
			GUIANN ui =new GUIANN();
			ui.setAnn(ann);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			double rotations = e.getPreciseWheelRotation();
			rotations = (int)rotations;
			while(rotations <0){
				rotations ++;
				zoom+=0.1;
			}
			while(rotations > 0){
				rotations --;
				zoom-=0.1;
			}
			this.repaint();
		}


		private int currentX = 0,currentY=0;

		@Override
		public void mouseDragged(MouseEvent e) {
			startX+= e.getX() - currentX;	
			startY+= e.getY() - currentY;
			currentX = e.getX();
			currentY = e.getY();
			this.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			currentX = e.getX();
			currentY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}
