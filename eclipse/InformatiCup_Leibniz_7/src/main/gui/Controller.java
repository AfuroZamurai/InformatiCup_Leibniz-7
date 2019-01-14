package main.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import main.encodings.CircleEncoding;
import main.encodings.GridEncoding;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.Sign;
import main.generate.CheckerGenerator;
import main.generate.EvoEncoderGenerator;
import main.generate.IGenerator;
import main.generate.GeneratorFramework;
import main.generate.NoChange;
import main.generate.Parameter;
import main.generate.RecursiveSquareGenerator;
import main.generate.RandomCircleGenerator;
import main.generate.Parameter.ParameterType;
import main.io.ImageLoader;
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
	boolean disableLoad = false;
	int filter = 0;
	private int delayTime;
	private int maxIterations;
	Thread thread;
	public EventType<Event> update = new EventType<Event>(EventType.ROOT);
	public Task<Void> task;

	private GeneratorFramework moduleFramework = new GeneratorFramework(this);
	private IGenerator module;

	private ArrayList<Pair<Parameter, TextField>> parameterTextFieldList = new ArrayList<>();
	private ArrayList<Pair<Parameter, RadioButton>> parameterRadioButtonList = new ArrayList<>();

	Series series = new Series();
	private int iterationCounter = 0;

	IClassification imageClass; // Selected Sign
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
	private MenuItem menuItem5;

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
	private ListView<IClassification> listView;

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
	private Button stopButton;

	@FXML
	private Button generateButton;

	@FXML
	private Button SaveImageButton;

	@FXML
	private VBox parameterBox;

	@FXML
	private TextField textFieldDelayTime;

	@FXML
	private TextField textFieldMaxIterations;

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
		selectedAlgorithmn = menuItem1;
		// textField1.setVisible(false);
		if (imageClass != null) {
			enableButton(generateButton);
		}
		module = new NoChange();
		explanationArea.setText(module.getModuleDescription());
		parameterTextFieldList.clear();
		parameterRadioButtonList.clear();
		generateParameterLayout();
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
		selectedAlgorithmn = menuItem2;
		if (imageClass != null) {
			enableButton(generateButton);
		}
		module = new CheckerGenerator();
		explanationArea.setText(module.getModuleDescription());
		parameterTextFieldList.clear();
		parameterRadioButtonList.clear();
		generateParameterLayout();
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
		if (imageClass != null) {
			enableButton(generateButton);
		}

		module = new RandomCircleGenerator();
		explanationArea.setText(module.getModuleDescription());
		parameterTextFieldList.clear();
		parameterRadioButtonList.clear();
		generateParameterLayout();
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
		if (imageClass != null) {
			enableButton(generateButton);
		}
		module = new RecursiveSquareGenerator();
		explanationArea.setText(module.getModuleDescription());
		parameterTextFieldList.clear();
		parameterRadioButtonList.clear();
		generateParameterLayout();
	}

	@FXML
	void menuItem5clicked(ActionEvent event) {
		selectedAlgorithmn = menuItem5;
		if (imageClass != null) {
			enableButton(generateButton);
		}
		module = new EvoEncoderGenerator(new GridEncoding(8, 8));
		explanationArea.setText(module.getModuleDescription());
		parameterTextFieldList.clear();
		parameterRadioButtonList.clear();
		generateParameterLayout();
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
		imageClass = listView.getSelectionModel().getSelectedItem();

		try {
			inputImage.setImage(SwingFXUtils.toFXImage(imageClass.getExampleImage(), null));
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
		enableButton(stopButton);
		disableButton(generateButton);
		progressIndicator.setVisible(true);
		classLabel.setText("Klasse: " + listView.getSelectionModel().getSelectedItem());

		lineChart.getData().clear();
		series = new Series();
		lineChart.getData().add(series);
		iterationCounter = 0;

		if (selectedAlgorithmn != null) {
			if (parseParameters()) {
				moduleFramework.startModule(module, SwingFXUtils.fromFXImage(inputImage.getImage(), null),
						listView.getSelectionModel().getSelectedItem(), delayTime, maxIterations);
			} else {
				cancellation(null);
			}
		} else {
			showAlertError("Es wurde kein Verfahren ausgewï¿½hlt");
			disableButton(stopButton);
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
	public void cancellation(ActionEvent event) {

		moduleFramework.stopModule();

		progressIndicator.setVisible(false);
		generationLocked = false;
		enableButton(generateButton);
		disableButton(stopButton);
		enableButton(SaveImageButton);
		listView.setDisable(false);
		// setConfidence(confidence);

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

		String extension = ImageSaver.FileExtension.PNG.toString();

		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter(extension, "*." + extension.toLowerCase()));

		File file = fileChooser.showSaveDialog(stage);
		configuringFileChooser(fileChooser);

		if (file == null) {
			return;
		}

		try {
			String fileName = file + "";
			String[] split = fileName.split(Pattern.quote("."));
			ImageSaver.saveImage(image, split[split.length - 2]);
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

		for (ImageLoader.FileExtension ext : ImageLoader.FileExtension.values()) {
			fileChooser.getExtensionFilters()
					.add(new FileChooser.ExtensionFilter(ext.toString(), "*." + ext.toString().toLowerCase()));
		}

		File file = fileChooser.showOpenDialog(stage);
		configuringFileChooser(fileChooser);

		if (file == null) {
			return;
		}

		try {
			BufferedImage image = ImageLoader.loadImage(file + "");
			inputImage.setImage(SwingFXUtils.toFXImage(image, null));
			enableButton(generateButton);
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
		ObservableList<IClassification> obsList = FXCollections.observableArrayList();
		for (int i = 0; i < 43; i++) {
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

	/**
	 * Updated the confidence value, is displayed in green if the value exceeds a
	 * limit, otherwise red
	 */
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

	/**
	 * Updated the Image and the confidence
	 */
	public void updateResultImage(BufferedImage newImg, EvaluationResult evalResult) {
		setOutputImage(SwingFXUtils.toFXImage(newImg, null));
		setConfidence(evalResult.getConfidenceForClass(listView.getSelectionModel().getSelectedItem()));

		iterationCounter++;
	}

	/**
	 * Modulates the GUI to show an input-mask for parameters for the selected
	 * module.
	 */
	private void generateParameterLayout() {
		parameterBox.getChildren().clear();
		if (module != null) {
			List<Parameter> parameterList = module.getParameterList();

			if (parameterList != null) {
				for (Parameter parameter : parameterList) {
					HBox hbox = new HBox(5.0);
					hbox.setAlignment(Pos.CENTER);
					Label parameterLabel = new Label(parameter.getName());
					TextField parameterTextfield = new TextField();
					parameterLabel.setTooltip(new Tooltip(parameter.getDescription()));
					parameterTextfield.setTooltip(new Tooltip(parameter.getDescription()));

					switch (parameter.getType()) {
					case P_BOOL:
						RadioButton parameterButton1 = new RadioButton("Ja");
						RadioButton parameterButton2 = new RadioButton("Nein");
						parameterButton1.setTooltip(new Tooltip(parameter.getDescription()));
						parameterButton2.setTooltip(new Tooltip(parameter.getDescription()));
						ToggleGroup tg = new ToggleGroup();
						parameterButton1.setToggleGroup(tg);
						parameterButton2.setToggleGroup(tg);
						if (parameter.getBoolValue()) {
							parameterButton1.setSelected(true);

						} else {
							parameterButton2.setSelected(true);
						}
						parameterRadioButtonList.add(new Pair<Parameter, RadioButton>(parameter, parameterButton1));
						hbox.getChildren().addAll(parameterLabel, parameterButton1, parameterButton2);
						break;
					case P_FLOAT:
						parameterTextfield.setText("" + parameter.getFloatValue());
						parameterTextFieldList.add(new Pair<Parameter, TextField>(parameter, parameterTextfield));
						hbox.getChildren().addAll(parameterLabel, parameterTextfield);
						break;
					case P_INT:
						parameterTextfield.setText("" + parameter.getIntValue());
						parameterTextFieldList.add(new Pair<Parameter, TextField>(parameter, parameterTextfield));
						hbox.getChildren().addAll(parameterLabel, parameterTextfield);
						break;
					default:
						break;
					}
					parameterBox.getChildren().add(hbox);
				}
			}
		}
	}

	private boolean parseParameters() {
		for (Pair<Parameter, TextField> pair : parameterTextFieldList) {
			TextField textField = pair.getValue();
			try {
				if (pair.getKey().getType() == ParameterType.P_FLOAT) {
					float value = Float.parseFloat(textField.getText());
					pair.getKey().setFloatValue(value);
				} else if (pair.getKey().getType() == ParameterType.P_INT) {
					int value = Integer.parseInt(textField.getText());
					pair.getKey().setIntValue(value);
				}
			} catch (Exception e) {
				showAlertError("Bitte valide Parameter eingeben!");
				return false;
			}
		}

		if (parameterRadioButtonList != null) {
			for (Pair<Parameter, RadioButton> pair : parameterRadioButtonList) {
				RadioButton rb = pair.getValue();
				if (rb.isSelected()) {
					pair.getKey().setBoolValue(true);
				} else {
					pair.getKey().setBoolValue(false);
				}
			}
		}

		// Parse input for the two parameters (processing delay and max iterations)
		try {
			delayTime = Integer.parseInt(textFieldDelayTime.getText());
		} catch (Exception e) {
			showAlertError("Bitte ganze Zahlen als Parameter für die Verzögerungszeit eingeben!");
			return false;
		}

		try {
			maxIterations = Integer.parseInt(textFieldMaxIterations.getText());
		} catch (Exception e) {
			showAlertError("Bitte ganze Zahlen als Parameter für die max. Iterationen eingeben!");
			return false;
		}
		return true;
	}

}
