package application;

import java.io.IOException;

import hardware.TestMOG2;

import java.io.IOException;

public class GameX01 extends Game {
	
	public GameX01(int playerCount, int goal){
		super(playerCount);
		this.goal = goal;
		initializePlayers(goal);
		
	}
	
	public boolean checkIfFinished(Player player){
		return player.getScore() == 0;
	}
	
	@Override
	public void processDart(int mult, int number){
		Player player = players[playerTurn - 1];
		int wurfScore = mult * number;
		player.removeScore(wurfScore);

		if (player.getScore() == 0){
			if (mult == 2) return; //Double Checkout => Gewonnen
			else { //Kein Double
				player.addScore(wurfScore);
				System.out.println("Double-Checkout erforderlich!");
				dartCount = 3; //Runde beendet
			}
		}
		if (player.getScore() < 0 || player.getScore() == 1) { //ï¿½berworfen
			player.addScore(wurfScore);
			System.out.println("Überworfen!");
			dartCount = 3; //Runde beendet
		}
		
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
			detect = new TestMOG2(this);
		}
	}
}
	
	
