package gui;

import application.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import java.awt.Toolkit;

/**
 * Created by PaulSchwieg, ElenaHackstein on 08.06.2017.
 */

public class GUIMain extends Application implements Runnable {
	GUIController controller;
	FXMLLoader loaderMain, loaderOptions;
	BorderPane rootBPane;
	GridPane optionsGPane;
	Scene sceneMain, sceneOptions;
	ActionEvent event = new ActionEvent();

	/**
	    * Startet die GUI
	    * Lädt FXML-Datei Main
	    * Created by EH, PS
	    * 
	    */
	
	@Override
	public void start(Stage primaryStage) {
		try {
			loaderMain = new FXMLLoader(getClass().getResource("GUIMainPage.fxml"));
			rootBPane = (BorderPane) loaderMain.load();
			rootBPane.setId("bpMain");
			sceneMain = new Scene(rootBPane);
			sceneMain.getStylesheets().add(getClass().getResource("guimain.css").toExternalForm());
			primaryStage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().height - 60);
			primaryStage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().width - 20);
			primaryStage.setTitle("DartDetect");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			primaryStage.setScene(sceneMain);
			primaryStage.setResizable(false);
			primaryStage.show();

			
			controller = (GUIController) loaderMain.getController();
			Main.gui = this;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GUIController controller(){
		return controller;
	}

	@Override
	public void run() {
		Application.launch();
	}
}
