package application;

import java.io.IOException;

import hardware.DartTrack;

public class GameFree extends Game {

	public GameFree(int playerCount) {
		super(playerCount);
		initializePlayers();
	}

	/*@Override
	public void playRound(Player player) {
		for (int i = 1; i <= 3; i++){
			int[] wurf = getDart();
			int wurfScore = wurf[0] * wurf[1];
			player.addScore(wurfScore);
		}
	}*/
	
	@Override
	public void processDart(int mult, int number){
		Player player = players[playerTurn - 1];
		
		int wurfScore = mult * number;
		player.addScore(wurfScore);		
		dartCount++;
		
		if (dartCount == 4) { //Runde beendet
			printAll();
			System.out.println("Drücke eine Taste, wenn du bereit bist!");
			try {
				System.in.read();
			} catch (IOException e){
				e.printStackTrace();
			}
			dartCount = 1;
			playerTurn++;
			if (playerTurn > playerCount) playerTurn = 1;
			System.out.println("Spieler " + playerTurn + " ist dran!");
			detect = new DartTrack(this);
		}
	}

}
