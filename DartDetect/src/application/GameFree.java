package application;

import hardware.DartTrack;

/**
 * 
 * @author Daniel Klaus
 *
 */
public class GameFree extends Game {

	public GameFree(int playerCount) {
		super(playerCount);
		initializePlayers();
	}

	@Override
	public void processDart(int mult, int number){
		Player player = players[playerTurn - 1];
		
		int wurfScore = mult * number;
		player.addScore(wurfScore);
		sendDartToGUI(playerTurn, dartCount, mult, number);
		dartCount++;
		
		if (dartCount == 4) { //Runde beendet
			sendPlayerScoreToGUI(playerTurn, player.getScore());
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
