package main.gui;

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
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.IModule;
import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.TrasiWebEvaluator;
import main.io.ImageLoader;
import main.io.ImageSaver;
import main.module.ModuleFramework;
import main.module.SimpleIterationModule;

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
	boolean disableLoad = false;
	int filter = 0;
	Thread thread;
	public EventType<Event> update = new EventType<Event>(EventType.ROOT);
	public Task<Void> task;
	private ModuleFramework moduleFramework = new ModuleFramework(this);

	Series series = new Series();
	private int iterationCounter = 0;

	Sign sign; // Selected Sign
	MenuItem selectedAlgorithmn;
	@FXML
	private MenuButton menuButton;

	@FXML
	private MenuItem menuItem1;

	@FXML
	private MenuItem menuItem2;

	@FXML
	private MenuItem menuItem3;

	@FXML
	private MenuItem menuItem4;

	@FXML
	private TextArea explanationArea;

	@FXML
	private TextField textField1;

	@FXML
	private TextField textField11;

	@FXML
	private ImageView inputImage;

	@FXML
	private Label inputImageLabel;

	@FXML
	private ListView<Sign> listView;

	@FXML
	private Button loadImage;

	@FXML
	private ImageView outputImage;

	@FXML
	private Label outputImageLabel;

	@FXML
	private Label classLabel;

	@FXML
	private Label confidenceLabel;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private Button cancellationButton;

	@FXML
	private Button generateButton;

	@FXML
	private Button SaveImageButton;

	/**
	 * This method is an ActionEvent of a MenuItem. If a Sign is selected, the
	 * Generation Button gets enabled
	 * 
	 * @param event
	 *            a ActionEvent, when you click on a Radio Button
	 * @see MenuItem
	 * @see ActionEvent
	 */
	@FXML
	void menuItem1clicked(ActionEvent event) {
		explanationArea.setText(
				"Erklärungstext zu dem ausgewählten Algorithmus:\n\nDieser Algorithmus sendet das Eingabebild\nan die künstliche Inteligenz.\n"
						+ "Das Bild ist auch wieder das Ausgabebild,\nda keine Veränderung vorgenommen wurde\n"
						+ "und die dazugeöhrige Konfidenz wird ausgegeben.");
		selectedAlgorithmn = menuItem1;
		textField1.setVisible(false);
		if (sign != null) {
			enableButton(generateButton);
		}
	}

	/**
	 * This method is an ActionEvent of a MenuItem. If a Sign is selected, the
	 * Generation Button gets enabled
	 * 
	 * @param event
	 *            a ActionEvent, when you click on a Radio Button
	 * @see MenuItem
	 * @see ActionEvent
	 */
	@FXML
	void menuItem2clicked(ActionEvent event) {
		explanationArea.setText(
				"Pixelmanipulations-Algorithmus:\n\nEs gibt eine Gruppe von Pixel,\ndie auf schwarz gesetz wird.\n"
						+ "Steigt die Konfidenz, wird die Gruppe\nim Ausgabebild auf schwarz gesetzt, sonst weiß.\n"
						+ "Das wird mit allen Gruppen gemacht.\nDie größe der Gruppe gibt der Filter an.\n\n"
						+ "Filtergröße: ");
		selectedAlgorithmn = menuItem2;
		textField1.setVisible(true);
		if (sign != null) {
			enableButton(generateButton);
		}
	}

	/**
	 * This method is an ActionEvent of a MenuItem. If a Sign is selected, the
	 * Generation Button gets enabled
	 * 
	 * @param event
	 *            a ActionEvent, when you click on a Radio Button
	 * @see MenuItem
	 * @see ActionEvent
	 */
	@FXML
	void menuItem3clicked(ActionEvent event) {
		selectedAlgorithmn = menuItem3;
		if (sign != null) {
			enableButton(generateButton);
		}
		explanationArea.setText("leer");
		textField1.setVisible(false);
	}

	/**
	 * This method is an ActionEvent of a MenuItem. If a Sign is selected, the
	 * Generation Button gets enabled
	 * 
	 * @param event
	 *            a ActionEvent, when you click on a Radio Button
	 * @see MenuItem
	 * @see ActionEvent
	 */
	@FXML
	void menuItem4clicked(ActionEvent event) {
		selectedAlgorithmn = menuItem4;
		if (sign != null) {
			enableButton(generateButton);
		}
		explanationArea.setText("leer");
		textField1.setVisible(false);
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

		if (selectedAlgorithmn != null) {
			enableButton(generateButton);
		}
	}

	/**
	 * This method is an ActionEvent of the "Generate" Button. Takes the selected
	 * algorithm (MenuItem) and start the generation of the output image
	 * 
	 * @param event
	 *            the click on the button
	 * @see Button
	 * @see ActionEvent
	 */
	@FXML
	void generateImage(ActionEvent event) {

		listView.setDisable(true);
		enableButton(cancellationButton);
		disableButton(generateButton);

		lineChart.getData().clear();
		series = new Series();
		lineChart.getData().add(series);
		iterationCounter = 0;

		if (selectedAlgorithmn == menuItem1) {
			startAlgorithm(new TestModule());
		} else if (selectedAlgorithmn == menuItem2) {
			if (filter == 0) {
				showAlertError("Es muss eine Filtergröße angegeben werden");
				disableButton(cancellationButton);
				listView.setDisable(false);
				return;
			}

			moduleFramework.startModule(
					new PixelSearchCancellationProcess(listView.getSelectionModel().getSelectedItem(), this, filter),
					SwingFXUtils.fromFXImage(inputImage.getImage(), null),
					listView.getSelectionModel().getSelectedItem());
		} else if (selectedAlgorithmn == menuItem3) {
			moduleFramework.startModule(new SimpleIterationModule(),
					SwingFXUtils.fromFXImage(inputImage.getImage(), null),
					listView.getSelectionModel().getSelectedItem());
		} else if (selectedAlgorithmn == menuItem4) {
		} else {
			showAlertError("Es wurde kein Verfahren ausgewählt");
			disableButton(cancellationButton);
			listView.setDisable(false);
			return;
		}
	}

	/**
	 * This method is an ActionEvent of the "Cancellation" Button. Stops the
	 * generation of an image
	 * 
	 * @param event
	 *            the click on the button
	 * @see Button
	 * @see ActionEvent
	 */
	@FXML
	void cancellation(ActionEvent event) {

		moduleFramework.stopModule();

		progressIndicator.setVisible(false);
		generationLocked = false;
		enableButton(generateButton);
		disableButton(cancellationButton);
		enableButton(SaveImageButton);
		listView.setDisable(false);
		setConfidence(confidence);

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
	}

	/**
	 * This method is an ActionEvent of the "Load" Button. Start a FileChooser and
	 * takes selected picture as input image
	 * 
	 * @param event
	 *            the click on the button
	 * @see Button
	 * @see ActionEvent
	 * @see FileChooser
	 */
	@FXML
	void loadImage(ActionEvent event) {

		// BufferedImage image = SwingFXUtils.fromFXImage(outputImage.getImage(), null);
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);

		configuringFileChooser(fileChooser);

		try {
			BufferedImage image = ImageLoader.loadImage(file + "");
			inputImage.setImage(SwingFXUtils.toFXImage(image, null));
		} catch (IOException e) {
			e.printStackTrace();
			showAlertError("Es hat einen Fehler beim laden des Bildes gegeben.");
		}
	}

	@FXML
	void validateInput(ActionEvent event) {

		try {
			filter = Integer.parseInt(textField1.getText());
		} catch (Exception e) {
			showAlertError("Es muss eine ganze Zahl zwischen 1 und 64 sein!");
		}
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
		progressIndicator.setVisible(true);
		classLabel.setText("Class:" + sign);

		BufferedImage img = SwingFXUtils.fromFXImage(inputImage.getImage(), null);

		task = new Task<Void>() {
			@Override
			public Void call() {

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

				return null;
			}
		};

		ProgressBar bar = new ProgressBar();
		bar.progressProperty().bind(task.progressProperty());

		thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				progressIndicator.setProgress(progressIndicator.INDETERMINATE_PROGRESS);
				progressIndicator.setVisible(false);
				generationLocked = false;

				enableButton(generateButton);
				disableButton(cancellationButton);
				enableButton(SaveImageButton);
				listView.setDisable(false);

				setConfidence(confidence);
			}
		});
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
	 * Disable a given Button
	 * 
	 * @param button
	 * @see Button
	 */
	private void disableButton(Button button) {

		button.setDisable(true);
	}

	/**
	 * Enable a given Button
	 * 
	 * @param button
	 * @see Button
	 */
	private void enableButton(Button button) {

		button.setDisable(false);
	}

	/**
	 * Set new Output Image in the GUI
	 * 
	 * @param image
	 * @see WritableImage
	 */
	public void setOutputImage(WritableImage image) {

		outputImage.setImage(image);
	}

	public void setConfidence(float newConfidenceValue) {

		if (newConfidenceValue >= LIMIT_CONFIDENCE) {
			confidenceLabel.setTextFill(Color.web("#00ff00"));
			confidenceLabel.setText("Konfidenz: " + (int) (newConfidenceValue * 100) + "%");
		} else {
			confidenceLabel.setTextFill(Color.web("#ff0000"));
			confidenceLabel.setText("Konfidenz: " + (int) (newConfidenceValue * 100) + "%");
		}

		confidence = newConfidenceValue;
		progressBar.setProgress(newConfidenceValue);

		series.getData().add(new XYChart.Data("" + iterationCounter, newConfidenceValue));

	}

	public void updateResultImage(BufferedImage newImg, EvaluationResult evalResult) {
		setOutputImage(SwingFXUtils.toFXImage(newImg, null));
		setConfidence(evalResult.getConfidenceForSign(listView.getSelectionModel().getSelectedItem()));

		iterationCounter++;
	}

}
