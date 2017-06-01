package application;

public abstract class Game {
	protected int playerCount;
	protected Player[] players;
	protected boolean running;
	protected int goal = -1;
	
	public Game(int playerCount){
		this.playerCount = playerCount;
		players = new Player[playerCount];
	}
	
	public void run(){
		printAll();
		running = true;
		while (running){
			for (Player player: players){
				if (running) {
					System.out.println("Hi!");
					player.playRound();
					running = !checkIfFinished(player);
				}
			}
			printAll();
		}
	}
	
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
	
	public boolean running(){
		return running;
	}
	
	public boolean checkIfFinished(Player player){
		if (goal == -1) return false;
		else return player.getScore() <= 0;
	}
}
