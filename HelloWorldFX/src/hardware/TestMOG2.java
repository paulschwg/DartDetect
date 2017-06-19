package hardware;
import application.AngleToCoord;
import application.Dartscheibe;
import application.JSONAccess;
import org.json.simple.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.opencv.imgproc.Imgproc.MORPH_CLOSE;
import static org.opencv.imgproc.Imgproc.MORPH_ELLIPSE;
import static org.opencv.imgproc.Imgproc.MORPH_OPEN;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;


public class TestMOG2 {

    public static final double ANGEL_PER_PIXEL = 47.86/1280.0;
    public static final double CENTER = 640.0;
    private Point mPointCameraBottom = null;
    private Point mPointCameraRight = null;
    AngleToCoord mAngleToCoord = new AngleToCoord();
    Dartscheibe mDartscheibe = new Dartscheibe();
    private int dartCount;
    boolean abfrage = true;


    public static void main(String[] args) {
        TestMOG2 testMOG2 = new TestMOG2();
        testMOG2.run();
    }

    public void run() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture[] videoCaptures = {new VideoCapture(0),new VideoCapture(1)};
        SettingObject[] settingObjects = {new SettingObject(0, 190, 1280, 100,1, 315,324),
                new SettingObject(0, 355, 1280, 100,0, 500,500)};
        JSONObject jsonObject = JSONAccess.getJSON();
        settingObjects[1].initializeJSONValues((JSONObject) jsonObject.get("KameraBC"));
        settingObjects[0].initializeJSONValues((JSONObject) jsonObject.get("KameraRC"));
        /*VideoCapture[] videoCaptures = {new VideoCapture(0)};
        SettingObject[] settingObjects = {new SettingObject(5, 195, 1265, 200,1, 460)};*/


