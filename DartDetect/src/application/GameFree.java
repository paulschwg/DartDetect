package application;

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
		//TODO
	}

}
