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
	
<<<<<<< HEAD
	public int[] wirfPfeil(){
		/*
		 * Hier müssen die Werte von den Kameras übergeben werden.
		 */
		AngleToCoord atc = new AngleToCoord(0, 27.5);
		return atc.calculateCoord();
	}
=======
>>>>>>> ae97477439b2eff1e7c0b6e3b080f480010aab42
}
