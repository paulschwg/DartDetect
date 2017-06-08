package application;

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

    public AngleToCoord(int a1, int a2){
        this.setAngle1(a1);
        this.setAngle2(a2);
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

    public int[] calculateAngle(){

    }
}
