package application;

import gui.GUIMain;
import javafx.application.Application;

public class Main {
	
	private Game game;
	public static GUIMain gui;
	public Thread guiThread;
	

	public static void main(String[] args) {
		Main main = new Main();
		main.launch();
	}
	
	public void launch() {
		Game game = new GameX01(2,501);

		(new Thread(new GUIMain())).start();
		while (gui == null) {System.out.println("Waiting for GUI");};
		game.addGUIInterface(gui);
		game.run();
	}

}
