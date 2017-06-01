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
	
	public void playRound(){
		Dartscheibe dartscheibe = new Dartscheibe();
		for (int i = 1; i <= 3; i++){
			//Warte auf Pfeil
			System.out.println("Wirf einen Pfeil!");
			int[] wurf = dartscheibe.getScore(1980, 1980);
			score -= (wurf[0] * wurf[1]);
			if (score == 0) return;
		}
	}
	
	public int getScore(){
		return score;
	}
	
	public String getName(){
		return name;
	}
}
