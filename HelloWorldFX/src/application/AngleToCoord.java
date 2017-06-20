package application;

import java.lang.reflect.Array;

/**
 * Created by JohannesKoppe on 08.06.2017.
 */
public class AngleToCoord {

    private final static int AX = 2250;
    private final static int AY = -3270;
    private final static int BX = 7730;
    private final static int BY = 2250;
    
    private double angle1, angle2;
    private int vzb1, vzb2;

    public AngleToCoord() { }
   
    /**
    * Berechnet Koordinaten aus 2 Winkeln
    * 
    * @param a1 Winkel untere Kamera
    * @param a2 Winkel rechte Kamera
    * @return int[] mit x & y Koordinaten
    */
    public int[] calculateCoord(double a1, double a2){
    	setMemberVariables(a1, a2);

        double m1, m2, n1, n2, x, y;
        int xf, yf;

        if(angle1 != 0) {
            m1 = vzb1 * cot(degtorad(this.angle1));
            n1 = AY - vzb1 * AX * cot(degtorad(this.angle1));
        }
        else {
            m1 = 0;
            n1 = 0;
        }

        if(angle2 != 0){
            m2 = vzb2 * Math.tan(degtorad(this.angle2));
            n2 = BY - vzb2 * BX *Math.tan(degtorad(angle2));
        }
        else {
            m2 = 0;
            n2 = 2250;
        }

        if(m1 != 0) {
            x = solve(m1, n1, m2, n2);
            y = m1*x+n1;
        }
        else {
            x = 2250;
            y = m2*x+n2;
        }

        xf = (int) Math.round(x);
        yf = (int) Math.round(y);

        return new int[]{xf,yf};
    }
    
    public void setMemberVariables(double a1, double a2){
    	angle1 = anglecorrection(Math.abs(23.93 - a1));
    	angle2 = anglecorrection(Math.abs(23.93 - a2));
    	
    	if(a1 > 23.93) vzb1 = 1;
        else vzb1 = -1;
    	
    	if(a2 > 23.93) vzb2 = -1;
        else vzb2 = 1;
    }

    public double solve(double m1,double n1,double m2,double n2){
        double x;

        /*
	        Herleitung der Berechnungsformel:
	        
	        ax + b = cx + d;
	        ax + b - d = cx;
	        b-d = cx - ax;
	        b-d = (c-a)x:
	        (b-d)/(c-a) = x
         */

        x = (n1-n2)/(m2-m1);

        return x;
    }

    public double anglecorrection(double a){

        return 1.05*a;
    }

    public double cot(double a){
        return (Math.cos(a)/Math.sin(a));
    }

    public double degtorad(double deg){
        return Math.PI*deg/180;
    }
}
