package gui;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.awt.*;

public class Main extends Application {
	GUIController test = null;
	FXMLLoader loaderMain, loaderOptions;
	BorderPane rootBPane;
	GridPane optionsGPane;
	Scene sceneMain, sceneOptions;
	Stage stOptions;
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			primaryStage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().height-60);
			primaryStage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().width-20);
			loaderMain = new FXMLLoader(getClass().getResource("GUIMainPage.fxml"));
			rootBPane = (BorderPane) loaderMain.load();
			sceneMain = new Scene(rootBPane);
			sceneMain.getStylesheets().add(getClass().getResource("guiscenebuilder.css").toExternalForm());
			test = (GUIController) loaderMain.getController();
			test.setLblP1T1("asdf2");
			primaryStage.setTitle("DartDetect");
			primaryStage.setScene(sceneMain);
			primaryStage.setResizable(false);
			primaryStage.show();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
	

}
