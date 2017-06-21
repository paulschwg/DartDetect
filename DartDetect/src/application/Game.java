package application;

import gui.GUIController;
import hardware.DartTrack;

public abstract class Game {
	protected int playerCount;
	protected Player[] players;
	protected boolean running;
	protected int goal = -1;
	protected Dartscheibe dartscheibe = new Dartscheibe();
	protected AngleToCoord atc = new AngleToCoord();
	protected DartTrack detect;
	protected GUIController gui;
	
	protected int dartCount = 1;
	protected int playerTurn = 1;

	
	public Game(int playerCount){
		this.playerCount = playerCount;
		players = new Player[playerCount];
	}
	
	public void run() {
		printAll();
		running = true;
		System.out.println("Spieler 1 ist dran!");
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
	
	public boolean running(){
		return running;
	}
		
	public void getDart(double a1, double a2) {
		int dartCoord[] = atc.calculateCoord(a1,a2);
		int score[] = dartscheibe.getScore(dartCoord[0], dartCoord[1]);
		processDart(score[0],score[1]);
	}
	
	public void addGUIInterface(GUIController gui) {
		this.gui = gui;
	}
	
	public void sendDartToGUI(int player, int dart, int score) {
		switch (player) {
			case 1:
				switch (dart) {
					case 1: gui.setTfP1T1(score); break;
					case 2: gui.setTfP1T2(score); break;
					case 3: gui.setTfP1T3(score); break;
					default: break;
				} break;
			case 2:
				switch (dart) {
				case 1: gui.setTfP2T1(score); break;
				case 2: gui.setTfP2T2(score); break;
				case 3: gui.setTfP2T3(score); break;
				default: break;
			} break;
			default: break;
		}
	}
	
	public void sendPlayerScoreToGUI(int player, int score) {
		switch (player) {
			case 1: gui.addPointsPlayer1(score); break;
			case 2: gui.addPointsPlayer2(score); break;
			default: break;
		}
	}
	
	public void clearDartsInGUI(int player) {
		switch (player) {
			case 1: gui.clearTfP1(); break;
			case 2: gui.clearTfP2(); break;
			default: break;
		}
	}
}
