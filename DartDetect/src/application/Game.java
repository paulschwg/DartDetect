package application;

import gui.GUIController;
import gui.GUIMain;
import hardware.DartTrack;

public abstract class Game{
	protected int playerCount;
	protected Player[] players;
	protected int goal = -1;
	protected Dartscheibe dartscheibe = new Dartscheibe();
	protected AngleToCoord atc = new AngleToCoord();
	protected DartTrack detect;
	protected GUIMain gui;

	protected int dartCount = 1;
	protected int playerTurn = 1;

	
	public Game(int playerCount){
		this.playerCount = playerCount;
		players = new Player[playerCount];
	}
	
	public void run() {
		for (int i = 1; i <= playerCount; i++) {
			sendPlayerScoreToGUI(i, players[i-1].getScore());
		}
		detect = new DartTrack(this);
	}
	
	public abstract void processDart(int mult, int number);
	
	public void printAll(){
		for (Player player: players){
			System.out.println(player.status());
		}
	}
	
	public void initializePlayers(int score){
		for (int i = 0; i < players.length; i++){
			players[i] = new Player(score,"Spieler " + (i + 1));
		}
	}	
	
	public void initializePlayers(){
		for (int i = 0; i < players.length; i++){
			players[i] = new Player(0,"Spieler " + (i + 1));
		}
	}

	public void getDart(double a1, double a2) {
		int dartCoord[] = atc.calculateCoord(a1,a2);
		int score[] = dartscheibe.getScore(dartCoord[0], dartCoord[1]);
		processDart(score[0],score[1]);
	}
	
	public void addGUIInterface(GUIMain gui) {
		this.gui = gui;
	}
	
	public void sendDartToGUI(int player, int dart, int mult, int number) {
		switch (player) {
			case 1:
				switch (dart) {
					case 1: gui.controller().setTfP1T1(mult,number); break;
					case 2: gui.controller().setTfP1T2(mult,number); break;
					case 3: gui.controller().setTfP1T3(mult,number); break;
					default: break;
				} break;
			case 2:
				switch (dart) {
				case 1: gui.controller().setTfP2T1(mult,number); break;
				case 2: gui.controller().setTfP2T2(mult,number); break;
				case 3: gui.controller().setTfP2T3(mult,number); break;
				default: break;
			} break;
			default: break;
		}
	}
	
	public void sendPlayerScoreToGUI(int player, int score) {
		switch (player) {
			case 1: gui.controller().addPointsPlayer1(score); break;
			case 2: gui.controller().addPointsPlayer2(score); break;
			default: break;
		}
	}
	
	public void clearDartsInGUI(int player) {
	    System.out.println(gui);
		switch (player) {
			case 1: gui.controller().clearTfP1(); break;
			case 2: gui.controller().clearTfP2(); break;
			default: break;
		}
	}

	public DartTrack getDetect() {
		return detect;
	}
}
