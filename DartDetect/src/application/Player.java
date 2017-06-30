package application;

/**
 * Spielerobjekt, das den Score und den Namen eines Spielers verwaltet.
 * @author Daniel Klaus
 *
 */
public class Player {
	int score,saveScore;
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
	
	/**
	 * Addiert Parameter score zu dem score des Spielers.
	 * @param score
	 */
	public void addScore(int score){
		this.score += score;
	}
	
	/**
	 * Subtrahiert Parameter score von dem score des Spielers.
	 * @param score
	 */
	public void removeScore(int score){
		this.score -= score;
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Speichert den aktuellen Punktestand zwischen, um zukünftig auf diese Punktzahl zu resetten.
	 * (Zum Beispiel bei einem Überwurf)
	 */
	public void saveScore() {
		saveScore = score;
	}
	
	/**
	 * Resettet auf eine vorher zwischengespeicherte Punktzahl.
	 */
	public void loadScore() {
		score = saveScore;
	}
}
