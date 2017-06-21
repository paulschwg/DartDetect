package application;

import gui.GUIController;

public class Main {
	
	private Game game;
	private GUIController gui;
	

	public static void main(String[] args) {
		Main main = new Main();
		main.launch();
	}
	
	public void launch() {
		Game game = new GameX01(1,501);
		game.addGUIInterface(gui);
		game.run();
	}

}
