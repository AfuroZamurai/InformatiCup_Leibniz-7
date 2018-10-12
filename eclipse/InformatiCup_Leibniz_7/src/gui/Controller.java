package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;

public class Controller {


	    @FXML
	    private RadioButton radioButton1;

	    @FXML
	    private Button generateButton;

	    @FXML
	    private ImageView outputImage;

	    @FXML
	    private RadioButton radioButton2;

	    @FXML
	    private RadioButton radioButton3;

	    @FXML
	    private RadioButton radioButton4;

	    @FXML
	    private Button SaveImageButton;

	    @FXML
	    private ListView<?> listeView;

	    @FXML
	    private ProgressIndicator progressIndicator;

	    @FXML
	    private ImageView inputImage;

	    @FXML
	    private Label Konfidenze;

	    @FXML
	    private Label inputImageLabel;

	    @FXML
	    private Label outputImageLabel;


    @FXML
    void generateImage(ActionEvent event) {
    	
    	progressIndicator.setVisible(true);
    	
    	//TODO generate Image
    	
    	// progressIndicator.setVisible(false);
    }

    @FXML
    void saveImage(ActionEvent event) {
    	
    	progressIndicator.setVisible(true);
    	
    	//TODO save Image
    	
    	// progressIndicator.setVisible(false);
    }

}
