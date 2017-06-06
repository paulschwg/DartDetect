package application;

/*
 * Koordinaten der Einschlagsstelle: Koordinatensystem von (0,0) bis (4500,4500)
 * Fl‰che f¸r Punkte begrenzt bei 550 und 3950 => 34cm Durchmesser der Punktefl‰che
 * (breite, hoehe)
 * Mitte des Bulls-Eyes bei (2250,2250)
 * 10 Einheiten entsprechen 1 Millimeter.
 */

public class Dartscheibe {

	private final static int POS_MIDDLE = 2250;
	private final static int RANGE_DOUBLE = 1700;
	private final static int RANGE_OUTER_SINGLE = 1620;
	private final static int RANGE_TRIPLE = 1070;
	private final static int RANGE_INNER_SINGLE = 990;
	private	final static int RANGE_BULL = 318;
	private	final static int RANGE_DOUBLE_BULL = 127;
	
	public Dartscheibe () { }
	
	public int[] getScore(int breite, int hoehe){
		/*
		 * Abstandsberechnung mit Pythagoras
		 */
		double distance = Math.sqrt( Math.pow(Math.abs(hoehe-POS_MIDDLE),2) + Math.pow(Math.abs(breite-POS_MIDDLE),2) );
		
		/*
		 * Winkelberechnung: arctan(Gegenkathete/Ankathete)
		 * Gesucht ist jeweils der Winkel, der an die n‰chste Gerade gegen den Uhrzeigersinn anliegt.
		 * Math.atan liefert Winkel im Bogenmaﬂ, deswegen Umrechnung in Gradmaﬂ.
		 * 
		 * Ergebnisse:
		 * Norden (20) = 0∞
		 * Osten (6)   = 90∞
		 * S¸den (3)   = 180∞
		 * Westen (11) = 270∞ 
		 */
		double angle = 0;
		
		if (breite > POS_MIDDLE && hoehe > POS_MIDDLE) { //Oben rechts => Winkel von der Gerade nach oben + 0
			angle = Math.atan((1.0*breite-POS_MIDDLE)/(1.0*hoehe-POS_MIDDLE))*180/Math.PI;
		}
		else if (breite > POS_MIDDLE && hoehe < POS_MIDDLE) { //Unten rechts => Winkel von der Gerade nach rechts + 90
			angle = Math.atan((1.0*POS_MIDDLE-hoehe)/(1.0*breite-POS_MIDDLE))*180/Math.PI + 90;
		}
		else if (breite < POS_MIDDLE && hoehe < POS_MIDDLE) { //Unten rechts => Winkel von der Gerade nach unten + 180
			angle = Math.atan((1.0*POS_MIDDLE-breite)/(1.0*POS_MIDDLE-hoehe))*180/Math.PI + 180;
		}
		else if (breite < POS_MIDDLE && hoehe > POS_MIDDLE) { //Oben links => Winkel von der Gerade nach links + 270
			angle = Math.atan((1.0*hoehe-POS_MIDDLE)/(1.0*POS_MIDDLE-breite))*180/Math.PI + 270;
		}
		else if (breite == POS_MIDDLE && hoehe > POS_MIDDLE){ //Genau ¸ber der Mitte => Winkel = 0 
			angle = 0;
		}
		else if (hoehe == POS_MIDDLE && breite > POS_MIDDLE){ //Genau rechts von der Mitte => Winkel = 90
			angle = 90;
		}
		else if (breite == POS_MIDDLE && hoehe < POS_MIDDLE){ //Genau unter der Mitte => Winkel = 180
			angle = 180;
		}
		else if (hoehe == POS_MIDDLE && breite < POS_MIDDLE){ //Genau links von der Mitte => Winkel = 270
			angle = 270;
		}
		
		System.out.println("Abstand von der Mitte: " + distance/10 + " Millimeter, Winkel: " + angle);
		int[] score = new int[] {0,0};
		
		/*
		 * Je nach Abstand von der Mitte werden die Score-Variablen gesetzt.
		 * Trifft der Pfeil auﬂerhalb vom Bullseye aber innerhalb der Wertungszone, wird zus‰tzlich der Zahlenwert anhand des Winkels berechnet.
		 */
		if (distance < RANGE_DOUBLE_BULL) {
			score[0] = 2;
			score[1] = 25;
		}
		else if (distance < RANGE_BULL) {
			score[0] = 1;
			score[1] = 25;
		}
		else if (distance < RANGE_INNER_SINGLE) {
			score[0] = 1;
		}
		else if (distance < RANGE_TRIPLE) {
			score[0] = 3;
		}
		else if (distance < RANGE_OUTER_SINGLE) {
			score[0] = 1;
		}
		else if (distance < RANGE_DOUBLE) {
			score[0] = 2;
		}
		
		if (distance > RANGE_BULL && distance < RANGE_DOUBLE){
			score[1] = getFieldNumber(angle);
		}
		
		System.out.println("Faktor: " + score[0] + ", Wert: " + score[1] + " => " + score[0]*score[1] + " Punkte");

		return score;
	}
	
	public int getFieldNumber(double angle){
		if (angle <= 9) return 20;
		else if (angle <= 27) return 1;
		else if (angle <= 45) return 18;
		else if (angle <= 63) return 4;
		else if (angle <= 81) return 13;
		else if (angle <= 99) return 6;
		else if (angle <= 117) return 10;
		else if (angle <= 135) return 15;
		else if (angle <= 153) return 2;
		else if (angle <= 171) return 17;
		else if (angle <= 189) return 3;
		else if (angle <= 207) return 19;
		else if (angle <= 225) return 7;
		else if (angle <= 243) return 16;
		else if (angle <= 261) return 8;
		else if (angle <= 279) return 11;
		else if (angle <= 297) return 14;
		else if (angle <= 315) return 9;
		else if (angle <= 333) return 12;
		else if (angle <= 351) return 5;
		else return 20;
	}
	
}
