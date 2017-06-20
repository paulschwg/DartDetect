package application;

import hardware.TestMOG2;

public abstract class Game {
	protected int playerCount;
	protected Player[] players;
	protected boolean running;
	protected int goal = -1;
	protected Dartscheibe dartscheibe = new Dartscheibe();
	protected AngleToCoord atc = new AngleToCoord();
	protected TestMOG2 detect = new TestMOG2(this);
	
	protected double a1 = -1;
	protected double a2 = -1;
	
	public Game(int playerCount){
		this.playerCount = playerCount;
		players = new Player[playerCount];
	}
	
	public abstract void run();
	
	public abstract void playRound(Player player);
	
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
	
	/**
    * Wartet auf einen geworfenen Pfeil und übernimmt die von den Kameras ausgelesenen Winkel.
    * Mithilfe der AngleToCoord- und Dartscheibe-Klassen wird dann der Punktwert berechnet.
    * 
    * @return int[] mit Multiplikator (1-3) und Zahlenwert (1-20 oder 25)
    */
	public int[] getDart(){
		while (a1 == -1) { }
		int dartCoord[] = atc.calculateCoord(46.4,30.8);
		return dartscheibe.getScore(dartCoord[0], dartCoord[1]);
	}
	
	public void setAngles(double a1, double a2) {
		this.a1 = a1;
		this.a2 = a2;
	}
}
