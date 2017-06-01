package application;

public class DartDemo {

	public static void main(String args[]) {
		
		Dartscheibe dartscheibe = new Dartscheibe();
		int[] score;

		/*
		 * Koordinaten der Einschlagsstelle: Koordinatensystem von (0,0) bis (3400,3400)
		 * (hoehe, breite)
		 * Mitte des Bulls-Eyes bei (1700,1700)
		 * 10 Einheiten entsprechen 1 Millimeter.
		 */
		score = dartscheibe.getScore(2500,1900);
		
		System.out.println("Faktor: " + score[0] + ", Wert: " + score[1] + " => " + score[0]*score[1] + " Punkte");
		
		/*
		 * Demo-Game 501
		 */
		GameX01 game = new GameX01(2,501);
		game.run();
		
		}

}
