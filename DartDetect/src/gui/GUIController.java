package gui;

import application.GameX01;
import application.Main;
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

/**
 * Created by PaulSchwieg, ElenaHackstein, DanielKlaus on 15.06.2017.
 */

public class GUIController {

	@FXML
	private Button btnOptions, btnCamera, btnClose, btnPlay, btnChangeValue1, btnChangeValue2, btnConfirm, btnEndGame;
	@FXML
	private ComboBox<String> cbMode, cbPlayer;
	@FXML
	TextField tfP1T1, tfP1T2, tfP1T3, tfP2T1, tfP2T2, tfP2T3;
	@FXML
	ListView lvPlayer1, lvPlayer2;

	GUICalibration cameraWindow;
	FXMLLoader fxmlLoaderOptions;
	Pane paneConfirm;
	GridPane gridOptions;
	Stage stageOptions;
	Scene sceneOptions;
	application.Game game;
	String inputMode, inputPlayer;

	static boolean roundReady = false;
/**
 * �ffnet Optionenfenster
 * Created by EH, PS
 * 
 */
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
	
	/**
	 * �ffnet Kalibrierungsfenster
	 * Created by EH, PS
	 */

	public void openCamera(ActionEvent event) throws Exception {
		try {
			cameraWindow = new GUICalibration();
			cameraWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schlie�t Optionenfenster
	 * Created by EH, PS
	 */
	
	public void closeOptions(ActionEvent event) {
		stageOptions = (Stage) btnClose.getScene().getWindow();
		stageOptions.close();
	}


	/**
	 * Methode zum Starten der naechsten Runde
	 * Created by DK
	 * @param event
	 */
	
	public void nextRound(ActionEvent event){
		roundReady = true;
	}

	/**
	 * Startet ein Spiel
	 * Created by EH, PS
	 */
	
	public void playGame(ActionEvent event) {

		inputMode = cbMode.getValue();
		inputPlayer = cbPlayer.getValue();
		int players = Integer.parseInt(inputPlayer.substring(0,1));
		if (inputMode.equals("Freies Spiel")) {
			Main.startNewFree(players);
		} else {
			Main.startNewX01(players,Integer.parseInt(inputMode));
		}
		stageOptions = (Stage) btnPlay.getScene().getWindow();
		stageOptions.close();

	}
	
	/**
	 * Beendet das Spiel
	 * Created by EH, PS
	 */

	public void endGame(){
		//Main.game.getDetect().releaseCameras();
		System.exit(0);
	}
	
	/**
	 * Getter f�r Spielmodi
	 * Created by EH, PS
	 * @return
	 */

	public String getInputMode() {
		return inputMode;
	}

	/**
	 * Getter f�r Spieleranzahl
	 * Created by EH, PS
	 * @return
	 */
	
	public String getInputPlayer() {
		return inputPlayer;
	}
	
	/**
	 * Setzt das Label des 1. Darts f�r Spieler 1
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */

	public void setTfP1T1(int mult, int number) {
		this.tfP1T1.setText(mult + "*" + number);
	}
	
	/**
	 * Setzt das Label des 2. Darts f�r Spieler 1
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */
	public void setTfP1T2(int mult, int number) {
		this.tfP1T2.setText(mult + "*" + number);
	}
	
	/**
	 * Setzt das Label des 3. Darts f�r Spieler 1
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */
	public void setTfP1T3(int mult, int number) {
		this.tfP1T3.setText(mult + "*" + number);
	}

	/**
	 * Setzt das Label des 1. Darts f�r Spieler 2
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */
	public void setTfP2T1(int mult, int number) {
		this.tfP2T1.setText(mult + "*" + number);
	}

	/**
	 * Setzt das Label des 2. Darts f�r Spieler 2
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */
	public void setTfP2T2(int mult, int number) {
		this.tfP2T2.setText(mult + "*" + number);
	}

	/**
	 * Setzt das Label des 3. Darts f�r Spieler 2
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */
	public void setTfP2T3(int mult, int number) {
		this.tfP2T3.setText(mult + "*" + number);
	}
	
	/**
	 * Cleart alle Dart-Anzeige-Labels f�r Spieler 1
	 */
	public void clearTfP1() {
		this.tfP1T1.setText("");
		this.tfP1T2.setText("");
		this.tfP1T3.setText("");
	}

	/**
	 * Cleart alle Dart-Anzeige-Labels f�r Spieler 2
	 */
	public void clearTfP2() {
		this.tfP2T1.setText("");
		this.tfP2T2.setText("");
		this.tfP2T3.setText("");
	}
	
	/**
	 * F�gt den aktuellen Score von Spieler 1, �bergeben im Parameter, der Liste an der "Tafel" hinzu.
	 * @param points	Aktueller Score des Spielers
	 */
	public void addPointsPlayer1(int points) {
		lvPlayer1.getItems().add(points);

	}

	/**
	 * F�gt den aktuellen Score von Spieler 2, �bergeben im Parameter, der Liste an der "Tafel" hinzu.
	 * @param points	Aktueller Score des Spielers
	 */
	public void addPointsPlayer2(int points) {
		lvPlayer2.getItems().add(points);

	}
	
	/**
	 * Abfrage, ob die Pfeile von der Dartscheibe genommen wurden
	 * Created by DK
	 */

	public boolean isReady() {
		return roundReady;
	}

	public void waitForReady() {
		roundReady = false;
	}

}