        while (abfrage) {

            for (int j = 0; j < videoCaptures.length; j++) {
                videoCaptures[j].set(CV_CAP_PROP_FRAME_WIDTH, 1280);
                videoCaptures[j].set(CV_CAP_PROP_FRAME_HEIGHT, 720);
                SettingObject s = settingObjects[j];

                if (videoCaptures[j].read(s.frame)) {
                    s.outerBox = new Mat(s.frame, new Rect(s.x, s.y, s.width, s.height));
                    s.backgroundSubtractorMOG2.apply(s.outerBox, s.fgMaskMOG2, s.learningRate);
                    Imgproc.morphologyEx(s.fgMaskMOG2, s.fgMaskMOG2, MORPH_CLOSE, s.kernel);
                    if (s.i == 1) {
                        ArrayList<Rect> rectArrayList = detectContours(s.frame, s.fgMaskMOG2, s);
                        if (rectArrayList.size() > 0) {
                            //s.detectedDartAtLastFrame = true;
                            for (Rect rect : rectArrayList) {
                                //Hier werden die umrandenden Rechtecke eingezeichnet
                                rect.x += s.x;
                                rect.y += s.y;
                                Imgproc.rectangle(s.frame, rect.br(), rect.tl(),
                                        new Scalar(0, 255, 0), 2);
                            }
                        } //else {
                            //s.detectedDartAtLastFrame = false;
                          //  s.learningRate = -1.0;
                        //}
                    }else {
                        s.i++;
                    }
                    Imgproc.rectangle(s.frame, new Point(s.x, s.y), new Point(s.x + s.width, s.y + s.height),
                            new Scalar(255, 255, 255), 1);
                    Imgproc.line(s.frame, new Point(s.width / 2 + s.x, s.y), new Point(s.width / 2 + s.x, s.y + s.height),
                            new Scalar(0, 255, 255));

                    Imgproc.line(s.frame, new Point(0, s.yLineL), new Point(1280, s.yLineR), new Scalar(0, 0, 255), 2);
                    ImageIcon image = new ImageIcon(convertMatToBufferedImage(s.frame));
                    s.jLabel.setIcon(image);
                    s.jLabel.repaint();
                }
            }
        }
        for (int j = 0; j < videoCaptures.length; j++) {
            SettingObject s = settingObjects[j];
            ImageIcon image = new ImageIcon(convertMatToBufferedImage(s.frame));
            s.jLabel.setIcon(image);
            s.jLabel.repaint();
        }
    }

    private void calculateAngle(Point point, int id){
        if(id==0){
            mPointCameraBottom = point;
        }else{
            mPointCameraRight = point;
        }
        if(mPointCameraRight!=null && mPointCameraBottom!= null){
            System.out.println("0: "+ mPointCameraBottom.x*ANGEL_PER_PIXEL +
                    "| 1: " + mPointCameraRight.x*ANGEL_PER_PIXEL);
            int [] coords = mAngleToCoord.calculateCoord(mPointCameraBottom.x*ANGEL_PER_PIXEL, mPointCameraRight.x*ANGEL_PER_PIXEL);
            int [] dartValues = mDartscheibe.getScore(coords[0],coords[1]);
            System.out.println("Treffer: "+dartValues[0]*dartValues[1]);
            System.out.println();
            //abfrage=false;
            mPointCameraRight = null;
            mPointCameraBottom = null;
        }
    }

    //Erkennung der Konturen innerhalb eines SW-Differenzbildes
    private ArrayList<Rect> detectContours(Mat src, Mat outmat, SettingObject s) {
        Mat v = new Mat();
        Mat vv = outmat.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);

        double minArea = 500.0;
        double maxArea = 9000.0;
        int maxAreaIdx;
        Rect r;
        ArrayList<Rect> rect_array = new ArrayList<>();

        for (int idx = 0; idx < contours.size(); idx++) {
            Mat contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(contour);

            if (minArea < contourarea && contourarea < maxArea) {
                //s.learningRate = 0.95;
                // maxArea = contourarea;
                //System.out.println(contourarea);
                maxAreaIdx = idx;
                r = Imgproc.boundingRect(contours.get(maxAreaIdx));
                rect_array.add(r);
                //Hier werden die Konturen vom erkannten Objekt eingezeichnet
                Imgproc.drawContours(src, contours, maxAreaIdx, new Scalar(0, 255, 0), 3, 8, v, 1, new Point(s.x, s.y));
                getOrientation(contours.get(idx),src,s);
                /*if (contourarea < s.lastFrameArea && s.lastFrameArea != 0.0) {
                    System.out.println("DartPfeil erkannt.");
                    /*dartCount++;
                    if(dartCount==6) {
                        abfrage = false;
                    }
                    //
                    getOrientation(contours.get(idx),src,s);
                    s.lastFrameArea = 0.0;
                    s.learningRate = -1.0 ;
                }
                else{
                    s.lastFrameArea = contourarea;
                    s.lastFrameContour = contours.get(idx);
                }*/
            }/*
            else{
                if(s.lastFrameArea != 0.0 && contourarea < s.lastFrameArea){
                    getOrientation(s.lastFrameContour,src,s);
                    s.lastFrameArea = 0.0;
                    s.lastFrameContour = null;
                }
            }*/
        }
        v.release();

        return rect_array;
    }

    //Bestimmung der Orientierung eines erkannten Objektes
    private double getOrientation(MatOfPoint pts_, Mat img, SettingObject s){
        //window(convertMatToBufferedImage(s.frame),"Hallo",1280,720);
        //Construct a buffer used by the pca analysis
        Point[] pts = pts_.toArray();
        int sz = pts.length;
        Mat data_pts =new Mat(sz, 2, CvType.CV_64FC1);
        for (int i = 0; i < data_pts.rows(); ++i)
        {
            data_pts.put(i,0,pts[i].x);
            data_pts.put(i, 1,pts[i].y);
        }

        //Perform PCA analysis
        Mat mean = new Mat();
        Mat eigenvalues = new Mat();
        Mat eigenvectors = new Mat();
        Mat vectors = new Mat();
        Mat covar = new Mat();
        Core.PCACompute(data_pts, mean, vectors);

        //TODO: Recherche zu calcCovarMatrix() und eigen()
        Core.calcCovarMatrix(data_pts, covar, mean, Core.COVAR_NORMAL | Core.COVAR_SCALE | Core.COVAR_ROWS | Core.COVAR_USE_AVG);
        Core.eigen(covar, eigenvalues, eigenvectors);

        //Store the position of the object
        Point pos = new Point(mean.get(0, 0)[0],
                mean.get(0, 1)[0]);

        //Store the eigenvalues and eigenvectors
        Point cntr = new Point(mean.get(0, 0)[0],mean.get(0, 1)[0]);
        Point [] eigen_vecs = new Point[2];
        Double [] eigen_val = new Double[2];
        for (int i = 0; i < 2; ++i)
        {
            eigen_vecs[i]=new Point(eigenvectors.get(i, 0)[0], eigenvectors.get(i, 1)[0]);
            eigen_val[i]=eigenvalues.get(i,0)[0];
        }

        Point p1_ = new Point(eigen_vecs[0].x * eigen_val[0], eigen_vecs[0].y * eigen_val[0]);
        Point p1 = new Point(cntr.x+0.02*p1_.x,cntr.y+0.02*p1_.y);

        Point cntr1 = cntr.clone();
        drawAxis(img, cntr1, p1, 1,s);

        return Math.atan2(eigen_vecs[0].y, eigen_vecs[0].x);
    }


    private void drawAxis(Mat img, Point p, Point q, double scale, SettingObject s){
        double angle;
        double hypotenuse;
        angle = Math.atan2(p.y - q.y, p.x - q.x ); // angle in radians
        hypotenuse = Math.sqrt((p.y - q.y) * (p.y - q.y) + (p.x - q.x) * (p.x - q.x));

        Point q1 = new Point();
        q1.x = (int) (p.x - scale * hypotenuse * Math.cos(angle));
        q1.y = (int) (p.y - scale * hypotenuse * Math.sin(angle));

        p.x += s.x;
        p.y += s.y;
        q1.x += s.x;
        q1.y += s.y;

        Point intersection = intersection(p,q1,new Point(0,s.yLineL), new Point(1280,s.yLineR),img);
        calculateAngle(intersection,s.id);
    }

    //p1 und p2 sind Punkte vom Dartpfeil
    private Point intersection(Point p1, Point p2, Point o1, Point o2, Mat img){
        double mp1 = (p2.y-p1.y)/(p2.x-p1.x);
        double mo2 = (o2.y-o1.y)/(o2.x-o1.x);

        double np1 = p2.y - mp1*p2.x;
        double no2 = o2.y - mo2*o2.x;
        //TODO edit mit Johannes absprechen
        double x = (np1-no2) / (mo2-mp1);
        double y = no2;
        Imgproc.circle(img,new Point(x,y),20,new Scalar(0,0,255),2);
        return new Point(x,y);
    }

    //Hilfs-Funktion zur Umwandlung von Mat zu BufferedImage
    private BufferedImage convertMatToBufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    //Klasse zur Speicherung der Kamera-Settings
    class SettingObject {
        Mat frame, outerBox, fgMaskMOG2, kernel;
        JFrame jFrame;
        JLabel jLabel;
        int x,y,width,height, id, yLineL, yLineR, i, dartCount, cameraID;
        double learningRate;
        boolean detectedDartAtLastFrame;
        double lastFrameArea;
        MatOfPoint lastFrameContour;
        BackgroundSubtractorMOG2 backgroundSubtractorMOG2;

        SettingObject(int x, int y, int width, int height, int id, int yLineL, int yLineR) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.id = id;
            this.i = 0;
            this.learningRate = -1;
            this.dartCount = 0;
            this.yLineL = yLineL;
            this.yLineR = yLineR;
            this.detectedDartAtLastFrame = false;
            this.backgroundSubtractorMOG2 = Video.createBackgroundSubtractorMOG2();
            initializeFrame();
            initializeMat();
        }
        void initializeMat(){
            frame = new Mat();
            fgMaskMOG2 = new Mat();
            kernel = Imgproc.getStructuringElement(MORPH_ELLIPSE, new Size(3, 3));
        }
        void initializeFrame() {
            jFrame = new JFrame("HUMAN MOTION DETECTOR FPS");
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jLabel = new JLabel();
            jFrame.setContentPane(jLabel);
            jFrame.setSize(1320, 800);
            jFrame.setVisible(true);
        }
        void initializeJSONValues(JSONObject jsonObject){
            this.yLineL = (int) jsonObject.get("yLineL");
            this.yLineR = (int) jsonObject.get("yLineR");
            this.y = (int) jsonObject.get("yRect");
            this.height = (int) jsonObject.get("yRectHeight");
            this.cameraID = (int) jsonObject.get("KameraID");
        }
    }
    public void window(BufferedImage img, String text, int x, int y) {
        JFrame frame0 = new JFrame();
        frame0.getContentPane().add(new JPanelOpenCV(img));
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }
}