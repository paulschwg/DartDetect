package hardware;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
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
    //TODO: Calculate the Angle to a specific Pixel (maybe Rect.width()/2 -> Spitze des Dartpfeiles)
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
        Mat temp = new Mat();
        boolean last_throw_motion = false;

        VideoCapture [] videoCaptures = {new VideoCapture(0)};
        SettingObject [] settingObjects = {new SettingObject(5,170,1265,100)};

        while (true) {
            for (int j = 0; j < videoCaptures.length; j++){

                videoCaptures[j].set(CV_CAP_PROP_FRAME_WIDTH,1280);
                videoCaptures[j].set(CV_CAP_PROP_FRAME_HEIGHT,720);
                SettingObject s = settingObjects[j];

                if (videoCaptures[j].read(s.frame)) {
                    Imgproc.resize(s.frame, s.frame, s.sz);
                    s.mat = s.frame.clone();
                    //s.outerBox = new Mat(s.frame.size(), CvType.CV_8UC1);
                    s.outerBox = new Mat(s.frame,new Rect(s.x,s.y,s.width,s.height));

                    Imgproc.cvtColor(s.outerBox, s.outerBox, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.GaussianBlur(s.outerBox, s.outerBox, new Size(3, 3), 0);

                    if (s.i == 0) {
                        s.jFrame.setSize(s.frame.width(), s.frame.height());
                        s.tempon_frame = new Mat(s.outerBox.size(), CvType.CV_8UC1);
                        s.diff_frame = s.outerBox.clone();
                        temp = s.outerBox.clone();
                    }

                    if (s.i == 1) {
                        //Subtrahiert outerBox von tempon_frame und speichert differenz in diff_frsme
                        Core.subtract(s.outerBox, s.tempon_frame, s.diff_frame);
                        Imgproc.adaptiveThreshold(s.diff_frame, s.diff_frame, 255,
                                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                                Imgproc.THRESH_BINARY_INV, 5, 2);
                        s.array = detection_contours(s.mat,s.diff_frame,s,false);
                        if (s.array.size() > 0) {
                            last_throw_motion = true;
                            System.out.println("Motion Detected!");

                            /*Iterator<Rect> it2 = s.array.iterator();
                            while (it2.hasNext()) {
                                Rect obj = it2.next();
                                //Hier werden die umrandenden Rechtecke eingezeichnet
                                obj.x += s.x;
                                obj.y += +s.y;
                                Imgproc.rectangle(s.mat, obj.br(), obj.tl(),
                                        new Scalar(0, 255, 0), 1);
                            }*/
                        }
                        else{ if(last_throw_motion){
                            System.out.println("Motion subtraction detected!");
                            Core.subtract(s.outerBox, temp, s.diff_frame);
                            window(MatToBufferedImage(temp.clone()),"Testi",0,0);
                            window(MatToBufferedImage(new Mat(s.frame,new Rect(s.x,s.y,s.width,s.height))),"TestB",0,0);
                            Imgproc.adaptiveThreshold(s.diff_frame, s.diff_frame, 255,
                                    Imgproc.ADAPTIVE_THRESH_MEAN_C,
                                    Imgproc.THRESH_BINARY_INV, 5, 2);
                            window(MatToBufferedImage(s.diff_frame),"Testi2",0,0);
                            s.array = detection_contours(s.mat,s.diff_frame,s,true);
                            if (s.array.size() > 0) {
                                last_throw_motion = true;
                                System.out.println("Motion sub Detected!");

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
                            temp = s.outerBox.clone();
                            last_throw_motion = false;
                        }
                        }
                        Imgproc.rectangle(s.mat, new Point(s.x,s.y), new Point(s.x+s.width,s.y+s.height),
                                new Scalar(255, 255, 255), 1);
                        Imgproc.line(s.mat,new Point(s.width/2+s.x,s.y), new Point(s.width/2+s.x,s.y+s.height),
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
    //Optional: Nur zu Testzwecken zur Ausgabe eines Bildes
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
    public ArrayList<Rect> detection_contours(Mat imag, Mat outmat, SettingObject s, boolean draw) {
        Mat v = new Mat();
        Mat vv = outmat.clone();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 150;
        int maxAreaIdx = -1;
        Rect r = null;
        ArrayList<Rect> rect_array = new ArrayList<Rect>();

        for (int idx = 0; idx < contours.size(); idx++) {
            Mat contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(contour);
            if (contourarea > maxArea) {

                    /*for(int i = 0; i < contour.rows(); i++){
                        for(int e = 0; e < contour.row(i).cols(); e++){
                            double[] values = contour.get(i,e);
                            System.out.println("Y: "+ i + " X: "+ e + " Erster Wert: " + values[0] + " Zweiter Wert: "+ values[1]);
                        }
                    }*/


                // maxArea = contourarea;
                maxAreaIdx = idx;
                r = Imgproc.boundingRect(contours.get(maxAreaIdx));
                /*
                //TODO houghlines with r

                Mat gray = new Mat(imag.clone(),r);
                //window(MatToBufferedImage(gray),"Hallo",0,0);
                Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.blur(gray, gray, new Size(3, 3));
                //window(MatToBufferedImage(gray),"Hallo",0,0);

                // detect the edges
                Mat edges = new Mat();
                int lowThreshold = 100;
                int ratio = 3;
                Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);

                Mat lines = new Mat();
                Imgproc.HoughLinesP(gray, lines, 1, Math.PI / 180, 100, 20, 30);

                for(int i = 0; i < lines.cols(); i++) {
                    double[] val = lines.get(0, i);
                    if(val!=null) {
                        Imgproc.line(imag, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 255, 0), 2);
                    }
                }*/

                rect_array.add(r);
                //Hier werden die Konturen vom erkannten Objekt eingezeichnet
                if(draw) {
                    //Imgproc.drawContours(imag, contours, maxAreaIdx, new Scalar(0, 255, 255), 1, 8, v, 1, new Point(s.x, s.y));
                    //window(MatToBufferedImage(imag),"Test",0,0);
                }
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

        public SettingObject(int x, int y, int width, int height) {
            sz = new Size(1280, 720);
            i = 0;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
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
    public void window(BufferedImage img, String text, int x, int y) {
        JFrame frame0 = new JFrame();
        frame0.getContentPane().add(new JPanelOpenCV(img));
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }
    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
}