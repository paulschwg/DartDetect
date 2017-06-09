package application;

public abstract class Game {
	protected int playerCount;
	protected Player[] players;
	protected boolean running;
	protected int goal = -1;
	protected Dartscheibe dartscheibe = new Dartscheibe();
	
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
	 * Diese Methode wartet nach dem Aufruf auf einen Pfeil und bekommt die von den Kameras erkannten Winkel übergeben.
	 * @return Score-Array [Multiplikator, Zahlenwert]
	 */
	public int[] getPfeil(){
		/*
		 * Hier müssen die Werte von den Kameras (Winkel) übergeben werden.
		 */
		AngleToCoord atc = new AngleToCoord(20,20);
		int wurfCoord[] = atc.calculateCoord();
		return dartscheibe.getScore(wurfCoord[0], wurfCoord[1]);
	}
}
