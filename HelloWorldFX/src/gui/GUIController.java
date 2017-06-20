package gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUIController {
	hardware.FXTest cameraWindow = new hardware.FXTest();
	// the FXML button
	@FXML
	private Button btnOptions, btnCamera, btnClose, btnPlay;
	@FXML
	private ComboBox<String> cbMode, cbPlayer;
	@FXML
	Label lblP1T1,lblP1T2,lblP1T3,lblP2T1,lblP2T2,lblP2T3;
	@FXML
	// the FXML image view
	GridPane gridOptions;
	Stage stageOptions;
	Scene sceneOptions;
	application.GameX01 gameX01;
	application.GameFree gameFree;
	hardware.TestMOG2 runGame = new hardware.TestMOG2();
	String inputMode, inputPlayer;
	
	public void openOptions(ActionEvent event) throws Exception {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUIOptions.fxml"));
			gridOptions = (GridPane) fxmlLoader.load();
			stageOptions = new Stage();
			sceneOptions = new Scene(gridOptions, 600, 400);
			sceneOptions.getStylesheets().add(getClass().getResource("guiscenebuilder.css").toExternalForm());
			stageOptions.setTitle("Optionen");
			stageOptions.setScene(sceneOptions);
			stageOptions.setResizable(false);
//			setLblP1T1("asdf");
			stageOptions.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLblP1T1(String lblP1T1) {
//		if(this.lblP1T1 == null){this.lblP1T1 = new Label();}
		this.lblP1T1.setText(lblP1T1);
	}

	public void setLblP1T2(String lblP1T2) {
		this.lblP1T2.setText(lblP1T2);;
	}

	public void setLblP1T3(String lblP1T3) {
		this.lblP1T3.setText(lblP1T3);;
	}

	public void setLblP2T1(String lblP2T1) {
		this.lblP2T1.setText(lblP2T1);;
	}

	public void setLblP2T2(String lblP2T2) {
		this.lblP2T2.setText(lblP2T2);;
	}

	public void setLblP2T3(String lblP2T3) {
		this.lblP2T3.setText(lblP2T3);
	}

	public void openCamera(ActionEvent event) throws Exception {
		try {
			cameraWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeOptions(ActionEvent event) {
		stageOptions = (Stage) btnClose.getScene().getWindow();
		stageOptions.close();
	}
	
	public void playGame(ActionEvent event) {
		int numPlayer;

		inputMode = cbMode.getValue();
		inputPlayer = cbPlayer.getValue();

		if (inputPlayer == "1 Spieler") {
			numPlayer = 1;
		} else {
			numPlayer = 2;
		}

		if (inputMode == "Freies Spiel") {
			gameFree = new application.GameFree(numPlayer);
			gameFree.run();
		} else {
			switch (inputMode.substring(0, 1)) {
			case ("3"):
				gameX01 = new application.GameX01(numPlayer, 301);
				break;
				
			case ("4"):
				gameX01 = new application.GameX01(numPlayer, 401);
				break;
				
			case ("5"):
				gameX01 = new application.GameX01(numPlayer, 501);
				break;
				
			case ("6"):
				gameX01 = new application.GameX01(numPlayer, 601);
				break;
				
			case ("7"):
				gameX01 = new application.GameX01(numPlayer, 701);
				break;
				
			case ("8"):
				gameX01 = new application.GameX01(numPlayer, 801);
				break;
				
			case ("9"):
				gameX01 = new application.GameX01(numPlayer, 901);
				break;
			}
		}
		runGame.run();
		stageOptions = (Stage) btnPlay.getScene().getWindow();
		stageOptions.close();
	}

}
