package application;

import hardware.TestMOG2;

public abstract class Game {
	protected int playerCount;
	protected Player[] players;
	protected boolean running;
	protected int goal = -1;
	protected Dartscheibe dartscheibe = new Dartscheibe();
	protected AngleToCoord atc = new AngleToCoord();
	protected TestMOG2 detect;
	
	protected int dartCount = 1;
	protected int playerTurn = 1;

	
	public Game(int playerCount){
		this.playerCount = playerCount;
		players = new Player[playerCount];
	}
	
	public void run() {
		printAll();
		running = true;
		detect = new TestMOG2(this);
	}
	
	public abstract void processDart(int mult, int number);
	
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
	
	public void initializePlayers(){
		for (int i = 0; i < players.length; i++){
			players[i] = new Player(0,"Spieler " + (i + 1));
		}
	}
	
	public boolean running(){
		return running;
	}
		
	public void getDart(double a1, double a2) {
		int dartCoord[] = atc.calculateCoord(46.4,30.8);
		int score[] = dartscheibe.getScore(dartCoord[0], dartCoord[1]);
	}
}
