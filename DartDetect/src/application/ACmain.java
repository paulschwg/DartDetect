package application;

/**
 * Created by JohannesKoppe on 08.06.2017.
 */
public class ACmain {
    public static void main(String[] args) {
        AngleToCoord atc = new AngleToCoord();
        int[] coord = atc.calculateCoord(30,20);
        System.out.println(coord[0]);
        System.out.println(coord[1]);
    }
}
