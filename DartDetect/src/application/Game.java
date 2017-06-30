package application;

import gui.GUIController;
import gui.GUIMain;
import hardware.DartTrack;

/**
 * 
 * @author Daniel Klaus
 *
 */
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
		//Oberste Reihe der Score-Tabelle füllen, sieht fancy aus
		for (int i = 1; i <= playerCount; i++) {
			sendPlayerScoreToGUI(i, players[i-1].getScore());
		}
		//Erstelle DartTrack-Instanz und verlinke sie mit dem Spiel
		detect = new DartTrack(this);
	}
	
	/**
	 * Diese Methode wird aufgerufen, sobald ein neues Score-Array berechnet wurde, welches einem Spieler angerechnet werden kann.
	 * 
	 * @param mult		Multiplikator des Wurfs (min: 0, max: 3)
	 * @param number	Zahlenwert des Wurfs (min: 0, max: 25)
	 */
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

	/**
	 * Diese Methode wird aufgerufen, sobald ein neuer Pfeil erkannt wurde und die Winkel feststehen.
	 * Mit Hilfe unserer beiden Hilfsklassen wird das Winkel-Array in ein Score-Array umgewandelt und an processDart übergeben
	 * @param a1	Winkel von Kamera 1 erkannt
	 * @param a2	Winkel von Kamera 2 erkannt
	 */
	public void getDart(double a1, double a2) {
		int dartCoord[] = atc.calculateCoord(a1,a2);
		int score[] = dartscheibe.getScore(dartCoord[0], dartCoord[1]);
		processDart(score[0],score[1]);
	}
	
	/**
	 * Linkt GUI mit dem aktuellen Spiel
	 * @param gui
	 */
	public void addGUIInterface(GUIMain gui) {
		this.gui = gui;
	}
	
	/**
	 * Übergibt einen geworfenen Dart an die GUI zur Anzeige (Hände hoch!)
	 * @param player	SpielerID, bei dem der Dart angezeigt wird
	 * @param dart		Dart 1, 2, oder 3 einer Runde
	 * @param mult		Score-Multiplikator
	 * @param number	Score-Basis
	 */
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
	
	/**
	 * Übergibt einen Player-Score an die GUI, damit er in der Liste an der "Tafel" hinzugefügt werden kann
	 * @param player	SpielerID, bei dem der Score hinzugefügt wird
	 * @param score		Player-Score
	 */
	public void sendPlayerScoreToGUI(int player, int score) {
		switch (player) {
			case 1: gui.controller().addPointsPlayer1(score); break;
			case 2: gui.controller().addPointsPlayer2(score); break;
			default: break;
		}
	}
	
	/**
	 * Cleart die 3 Dart-Anzeige-Labels für einen Spieler
	 * @param player	SpielerID, bei dem die Labels gecleart werden
	 */
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
