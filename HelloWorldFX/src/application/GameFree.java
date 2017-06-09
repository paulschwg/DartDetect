package application;

public class GameFree extends Game {

	public GameFree(int playerCount) {
		super(playerCount);
		initializePlayers();
	}

	@Override
	public void run() {
		printAll();
		running = true;
		while (running){
			for (Player player: players){
				if (running) {
					playRound(player);
				}
			}
			printAll();
		}
	}

	@Override
	public void playRound(Player player) {
		for (int i = 1; i <= 3; i++){
			int[] wurf = getPfeil();
			int wurfScore = wurf[0] * wurf[1];
			player.addScore(wurfScore);
		}
	}

}
