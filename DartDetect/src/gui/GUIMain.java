package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import java.awt.Toolkit;

public class GUIMain extends Application {
	GUIController test;
	FXMLLoader loaderMain, loaderOptions;
	BorderPane rootBPane;
	GridPane optionsGPane;
	Scene sceneMain, sceneOptions;
	ActionEvent event = new ActionEvent();

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

			
			test = (GUIController) loaderMain.getController();
			test.openConfirm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GUIMain stagetest = new GUIMain();
		Stage teststage = new Stage();
		stagetest.start(teststage);
		
		//launch(args);
	}

	public GUIMain() {
	}

	public GUIController controller(){
		return test;
	}
}
