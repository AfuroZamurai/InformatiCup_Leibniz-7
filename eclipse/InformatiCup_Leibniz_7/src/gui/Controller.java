package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.IModule;
import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.TrasiWebEvaluator;
import main.io.ImageSaver;

/**
 * This class is the Controller for the GUI
 * 
 * @author Dorian
 *
 */

public class Controller implements Initializable {

	float confidence; // recognition value of an image
	final float LIMIT_CONFIDENCE = 0.9f; // smallest value needed to pass the task
	
	boolean generationLocked = false; // true, when a picture is generated
	boolean disableStop = true;
	boolean disableGenerate = true;
	boolean disableSave = true;
	
	Sign sign;	// Selected Sign

	@FXML
	private RadioButton radioButton1;

	@FXML
	private ToggleGroup group1;

	@FXML
	private Button generateButton;
	
	@FXML
	private Button cancellationButton;

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
	private ListView<Sign> listView;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private ImageView inputImage;

	@FXML
	private Label confidenceLabel;

	@FXML
	private Label inputImageLabel;

	@FXML
	private Label outputImageLabel;

	@FXML
	private Label explanationLabel;

	/**
	 * This method is an ActionEvent of the Radio Buttons. If a Sign is selected 
	 * the Generation Button gets enabled
	 * @param event
	 *            a ActionEvent, when you click on a Radio Button
	 * @see RadioButton
	 * @see ActionEvent
	 */
	@FXML
	void radioButtonClicked(ActionEvent event) {
		if(sign != null) {
			enabelButton(generateButton);
		}
	}

