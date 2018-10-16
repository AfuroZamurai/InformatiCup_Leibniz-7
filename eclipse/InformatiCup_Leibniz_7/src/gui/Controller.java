package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.fxml.builder.JavaFXImageBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.IModule;
import main.evaluate.TrasiWebEvaluator;
import main.io.ImageSaver;

public class Controller implements Initializable {
	
	boolean generationLocked = false;

	@FXML
	private RadioButton radioButton1;

	@FXML
	private ToggleGroup group1;

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
	private ListView<String> listView;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private ImageView inputImage;

	@FXML
	private Label konfidenzeLabel;

	@FXML
	private Label inputImageLabel;

	@FXML
	private Label outputImageLabel;

	@FXML
	void ClickedListView(MouseEvent event) {
		System.out.println("clicked");
	}

	@FXML
	void fillListView(ActionEvent event) {
		System.out.println("Fill");
	}

	@FXML
	void generateImage(ActionEvent event) {

		if (radioButton1.isSelected()) {
			System.out.println("RadioButton 1 wurde angeclicket");
			
			startAlgorithm(new TestModule());
			// TODO
		} else if (radioButton2.isSelected()) {
			System.out.println("RadioButton 2 wurde angeclicket");
			// TODO
		} else if (radioButton3.isSelected()) {
			System.out.println("RadioButton 3 wurde angeclicket");
			// TODO
		} else if (radioButton4.isSelected()) {
			System.out.println("RadioButton 4 wurde angeclicket");
			// TODO
		} else {
			System.out.println("Es wurde kein Verfahren ausgewählt");
		}		
	}
	
	void startAlgorithm(IModule module) {
		
		generationLocked = true;
		
		BufferedImage img = SwingFXUtils.fromFXImage( inputImage.getImage(), null);
		
		progressIndicator.setVisible(true);
		
		Thread t  = new Thread(new Runnable() {

			@Override
			public void run() {
				
				Image output = SwingFXUtils.toFXImage(module.generateImage(img), null);
				
				System.out.println("FINISHED");
				progressIndicator.setVisible(false);
				
				outputImage.setImage(output);
				
				TrasiWebEvaluator twb = new TrasiWebEvaluator();
				try {
					twb.evaluate( SwingFXUtils.fromFXImage(output, null));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				float konfidenz = 1;
				konfidenzeLabel.setText("Konfidenz:" + konfidenz);
				generationLocked = false;				
			}
			
		});		
		t.start();	
	}
	

	@FXML
	void saveImage(ActionEvent event) {

		progressIndicator.setVisible(true);

		if (outputImage.getImage() == null) {
			System.out.println("Es wurde noch kein Bild generiert, dass gespeichert werden kann.");
			progressIndicator.setVisible(false);
			return;
		}

		BufferedImage image = SwingFXUtils.fromFXImage(outputImage.getImage(), null);
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(stage);
		ImageSaver imageSaver = new ImageSaver();

		configuringFileChooser(fileChooser);

		try {
			imageSaver.saveImage(image, file + " ", null);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Es hat einen Fehler beim speichern des Bildes gegeben");
		}

		progressIndicator.setVisible(false);

	}

	private void configuringFileChooser(FileChooser fileChooser) {

		fileChooser.setTitle("Explorer");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // Set Initial Directory
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<String> obsList = FXCollections.observableArrayList("Test1", "Test2", "Test3");
		listView.setItems(obsList);

	}

}
