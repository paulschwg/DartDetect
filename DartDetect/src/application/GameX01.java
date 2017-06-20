package application;

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
		Player player = players[playerTurn];
		int wurfScore = mult * number;
		player.removeScore(wurfScore);

		if (player.getScore() == 0){
			if (mult == 2) return; //Double Checkout => Gewonnen
			else { //Kein Double
				player.addScore(wurfScore);
				System.out.println("Double-Checkout erforderlich!");
				return;
			}
		}
		if (player.getScore() < 0 || player.getScore() == 1) { //Überworfen
			player.addScore(wurfScore);
			System.out.println("Überworfen!");
			return;
		}
		
		dartCount++;
		
		if (dartCount == 4) { //Runde beendet
			printAll();
			dartCount = 1;
			playerTurn++;
			if (playerTurn > playerCount) playerTurn = 1;
		}
	}
}
	
	
