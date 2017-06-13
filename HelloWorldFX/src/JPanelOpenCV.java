import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_BACKLIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;

public class JPanelOpenCV extends JPanel{

    BufferedImage image;

    public static void main (String args[]) throws InterruptedException{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        JPanelOpenCV t = new JPanelOpenCV();

        VideoCapture camera1 = new VideoCapture(0);
        //Optionen für die Kameraauflösung
        camera1.set(CV_CAP_PROP_FRAME_WIDTH,4000);
        camera1.set(CV_CAP_PROP_FRAME_HEIGHT,4000);

        VideoCapture camera2 = new VideoCapture(1);
        camera2.set(CV_CAP_PROP_FRAME_WIDTH,4000);
        camera2.set(CV_CAP_PROP_FRAME_HEIGHT,4000);
        VideoCapture [] cameras = new VideoCapture[2];

        cameras[0] = camera1;
        cameras[1] = camera2;

        t.captureImage(cameras,t);
    }
    //Aufnahme und Ausgabe von Bildern aller im cameras Array abgespeichterter WebCams
    public void captureImage(VideoCapture [] cameras, JPanelOpenCV t){
        for(VideoCapture camera: cameras) {
            Mat frame = new Mat();
            camera.read(frame);

            if (!camera.isOpened()) {
                System.out.println("Error");
            } else {
                while (true) {

                    if (camera.read(frame)) {
                        //Beispiel zum Ausschneiden bestimmter Elemente aus einem Bild (Zuschneiden):
                        /*Rect roi = new Rect(0, 0, frame.width()/2, frame.height()/2);
                        Mat cropped = new Mat(frame, roi);
                        BufferedImage image = t.MatToBufferedImage(cropped);*/
                        Mat gray = new Mat();
                        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                        Imgproc.blur(gray, gray, new Size(3, 3));

                        // detect the edges
                        Mat edges = new Mat();
                        int lowThreshold = 50;
                        int ratio = 3;
                        Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);

                        Mat lines = new Mat();
                        Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 50, 50, 30);

                        for(int i = 0; i < lines.cols(); i++) {
                            double[] val = lines.get(0, i);
                            Imgproc.line(frame, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
                        }

                        BufferedImage image = t.MatToBufferedImage(frame);

                        t.window(image, "Original Image", 0, 0);

                        //t.window(t.grayscale(image), "Processed Image", 40, 60);

                        //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                        break;
                    }
                }
            }
            camera.release();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public JPanelOpenCV() {
    }

    public JPanelOpenCV(BufferedImage img) {
        image = img;
    }

    //Show image on window
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

    //Load an image
    public BufferedImage loadImage(String file) {
        BufferedImage img;

        try {
            File input = new File(file);
            img = ImageIO.read(input);

            return img;
        } catch (Exception e) {
            System.out.println("erro");
        }

        return null;
    }

    //Save an image
    public void saveImage(BufferedImage img) {
        try {
            File outputfile = new File("Images/new.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    //Grayscale filter
    public BufferedImage grayscale(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));

                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);

                Color newColor =
                        new Color(
                                red + green + blue,
                                red + green + blue,
                                red + green + blue);

                img.setRGB(j, i, newColor.getRGB());
            }
        }

        return img;
    }

}