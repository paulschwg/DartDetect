package application;

public class GameX01 extends Game {
	
	public GameX01(int playerCount, int goal){
		super(playerCount);
		this.goal = goal;
		initializePlayers(goal);
		
	}
	
	@Override
	public void run(){
		printAll();
		running = true;
		while (running){
			for (Player player: players){
				if (running) {
					playRound(player);
					running = !checkIfFinished(player);
				}
			}
			printAll();
		}
		for (Player player: players){
			if (player.getScore() == 0) System.out.println(player.getName() + " hat gewonnen!");
		}
	}
	
	public boolean checkIfFinished(Player player){
		return player.getScore() == 0;
	}
	
	@Override
	public void playRound(Player player){
		for (int i = 1; i <= 3; i++){
			int[] wurf = getDart();
			int wurfScore = wurf[0] * wurf[1];
			player.removeScore(wurfScore);

			if (player.getScore() == 0){
				if (wurf[0] == 2) return; //Double Checkout => Gewonnen
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
		}
	}
	
	
}
