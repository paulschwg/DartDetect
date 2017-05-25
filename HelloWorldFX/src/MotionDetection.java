import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;

public class MotionDetection {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    //TODO: Start parallel Motion Detection of both cameras
    //TODO: Calculate the Angle to the Pixel (Rect.width()/2)
    //TODO: Calculate the crossing point of the two lines
    //TODO: Calculate the pixel value for 1700 x 1700 coordinate system

    public static void main(String[] args) {
        MotionDetection motionDetection = new MotionDetection();
        motionDetection.runDual();
    }

    public void runDual(){
        //Camera Objects
        //VideoCapture [] videoCaptures = {new VideoCapture(0),new VideoCapture(1)};
        //SettingObject [] settingObjects = {new SettingObject(), new SettingObject()};

        VideoCapture [] videoCaptures = {new VideoCapture(0)};
        SettingObject [] settingObjects = {new SettingObject()};

        while (true) {
            for (int j = 0; j < videoCaptures.length; j++){

                videoCaptures[j].set(CV_CAP_PROP_FRAME_WIDTH,1280);
                videoCaptures[j].set(CV_CAP_PROP_FRAME_HEIGHT,720);
                SettingObject s = settingObjects[j];

                if (videoCaptures[j].read(s.frame)) {
                    Imgproc.resize(s.frame, s.frame, s.sz);
                    s.mat = s.frame.clone();
                    //TODO: Define mat size as cropped
                    //s.outerBox = new Mat(s.frame.size(), CvType.CV_8UC1);
                    s.outerBox = new Mat(s.frame,new Rect(s.x,s.y,s.width,s.height));
                    //displayImage(Mat2bufferedImage(s.outerBox));


                    Imgproc.cvtColor(s.outerBox, s.outerBox, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.GaussianBlur(s.outerBox, s.outerBox, new Size(3, 3), 0);

                    if (s.i == 0) {
                        s.jFrame.setSize(s.frame.width(), s.frame.height());
                        s.tempon_frame = new Mat(s.outerBox.size(), CvType.CV_8UC1);
                        s.diff_frame = s.outerBox.clone();
                    }

                    if (s.i == 1) {
                        //Subtrahiert outerBox von tempon_frame und speichert differenz in diff_frsme
                        Core.subtract(s.outerBox, s.tempon_frame, s.diff_frame);
                        Imgproc.adaptiveThreshold(s.diff_frame, s.diff_frame, 255,
                                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                                Imgproc.THRESH_BINARY_INV, 5, 2);
                        s.array = detection_contours(s.mat,s.diff_frame,s);
                        if (s.array.size() > 0) {
                            //System.out.println("Motion Detected!");

                            Iterator<Rect> it2 = s.array.iterator();
                            while (it2.hasNext()) {
                                Rect obj = it2.next();
                                //Hier werden die umrandenden Rechtecke eingezeichnet
                                obj.x += s.x;
                                obj.y += +s.y;
                                Imgproc.rectangle(s.mat, obj.br(), obj.tl(),
                                        new Scalar(0, 255, 0), 1);
                            }
                        }
                        Imgproc.rectangle(s.mat, new Point(35,200), new Point(1225,300),
                                new Scalar(255, 255, 255), 1);
                        Imgproc.line(s.mat,new Point(630,200), new Point(630,300),
                                new Scalar(0,255,255));
                    }

                    s.i = 1;

                    ImageIcon image = new ImageIcon(Mat2bufferedImage(s.mat));
                    s.jLabel.setIcon(image);
                    s.jLabel.repaint();
                    s.tempon_frame = s.outerBox.clone();
                }
            }
        }
    }
    //Kovertiert eine Matrix zu einem .jpg Bild
    public BufferedImage Mat2bufferedImage(Mat image) {
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
    public void displayImage(Image img2)
    {
        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
        ImageIcon icon=new ImageIcon(img2);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    //Erkennung der Konturen des Objektes
    public ArrayList<Rect> detection_contours(Mat imag, Mat outmat, SettingObject s) {
        Mat v = new Mat();
        Mat vv = outmat.clone();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 100;
        int maxAreaIdx = -1;
        Rect r = null;
        ArrayList<Rect> rect_array = new ArrayList<Rect>();

        for (int idx = 0; idx < contours.size(); idx++) {
            Mat contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(contour);
            if (contourarea > maxArea) {
                // maxArea = contourarea;
                maxAreaIdx = idx;
                r = Imgproc.boundingRect(contours.get(maxAreaIdx));
                rect_array.add(r);
                //Hier werden die Konturen vom erkannten Objekt eingezeichnet
                Imgproc.drawContours(imag, contours, maxAreaIdx, new Scalar(0,255, 255),1,8,v,1,new Point(s.x,s.y));
            }

        }

        v.release();

        return rect_array;
    }
    //Klasse zur Speicherung der Kamera-Settings
    class SettingObject {
        Mat mat, frame, outerBox, diff_frame, tempon_frame;
        ArrayList<Rect> array;
        Size sz;
        int i;
        JFrame jFrame;
        JLabel jLabel;
        int x,y,width,height;

        public SettingObject() {
            sz = new Size(1280, 720);
            i = 0;
            x = 35;
            y = 200;
            width = 1190;
            height = 100;
            initializeFrame();
            initializeMat();
        }
        void initializeMat(){
            mat = null;
            frame = new Mat();
            diff_frame = null;
            tempon_frame = null;
        }
        void initializeFrame() {
            jFrame = new JFrame("HUMAN MOTION DETECTOR FPS");
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jLabel = new JLabel();
            jFrame.setContentPane(jLabel);
            jFrame.setSize(1280, 720);
            jFrame.setVisible(true);
        }
    }
}