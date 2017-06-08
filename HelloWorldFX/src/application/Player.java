package application;

public class Player {
	int score;
	String name;
	
	public Player(int score, String name){
		this.score = score;
		this.name = name;
	}
	
	public String status(){
		return "Spieler " + name + " hat einen Score von " + score + " Punkten!";
	}
	
	public int getScore(){
		return score;
	}
	
	public void addScore(int score){
		this.score += score;
	}
	
	public void removeScore(int score){
		this.score -= score;
	}
	
	public String getName(){
		return name;
	}
	
	public int[] wirfPfeil(){
		/*
		 * Hier müssen die Werte von den Kameras übergeben werden.
		 */
		return new int[] {1451,1109};
	}
}
