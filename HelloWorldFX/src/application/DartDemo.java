package application;

public class DartDemo {

	public static void main() {
		
		Dartscheibe dartscheibe = new Dartscheibe();
		int[] score;

		score = dartscheibe.gibScore(2700,1700);
		
		System.out.println("Faktor: " + score[0] + ", Wert: " + score[1] + " => " + score[0]*score[1] + " Punkte");
		}

}