	/**
	 * This method is an MouseEvent of the ListView. Select a Sign in the ListView
	 * and this Sign appear as an input image in the GUI
	 * 
	 * @param event
	 *            a MouseEvent, when you click on a Sign in the listView
	 * @see ListView
	 * @see MouseEvent
	 */
	@FXML
	void ClickedListView(MouseEvent event) {
		System.out.println("" + listView.getSelectionModel().getSelectedItem());
		sign = listView.getSelectionModel().getSelectedItem();

		try {
			inputImage.setImage(SwingFXUtils.toFXImage(EvaluationResult.getExampleImage(sign), null));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(group1.getSelectedToggle() != null) {
			enabelButton(generateButton);
		}
	}

	/**
	 * This method is an ActionEvent of the "Generate" Button. Takes the selected
	 * algorithm (RadioButton) and start the generation of the output image
	 * 
	 * @param event
	 *            the click on the button
	 * @see Button
	 * @see ActionEvent
	 */
	@FXML
	void generateImage(ActionEvent event) {

		enabelButton(cancellationButton);
		disabelButton(generateButton);
		if (radioButton1.isSelected()) {
			System.out.println("RadioButton 1 wurde angeklickt");

			startAlgorithm(new TestModule());
			// TODO
		} else if (radioButton2.isSelected()) {
			System.out.println("RadioButton 2 wurde angeklickt");
			startAlgorithm(new PixelSearchCancellationProcess(listView.getSelectionModel().getSelectedItem()));
			// TODO
		} else if (radioButton3.isSelected()) {
			System.out.println("RadioButton 3 wurde angeklickt");
			// TODO
		} else if (radioButton4.isSelected()) {
			System.out.println("RadioButton 4 wurde angeklickt");
			// TODO
		} else {

			showAlertError("Es wurde kein Verfahren ausgewählt");
			disabelButton(cancellationButton);
			return;
		}

	}

	/**
	 * This method is an ActionEvent of the "Cancellation" Button. Stopps the
	 * generation of an image
	 * 
	 * @param event
	 *            the click on the button
	 * @see Button
	 * @see ActionEvent
	 */
	@FXML
	void cancellation(ActionEvent event) {
		// TODO
	}

	/**
	 * This method is an ActionEvent of the "Save" Button. Takes the output Image
	 * and start a FileChooser
	 * 
	 * @param event
	 *            the click on the button
	 * @see Button
	 * @see ActionEvent
	 * @see FileChooser
	 */
	@FXML
	void saveImage(ActionEvent event) {

		progressIndicator.setVisible(true);

		if (outputImage.getImage() == null) {
			showAlertError("Es wurde noch kein Bild generiert, dass gespeichert werden kann.");
			progressIndicator.setVisible(false);
			return;
		}

		BufferedImage image = SwingFXUtils.fromFXImage(outputImage.getImage(), null);
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(stage);

		configuringFileChooser(fileChooser);

		try {
			ImageSaver.saveImage(image, file + "");
		} catch (IOException e) {
			e.printStackTrace();
			showAlertError("Es hat einen Fehler beim speichern des Bildes gegeben.");
		}
		progressIndicator.setVisible(false);
	}

	/**
	 * This method start the generation of an Image with an given algorithm. The
	 * program generates only one picture at time and runs as a separate thread.
	 * After generating the image it will be shown on the screen as well as the
	 * confidence.
	 * 
	 * @param module
	 *            algorithm for generating images
	 * @see IModule
	 * @see Service
	 */
	void startAlgorithm(IModule module) {

		if (generationLocked == true) {
			showAlertError("Es läuft bereits ein Algorithmus");
			return;
		}

		generationLocked = true;

		BufferedImage img = SwingFXUtils.fromFXImage(inputImage.getImage(), null);

		progressIndicator.setVisible(true);

		Service<Void> service = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						// Background work
						Image output = SwingFXUtils.toFXImage(module.generateImage(img), null);
						outputImage.setImage(output);
						TrasiWebEvaluator twb = new TrasiWebEvaluator();
						EvaluationResult er;
						try {
							// get confidence from outputImage for the selected sign

							er = twb.evaluateImage(SwingFXUtils.fromFXImage(output, null));
							confidence = er.getConfidenceForSign(listView.getSelectionModel().getSelectedItem());
						} catch (Exception e) {
							e.printStackTrace();
						}

						progressIndicator.setVisible(false);
						generationLocked = false;
						
						enabelButton(generateButton);
						disabelButton(cancellationButton);
						enabelButton(SaveImageButton);

						final CountDownLatch latch = new CountDownLatch(1);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								try {
									// FX Stuff done here
									// Set the confidence value in the Gui, coloured red if the value is less than a
									// given limit, else green
									if (confidence >= LIMIT_CONFIDENCE) {
										confidenceLabel.setTextFill(Color.web("#00ff00"));
										confidenceLabel.setText("Konfidenz: " + (int) (confidence * 100) + "%");
									} else {
										confidenceLabel.setTextFill(Color.web("#ff0000"));
										confidenceLabel.setText("Konfidenz: " + (int) (confidence * 100) + "%");
									}

								} finally {
									latch.countDown();
								}
							}
						});
						latch.await();
						// Keep with the background work
						return null;
					}
				};
			}
		};
		service.start();
	}

	/**
	 * The method sets the setting for the file chooser Set the Initial Directory to
	 * Home directory
	 * 
	 * @param fileChooser
	 *            file save dialogs
	 * @see FileChooser
	 */
	private void configuringFileChooser(FileChooser fileChooser) {

		fileChooser.setTitle("Explorer");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	/**
	 * The method starts an error screen and show a message
	 * 
	 * @param message
	 *            text with information about the error
	 * @see Alert
	 */
	public static void showAlertError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

	/**
	 * This method is called by starting the GUI and initializes the listView
	 * 
	 * @param url
	 *            an absolute URL giving the base location of the image
	 * @param resources
	 *            bundles contain locale-specific objects
	 * @see URL
	 * @see ResourceBundle
	 * @see ObservableList
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Sign[] arrayOfSigns = Sign.values();
		ObservableList<Sign> obsList = FXCollections.observableArrayList();
		for (int i = 1; i < 43; i++) {
			obsList.add(arrayOfSigns[i]);
		}

		listView.setItems(obsList);
	}
	
	/**
	 * @param button
	 * @see Button
	 */
	private void disabelButton(Button button){
		
		button.setDisable(true);
		
	}
	
	/**
	 * @param button          
	 * @see Button
	 */
	private void enabelButton(Button button){
		
		button.setDisable(false);
		
	}
}

