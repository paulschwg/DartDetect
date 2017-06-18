package gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a> (minor fixes)
 * @version 2.0 (2016-09-17)
 * @since 1.0 (2013-10-20)
 *
 */
public class GUIController
{
	// the FXML button
	@FXML
	private Button btnOptions;
	// the FXML image view
	
	
	public void openOptions(ActionEvent event) throws Exception {               
        try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUIOptions.fxml"));
                GridPane root1 = (GridPane) fxmlLoader.load();
                Stage stage = new Stage();
                Scene options = new Scene(root1, 600,400);
                stage.setScene(options);
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
}

}
