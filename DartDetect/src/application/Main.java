package application;

import gui.GUIController;
import gui.GUIMain;

public class Main {
	
	private Game game;
	private GUIMain gui;
	

	public static void main(String[] args) {
		Main main = new Main();
		main.launch();
	}
	
	public void launch() {
		Game game = new GameX01(1,501);
		gui = new GUIMain();
		game.addGUIInterface(gui);
		game.run();
	}

}
