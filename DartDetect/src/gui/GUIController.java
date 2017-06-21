package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUIController {

	@FXML
	private Button btnOptions, btnCamera, btnClose, btnPlay, btnChangeValue1, btnChangeValue2, btnConfirm;
	@FXML
	private ComboBox<String> cbMode, cbPlayer;
	@FXML
	TextField tfP1T1, tfP1T2, tfP1T3, tfP2T1, tfP2T2, tfP2T3;
	@FXML
	ListView lvPlayer1, lvPlayer2;

	GUICalibration cameraWindow;
	application.GameX01 gameX01;
	application.GameFree gameFree;
	FXMLLoader fxmlLoaderOptions;
	Pane paneConfirm;
	GridPane gridOptions;
	Stage stageOptions;
	Scene sceneOptions;
	application.Game game;
	String inputMode, inputPlayer;

	static boolean roundReady = false;

	public void openOptions(ActionEvent event) throws Exception {
		try {
			fxmlLoaderOptions = new FXMLLoader(getClass().getResource("GUIOptions.fxml"));
			stageOptions = new Stage();
			gridOptions = (GridPane) fxmlLoaderOptions.load();
			sceneOptions = new Scene(gridOptions, 600, 400);

			sceneOptions.getStylesheets().add(getClass().getResource("guioptions.css").toExternalForm());
			stageOptions.setTitle("DartDetect");
			stageOptions.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			gridOptions.setId("gOptions");
			stageOptions.setScene(sceneOptions);
			stageOptions.setResizable(false);

			stageOptions.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openCamera(ActionEvent event) throws Exception {
		try {
			cameraWindow = new GUICalibration();
			cameraWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeOptions(ActionEvent event) {
		stageOptions = (Stage) btnClose.getScene().getWindow();
		stageOptions.close();
	}


	/**
	 * Methode zum Starten der n�chsten Runde
	 * @param event
	 */
	public void nextRound(ActionEvent event){
		roundReady = true;
		System.out.println("Ready");
	}

	public void playGame(ActionEvent event) {

		inputMode = cbMode.getValue();
		inputPlayer = cbPlayer.getValue();

		if (inputMode == "Freies Spiel") {
			if (inputPlayer == "2 Spieler") {
			}
		} else {
			if (inputPlayer == "2 Spieler") {
			}
		}

		stageOptions = (Stage) btnPlay.getScene().getWindow();
		stageOptions.close();
	}


	public String getInputMode() {
		return inputMode;
	}

	public String getInputPlayer() {
		return inputPlayer;
	}

	public void setTfP1T1(int throw1) {
		this.tfP1T1.setText("" + throw1);
	}	
		
	public void setTfP1T1(int mult, int number) {
		this.tfP1T1.setText(mult + "*" + number);
	}

	public void setTfP1T2(int mult, int number) {
		this.tfP1T2.setText(mult + "*" + number);
	}

	public void setTfP1T3(int mult, int number) {
		this.tfP1T3.setText(mult + "*" + number);
	}

	public void setTfP2T1(int mult, int number) {
		this.tfP2T1.setText(mult + "*" + number);
	}

	public void setTfP2T2(int mult, int number) {
		this.tfP2T2.setText(mult + "*" + number);
	}

	public void setTfP2T3(int mult, int number) {
		this.tfP2T3.setText(mult + "*" + number);
	}

	public void clearTfP1() {
		this.tfP1T1.setText("");
		this.tfP1T2.setText("");
		this.tfP1T3.setText("");
	}

	public void clearTfP2() {
		this.tfP2T1.setText("");
		this.tfP2T2.setText("");
		this.tfP2T3.setText("");
	}

	public void addPointsPlayer1(int points) {
		lvPlayer1.getItems().add(points);

	}

	public void addPointsPlayer2(int points) {
		lvPlayer2.getItems().add(points);

	}

	public void changeValue1() {

	}

	public void changeValue2() {

	}

	public boolean isReady() {
		return roundReady;
	}

	public void waitForReady() {
		roundReady = false;
	}

}
