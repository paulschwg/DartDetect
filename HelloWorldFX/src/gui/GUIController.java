package gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUIController {
	hardware.FXTest cameraWindow = new hardware.FXTest();
	// the FXML button
	@FXML
	private Button btnOptions, btnCamera;
	// the FXML image view

	public void openOptions(ActionEvent event) throws Exception {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUIOptions.fxml"));
			GridPane gridOptions = (GridPane) fxmlLoader.load();
			Stage stageOptions = new Stage();
			Scene sceneOptions = new Scene(gridOptions, 600, 400);
			sceneOptions.getStylesheets().add(getClass().getResource("guiscenebuilder.css").toExternalForm());
			stageOptions.setTitle("Optionen");
			stageOptions.setScene(sceneOptions);
			stageOptions.setResizable(false);
			stageOptions.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openCamera(ActionEvent event) throws Exception {
		try {
			cameraWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
