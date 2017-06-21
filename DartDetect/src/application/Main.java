package application;

import gui.GUIMain;
import javafx.application.Application;

public class Main {
	
	private static Game game;
	public static GUIMain gui;
	public Thread guiThread;

	private static int players;
	private static int goal = -1;
	private static boolean startGame;


	public static void main(String[] args) {
		Main main = new Main();
		main.launch();
	}
	
	public void launch() {
		(new Thread(new GUIMain())).start();
		while (true) { //Warte auf Spielanfang
			System.out.println(startGame);
			if (startGame) {
				startGame = false;
				if (goal != -1){ //X01
					game = new GameX01(players,goal);
				} else {
					game = new GameFree(players);
				}
				game.addGUIInterface(gui);
				game.run();
			}
		}
	}

	public static void startNewX01(int pPlayers, int pGoal){
		players = pPlayers;
		goal = pGoal;
		startGame = true;
	}

	public static void startNewFree(int pPlayers) {
		players = pPlayers;
		startGame = true;
	}

	public static void endGame(){
		game = null;
	}

}
