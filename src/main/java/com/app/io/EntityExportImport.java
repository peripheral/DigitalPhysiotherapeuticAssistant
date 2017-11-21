package com.app.io;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.gui.list_item.ListItem;

public class EntityExportImport implements IEntityExportImport{

	public void export(Posture posture,String fName){	
		FileWriter out = null;
		try {
			out = new FileWriter(new File(fName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Map<String,double[]> jointMap = posture.getJointMap();
		Set<String> keys = jointMap.keySet();
		double[] coords;
		try {
			for(String key:keys){
				coords = jointMap.get(key);
				out.write(coords[0]+" ");
				out.write(coords[1]+" ");
				out.write(coords[2]+" ");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Posture importFromFile(String file){	
		Posture p = new Posture();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			double coords[] = new double[3];
			Map<String,double[]> jointMap = p.getJointMap();
			Set<String> keys = jointMap.keySet();
			for(String key:keys){
				coords[0] = new Double(scanner.next());
				coords[1] = new Double(scanner.next());
				coords[2] = new Double(scanner.next());
				p.setJoinLocation(key, coords);
			}
		} catch (NoSuchElementException e) {
			System.err.println("Incorect file format");
		}
		return p;
	}

	public Posture importFromFile(File file){	
		Posture p = new Posture();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			double coords[] = new double[3];
			Map<String,double[]> jointMap = p.getJointMap();
			Set<String> keys = jointMap.keySet();
			for(String key:keys){
				coords[0] = new Double(scanner.next());
				coords[1] = new Double(scanner.next());
				coords[2] = new Double(scanner.next());
				p.setJoinLocation(key, coords);
			}
			return p;
		} catch (NoSuchElementException e) {
			System.err.println("Incorect file format");
			scanner.close();
			return null;
		}
	}


	private void export(Posture pose, File file) {
		FileWriter out = null;
		try {
			out = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Map<String,double[]> jointMap = pose.getJointMap();
		Set<String> keys = jointMap.keySet();
		double[] coords;
		try {
			for(String key:keys){
				coords = jointMap.get(key);
				out.write(coords[0]+" ");
				out.write(coords[1]+" ");
				out.write(coords[2]+" ");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void saveToXML(Exercise e,String xml) {
		Document dom;
		Element exercise = null;
		Element posture = null;
		Element attribute = null;

		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			dom = db.newDocument();

			// create the root element
			Element rootEle = dom.createElement("document");

			// create data elements and place them under root
			exercise = dom.createElement(Exercise.class.getSimpleName());
			attribute = dom.createElement("latestProgress");
			attribute.appendChild(dom.createTextNode(e.getLatestProgress()+""));
			exercise.appendChild(attribute);
			attribute = dom.createElement("highestProgress");
			attribute.appendChild(dom.createTextNode(e.getLatestProgress()+""));
			exercise.appendChild(attribute);
			attribute = dom.createElement("name");
			attribute.appendChild(dom.createTextNode(e.getName()));
			exercise.appendChild(attribute);
			for(Posture p : e.getPostures()){
				posture = dom.createElement(Posture.class.getSimpleName());
				attribute = dom.createElement("latestProgress");
				attribute.appendChild(dom.createTextNode(e.getLatestProgress()+""));
				posture.appendChild(attribute);
				attribute = dom.createElement("highestProgress");
				attribute.appendChild(dom.createTextNode(e.getLatestProgress()+""));
				posture.appendChild(attribute);
				attribute = dom.createElement("name");
				attribute.appendChild(dom.createTextNode(p.getName()));
				posture.appendChild(attribute);
				attribute = dom.createElement("duration");
				attribute.appendChild(dom.createTextNode(p.getDuration()+""));
				posture.appendChild(attribute);
				Set<String> keySet = p.getJointMap().keySet();
				for(String key:keySet){
					attribute = dom.createElement(key);
					attribute.appendChild(dom.createTextNode(p.getJointMap().get(key)[0]+";"+
							p.getJointMap().get(key)[1]+";"+p.getJointMap().get(key)[2]));
					posture.appendChild(attribute);
				}
				exercise.appendChild(posture);
			}
			rootEle.appendChild(exercise);
			dom.appendChild(rootEle);

			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				//tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				// send DOM to file
				tr.transform(new DOMSource(dom), 
						new StreamResult(new FileOutputStream(xml)));

			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
	}



	public Exercise importExerciseFromFile(String exerciseFile){
		return readFromXml(exerciseFile);
	}
	
	public Exercise readFromXml(String xml){
		Exercise e = new Exercise();
		Document dom;
		// Make an  instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use the factory to take an instance of the document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using the builder to get the DOM mapping of the    
			// XML file
			dom = db.parse(xml);
			Posture p = null;

			Element doc = dom.getDocumentElement();
			NodeList list =   doc.getElementsByTagName(Exercise.class.getSimpleName());
			if(list.getLength() > 0){
				Node exerciseNode =	list.item(0);
				list = exerciseNode.getChildNodes();
				for(int i = 0;i < list.getLength();i++){
					if(list.item(i).getNodeName().equals(Posture.class.getSimpleName())){
						p = getPosture(list.item(i));
						e.addPosture(p);
					}else{
						setValueValues(list.item(i),e);
					}
				}
			}
		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		return e;
	}

	private void setValueValues(Node item,Exercise e) {
		if(item.getNodeName().equals("latestProgress")){
			e.setLatestProgress(Integer.parseInt(item.getTextContent()));
		}
		else if(item.getNodeName().equals("highestProgress")){
			e.setHighestProgress(Integer.parseInt(item.getTextContent()));
		}
		else if(item.getNodeName().equals("name")){
			e.setName(item.getTextContent());
		}
	}

	private Posture getPosture(Node item) {
		Posture p = new Posture();
		NodeList list = item.getChildNodes();
		Set<String> keys = p.getJointMap().keySet();
		for(int i = 0;i < list.getLength();i++){
			if(keys.contains(list.item(i).getNodeName())){
				String[] valuesText =  list.item(i).getTextContent().split(";");
				double[] values = new double[valuesText.length];
				for(int ii = 0; ii < valuesText.length;ii++ ){
					values[ii] = Double.parseDouble(valuesText[ii]);
				}

				p.setJoinLocation(list.item(i).getNodeName(), values);
			}else{
				if(list.item(i).getNodeName().equals("latestProgress")){
					p.setLatestProgress(Integer.parseInt(list.item(i).getTextContent()));
				}else if(list.item(i).getNodeName().equals("highestProgress")){
					p.setHighestProgress(Integer.parseInt(list.item(i).getTextContent()));
				}else if(list.item(i).getNodeName().equals("name")){
					p.setName(list.item(i).getTextContent());
				}
				else if(list.item(i).getNodeName().equals("duration")){
					p.setDuration(Integer.parseInt(list.item(i).getTextContent()));
				}
			}
		}
		return p;
	}

	@Override
	public Exercise importExercise(){
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();	        
			return readFromXml(file.getAbsolutePath() );
		} else {

		}
		return null;
	}
	
	@Override
	public Posture importPosture() {
		JFileChooser fc = new JFileChooser();
		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();	        
			return importFromFile(file );

		} else {

		}
		return null;
	}
	
	@Override
	public void export(Exercise exercise) {

		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();	        

			saveToXML(exercise,file.getAbsolutePath());

		} else {

		}		
	}	

	@Override
	public void export(Posture p) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();	        

			export(p,file );

		} else {

		}
	}
}
