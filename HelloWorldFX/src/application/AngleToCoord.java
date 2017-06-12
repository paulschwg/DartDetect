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
    private double angle1, angle2;
    private int vzb1, vzb2;

    public AngleToCoord(){}

    /**
     * Konstruktor
     *
     * erhält als Parameter 2 Winkel
     *
     * @param a1 Winkel untere Kamera
     * @param a2 Winkel rechte Kamera
     */
    public AngleToCoord(double a1, double a2){
        if(a1 > 30) {
            this.setAngle1(Math.abs(30 - a1));
            this.setVzb1(1);
        }
        else {
            this.setAngle1(30 - a1);
            this.setVzb1(-1);
        }
        if(a2 > 30) {
            this.setAngle2(Math.abs(30 - a2));
            this.setVzb2(-1);
        }
        else {
            this.setAngle2(30 - a2);
            this.setVzb2(1);
        }
    }

    public double getAngle1() {
        return angle1;
    }

    public void setAngle1(double angle1) {
        this.angle1 = angle1;
    }

    public double getAngle2() {
        return angle2;
    }

    public void setAngle2(double angle2) {
        this.angle2 = angle2;
    }

    public int getVzb1() { return vzb1; }

    public void setVzb1(int vzb1) { this.vzb1 = vzb1; }

    public int getVzb2() { return vzb2; }

    public void setVzb2(int vzb2) { this.vzb2 = vzb2; }

    public int[] calculateCoord(){

        double m1, m2, n1, n2, x, y;
        int xf, yf;

        if(angle1 != 0) {
            m1 = vzb1 * cot(degtorad(this.angle1));
            n1 = this.ay - vzb1* this.ax*cot(degtorad(this.angle1));
        }
        else {
            m1 = 0;
            n1 = 0;
        }

        if(angle2 != 0){
            m2 = vzb2 * Math.tan(degtorad(this.angle2));
            n2 = this.by - vzb2* this.bx*Math.tan(degtorad(angle2));
        }
        else {
            m2 = 0;
            n2 = 2250;
        }

        System.out.println(n2);

        if(m1 != 0) {
            x = solve(m1, n1, m2, n2);
            y = m1*x+n1;
        }
        else{
            x = 2250;
            y = m2*x+n2;
        }



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

    public double degtorad(double deg){
        return Math.PI*deg/180;
    }
}
