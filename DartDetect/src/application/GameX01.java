package application;

import hardware.DartTrack;

/**
 * 
 * @author Daniel Klaus
 *
 */
public class GameX01 extends Game {
	
	public GameX01(int playerCount, int goal){
		super(playerCount);
		this.goal = goal;
		initializePlayers(goal);
	}

	@Override
	public void processDart(int mult, int number){
		Player player = players[playerTurn - 1];
		if (dartCount == 1) {
			player.saveScore();
		}
		
		int wurfScore = mult * number;
		player.removeScore(wurfScore);
		sendDartToGUI(playerTurn, dartCount, mult, number);

		if (player.getScore() == 0){
			if (mult == 2) {//Double Checkout => Gewonnen
				dartCount = 3; //Runde beendet
			}
			else { //Kein Double => Runde wird zurückgesetzt
				player.loadScore();
				dartCount = 3; //Runde beendet
			}
		}
		if (player.getScore() < 0 || player.getScore() == 1) { //Überworfen => Runde wird zurückgesetzt
			player.loadScore();
			dartCount = 3; //Runde beendet
		}
		
		dartCount++;
		
		if (dartCount == 4) { //Runde beendet
			sendPlayerScoreToGUI(playerTurn, player.getScore());
			if (player.getScore() == 0) {
				detect = null; //Spiel beendet
			}
			gui.controller().waitForReady();
			while (!gui.controller().isReady()) {System.out.println("Waiting for confirmation");} //Funktioniert nicht ohne syso, don't ask me why
			clearDartsInGUI(playerTurn);
			dartCount = 1;
			playerTurn++;
			if (playerTurn > playerCount) playerTurn = 1; //Neuer Spieler
			detect = new DartTrack(this);
		}
	}
}
	
	
