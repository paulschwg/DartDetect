package application;

import java.lang.reflect.Array;

/**
 * Created by JohannesKoppe on 08.06.2017.
 */
public class AngleToCoord {

    private static int ax = 2250;
    private static int ay = -3420;
    private static int bx = 7920;
    private static int by = 2250;
    private int angle1, angle2;

    public AngleToCoord(){}

    /**
     * Konstruktor
     *
     * erhält als Parameter 2 Winkel
     *
     * @param a1 Winkel untere Kamera
     * @param a2 Winkel rechte Kamera
     */
    public AngleToCoord(int a1, int a2){
        this.setAngle1(30 - a1);
        this.setAngle2(30 - a2);
    }

    public int getAngle1() {
        return angle1;
    }

    public void setAngle1(int angle1) {
        this.angle1 = angle1;
    }

    public int getAngle2() {
        return angle2;
    }

    public void setAngle2(int angle2) {
        this.angle2 = angle2;
    }

    public int[] calculateCoord(){

        double m1, m2, n1, n2, x, y;
        int xf, yf;

        m1 = -1 * cot(degtorad(this.angle1));
        m2 = Math.tan(degtorad(this.angle2));

        n1 = this.ay+this.ax*cot(degtorad(this.angle1));
        n2 = this.by-this.bx*Math.tan(degtorad(angle2));

        x = solve(m1,n1,m2,n2);

        y = m1*x+n1;

        xf = (int) Math.round(x);
        yf = (int) Math.round(y);

        int[] coord = {xf,yf};

        return coord;
    }

    public double solve(double m1,double n1,double m2,double n2){
        double x;

        /*
        ax + b = cx + d;
        ax + b - d = cx;
        b-d = cx - ax;
        b-d = (c-a)x:
        (b-d)/(c-a) = x
         */

        x = (n1-n2)/(m2-m1);

        return x;
    }

    public double cot(double a){
        return (Math.cos(a)/Math.sin(a));
    }

    public double degtorad(int deg){
        return Math.PI*deg/180;
    }
}
