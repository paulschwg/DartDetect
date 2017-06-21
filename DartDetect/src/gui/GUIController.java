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
	FXMLLoader fxmlLoaderOptions, fxmlLoaderConfirm;
	Pane paneConfirm;
	GridPane gridOptions;
	Stage stageOptions, stageConfirm;
	Scene sceneOptions, sceneConfirm;
	application.Game game;
	String inputMode, inputPlayer;

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

	public void openConfirm(){
		try {
			fxmlLoaderConfirm = new FXMLLoader(getClass().getResource("GUIConfirm.fxml"));
			stageConfirm = new Stage();
			paneConfirm = (Pane) fxmlLoaderConfirm.load();
			sceneConfirm = new Scene(paneConfirm, 200, 200);

			sceneConfirm.getStylesheets().add(getClass().getResource("guiconfirm.css").toExternalForm());
			stageConfirm.setTitle("DartDetect");
			stageConfirm.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			paneConfirm.setId("pConfirm");
			stageConfirm.setScene(sceneConfirm);
			stageConfirm.setResizable(false);

			stageConfirm.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Methode zum Starten der nächsten Runde
	 * @param event
	 */
	public void nextRound(ActionEvent event){
		
	}
	
	public void playGame(ActionEvent event) {
		int numPlayer;
		String inputMode, inputPlayer;

		inputMode = cbMode.getValue();
		inputPlayer = cbPlayer.getValue();

		if (inputPlayer == "1 Spieler") {
			numPlayer = 1;
		} else {
			numPlayer = 2;
		}

		if (inputMode == "Freies Spiel") {
			gameFree = new application.GameFree(numPlayer);
		} else {
			gameX01 = new application.GameX01(numPlayer, Integer.parseInt(inputMode.substring(0, 1)) * 100 + 1);
		}

		stageOptions = (Stage) btnPlay.getScene().getWindow();
		stageOptions.close();
	}

	public void setTfP1T1(int throw1) {
		this.tfP1T1.setText("" + throw1);
	}

	public void setTfP1T2(int throw2) {
		this.tfP1T2.setText("" + throw2);
	}

	public void setTfP1T3(int throw3) {
		this.tfP1T3.setText("" + throw3);
	}

	public void setTfP2T1(int throw1) {
		this.tfP2T1.setText("" + throw1);
	}

	public void setTfP2T2(int throw2) {
		this.tfP2T2.setText("" + throw2);
	}

	public void setTfP2T3(int throw3) {
		this.tfP2T3.setText("" + throw3);
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

}
