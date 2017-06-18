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

public class Main extends Application {
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
			// load the FXML resource
			loaderMain = new FXMLLoader(getClass().getResource("GUIMainPage.fxml"));
			loaderOptions = new FXMLLoader(getClass().getResource("GUIOptions.fxml"));
			// store the root element so that the controllers can use it
			rootBPane = (BorderPane) loaderMain.load();
			optionsGPane = (GridPane) loaderOptions.load();
			// create and style a scene
			stOptions = new Stage();
			sceneMain = new Scene(rootBPane, 800, 600);
			sceneOptions = new Scene(optionsGPane,600,400);
			sceneMain.getStylesheets().add(getClass().getResource("guiscenebuilder.css").toExternalForm());
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("JavaFX meets OpenCV");
			primaryStage.setScene(sceneMain);
			stOptions.setTitle("Optionen");
			stOptions.setScene(sceneOptions);
			// show the GUI
			primaryStage.show();
			
			// set the proper behavior on closing the application

			GUIController controller = loaderMain.getController();
			
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
