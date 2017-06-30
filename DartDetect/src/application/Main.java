package application;

import gui.GUIMain;
import javafx.application.Application;

/**
 * 
 * @author Daniel Klaus
 *
 */
public class Main {
	
	public static Game game;
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
			System.out.println(startGame);		//Ich weiﬂ nicht, wieso ich das hier drinhaben muss, aber ohne funktioniert es nicht
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

	/**
	 * L‰sst im Main-Thread eine neue X01-Spielinstanz starten
	 * @param pPlayers
	 * @param pGoal
	 */
	public static void startNewX01(int pPlayers, int pGoal){
		players = pPlayers;
		goal = pGoal;
		startGame = true;
	}

	/**
	 * L‰sst im Main-Thread eine neue Freies Spiel-Instanz starten
	 * @param pPlayers
	 */
	public static void startNewFree(int pPlayers) {
		players = pPlayers;
		startGame = true;
	}

	public static void endGame(){
		game = null;
	}

}
