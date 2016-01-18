package app;

import app.classifier_wrappers.ArtificialNNClassifierWrapper;
import app.classifier_wrappers.K_NNClassifierWrapper;
import app.classifier_wrappers.RandomForestClassifierWrapper;
import app.ui.UserInterface;

public class ClassifierBox {
	private RandomForestClassifierWrapper rFWrapper = null;
	private K_NNClassifierWrapper kNNWrapper = null;
	private ArtificialNNClassifierWrapper aNNWrapper = null;
	
	public static void main(String[] args) {
		ClassifierBox cb = new ClassifierBox();
		cb.initiateRandomForestClassifier();
		UserInterface ui = new UserInterface();
		cb.startRFC(ui);

	}
	
	public void startRFC(UserInterface callback) {
		// TODO Auto-generated method stub
		
	}

	public void initiateRandomForestClassifier() {
				
	}

	public void initiateClassifiers(UserInterface calbacks){
		
	}

}
