package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GUI extends Application {

	Stage thestage, stOptions, stCalibrate;
	GridPane pane, paOptions, paCalibrate;
	Scene scene, scOptions, scCalibrate;
	Button bOptions, bCalibrate;
	Label lGameMode, lPlayer, lQuestionmark;
	ComboBox cbGameMode, cbPlayer;
	Tooltip tQuestionmark;
	TableView table;
	TableColumn tcFirstPlayer, tcSecondPlayer;
	VBox vbTable;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		thestage = primaryStage;
		thestage.setMaximized(true);

		bOptions = new Button("Optionen");
		bCalibrate = new Button("Kameras kalibrieren");
		pane = new GridPane();
		paOptions = new GridPane();
		paCalibrate = new GridPane();
		scene = new Scene(pane);
		scOptions = new Scene(paOptions, 450, 400);
		scCalibrate = new Scene(paCalibrate, 600, 400);
		stOptions = new Stage();
		stCalibrate = new Stage();
		lGameMode = new Label("Spielmodi:");
		lPlayer = new Label("Spieleranzahl:");
		lQuestionmark = new Label("?");
		tQuestionmark = new Tooltip("dsdsfkjhdsighfsiodgxdhfdhfkjdhfkjshdfkjhsdfkuhsdkjfhsdkbfdshjf");
		ObservableList<String> modes = FXCollections.observableArrayList("301", "401", "501", "601", "701", "801", "901", "Freies Spiel");
		cbGameMode = new ComboBox(modes);
		ObservableList<String> player = FXCollections.observableArrayList("1 Spieler", "2 Spieler");
		cbPlayer = new ComboBox(player);
		table = new TableView();
		tcFirstPlayer = new TableColumn("Spieler 1");
		tcSecondPlayer = new TableColumn("Spieler 2");
		vbTable = new VBox();
		

		bOptions.setOnAction(e -> bOptionsClicked(e));
		bCalibrate.setOnAction(e -> bCalibrateClicked(e));

		pane.setId("pane1");
		paOptions.setId("pane2");
		paCalibrate.setId("pane3");
		lQuestionmark.setId("questionmark");

		
		pane.setHgap(20);
		paOptions.setVgap(20);
		paOptions.setHgap(40);
		paCalibrate.setVgap(20);
		paCalibrate.setHgap(20);
		table.setEditable(true);
		table.getColumns().addAll(tcFirstPlayer, tcSecondPlayer);
		cbGameMode.getSelectionModel().selectFirst();
		cbPlayer.getSelectionModel().selectFirst();
		Tooltip.install(lQuestionmark, tQuestionmark);
		tQuestionmark.setMaxWidth(300);
		tQuestionmark.setWrapText(true);
		
		
		scene.getStylesheets().add(this.getClass().getResource("gui.css").toExternalForm());
		scOptions.getStylesheets().add(this.getClass().getResource("gui.css").toExternalForm());
		scCalibrate.getStylesheets().add(this.getClass().getResource("gui.css").toExternalForm());

		stOptions.setScene(scOptions);
		stOptions.setTitle("Optionen");
		stCalibrate.setScene(scCalibrate);
		stCalibrate.setTitle("Kameras kalibrieren");
		primaryStage.setTitle("DartDetect");
		primaryStage.setScene(scene);

		pane.add(bOptions, 1, 1);
		pane.add(bCalibrate, 2, 1);
		pane.add(table, 2, 4);
		paOptions.add(lGameMode, 1, 5);
		paOptions.add(lPlayer, 1, 6);
		paOptions.add(cbGameMode, 2, 5);
		paOptions.add(cbPlayer, 2, 6);
		paOptions.add(lQuestionmark, 3, 5);
		primaryStage.show();
	}

	public void bOptionsClicked(ActionEvent e) {
		stOptions.showAndWait();
	}

	public void bCalibrateClicked(ActionEvent e) {
		stCalibrate.showAndWait();
	}
}
