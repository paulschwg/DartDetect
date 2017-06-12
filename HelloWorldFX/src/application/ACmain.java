package application;

/**
 * Created by JohannesKoppe on 08.06.2017.
 */
public class ACmain {
    public static void main(String[] args) {
        AngleToCoord atc = new AngleToCoord(30,20);
        int[] coord = atc.calculateCoord();
        System.out.println(coord[0]);
        System.out.println(coord[1]);
    }
}
