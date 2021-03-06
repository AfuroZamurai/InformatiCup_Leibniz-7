package main.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GuiStart extends Application {

	public static void main(String[] args) {
		Application.launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {

		Pane mainPane = (Pane) FXMLLoader.load(GuiStart.class.getResource("MainScene.fxml"));
		stage.setScene(new Scene(mainPane));
		stage.show();
	}

}
