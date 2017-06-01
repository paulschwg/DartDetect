package application;

public class GameX01 extends Game {
	
	public GameX01(int playerCount, int goal){
		super(playerCount);
		this.goal = goal;
		initializePlayers(goal);
		
	}
	
	
}
