package hardware;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;

public class FXTest implements MouseListener, KeyListener{

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    int mRectY = 300;
    int mRectHeight = 200;
    int mLineY = 600;
    int mRectThickness = 1;
    int mLineThickness =  10;

    JFrame jFrame;
    JLabel jLabel;
    JPanel contentPane;
    Mat frame;


    public static void main(String[] args) {
        FXTest fxTest = new FXTest();
        fxTest.show();
    }

    private void initializeContentPanel(){
        contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);
    }

    private JButton initializeJButton(int x, int y, int width, int height, String text){
        JButton button = new JButton(text);
        button.setSize(width, height);
        button.setLocation(x, y);
        return button;
    }

    private void initJLabel(){
        jLabel = new JLabel();
        jLabel.setSize(1280,720);
        jLabel.setLocation(0,40);
        jLabel.setFocusable(true);
    }

    private void addListener(JLabel jLabel){
        jLabel.addKeyListener(this);
        jLabel.addMouseListener(this);
    }

    private void initJFrame(){
        frame = new Mat();
        jFrame = new JFrame("HUMAN MOTION DETECTOR FPS");
        jFrame.setResizable(false);
    }


    public void show(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        initJFrame();
        initJLabel();
        initializeContentPanel();
        JButton button = initializeJButton(0,0,100,30,"Hello World");
        addListener(jLabel);

        contentPane.setFocusable(true);
        contentPane.add(jLabel);
        contentPane.add(button);

        //Fenster hat 15pt breite und 40 pt hoehe, neben gewuenschter flaeche
        jFrame.setContentPane(jLabel);
        jFrame.setSize(1385, 850);
        jFrame.setLocationByPlatform(true);
        jFrame.setVisible(true);

        VideoCapture videoCapture = new VideoCapture(0);
        videoCapture.set(CV_CAP_PROP_FRAME_WIDTH,1280);
        videoCapture.set(CV_CAP_PROP_FRAME_HEIGHT,720);
        while(true) {
            if (videoCapture.read(frame)) {
                Imgproc.rectangle(frame, new Point(0, mRectY), new Point(SCREEN_WIDTH, mRectY + mRectHeight), new Scalar(255, 0, 0), mRectThickness);
                Imgproc.line(frame, new Point(0, mLineY), new Point(SCREEN_WIDTH, mLineY), new Scalar(0, 0, 255), mLineThickness);
                ImageIcon image = new ImageIcon(convertMatToBufferedImage(frame));
                jLabel.setIcon(image);
                jLabel.repaint();
            }
        }
    }

    private static BufferedImage convertMatToBufferedImage(Mat image) {
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


    @Override
    public void mouseClicked(MouseEvent e) {

        if(jFrame.getY() + mLineY - 10 < e.getY() && e.getY() < jFrame.getY()+mLineY+10){
            mLineThickness = 10;
            mRectThickness = 1;
        }else {
            if(jFrame.getY() + mRectY <= e.getY() && e.getY() <= jFrame.getY() + mRectY+mRectHeight){
                mLineThickness = 1;
                mRectThickness = 10;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if(mRectThickness==10){
                if(mRectY >= 1)
                mRectY--;
            }
            else{
                if(mLineY >= 1)
                mLineY--;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if(mRectThickness==10){
                if(mRectY + mRectHeight < 719)
                mRectY++;
            }
            else{
                if(mLineY < 719)
                mLineY++;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
}