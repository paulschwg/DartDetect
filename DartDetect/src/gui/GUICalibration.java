package gui;
import application.JSONAccess;
import org.json.simple.JSONObject;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;

public class GUICalibration implements MouseListener, KeyListener, WindowListener{

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int IMAGE_VIEW_Y = 80;
    public static final int COMPONENTS_HEIGHT = 30;

    int mRectY = 300;
    int mRectHeight = 200;
    int mLineYLeft = 600;
    int mLineYRight = 600;
    int mRectThickness = 1;
    int mLineThickness =  5;

    JFrame jFrame;
    JLabel jLabel;
    JPanel contentPane;
    Mat frame;

    JTextPane jTextAreaBC;
    JTextPane jTextAreaRC;

    //COMBO-Box Daten: Breite: 200 px; Höhe: 30px
    JComboBox jComboBoxBC;
    JComboBox jComboBoxRC;

    //Checkbox Daten: Breite: 50 px; Höhe: 30px
    JCheckBox jCheckBoxBC;
    JCheckBox jCheckBoxRC;

    //Textarea Daten: Breite: width px; Höhe: 30px
    JTextPane rectHeight;
    JTextPane lineYLeft;
    JTextPane lineYRight;

    //Textfield Daten: Breite: 100 px; Höhe: 30px
    JTextField textFieldRectHeight;
    JTextField textFieldY_LineLeft;
    JTextField textFieldY_LineRight;

    JButton jButtonChange;
    JButton jButtonSave;

    JSONObject jsonObject;

    ArrayList <VideoCapture> videoCaptures = new ArrayList<>();

    boolean abfrage;

    int currentCamera;

    private ArrayList <String> kameras = new ArrayList<>();


    public static void main(String[] args) {
        GUICalibration GUICalibration = new GUICalibration();
        GUICalibration.show();
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
        jLabel.setLocation(0,IMAGE_VIEW_Y);
        jLabel.setFocusable(true);
    }

    private JComboBox initJComboBoxCameras(int x, int y, int id){
        // Array für unsere JComboBo
        for(int i = 0; i < kameras.size(); i++){
            videoCaptures.add(new VideoCapture(i));
        }
        //JComboBox mit Bundesländer-Einträgen wird erstellt
        JComboBox jComboBox = new JComboBox(kameras.toArray());
        if(id < kameras.size())
        jComboBox.setSelectedIndex(id);
        jComboBox.setSize(200,COMPONENTS_HEIGHT);
        jComboBox.setLocation(x,y);
        return jComboBox;
    }

    private JCheckBox initJCheckBox(int x, int y, boolean selected){
        JCheckBox jCheckBox = new JCheckBox();
        jCheckBox.setSize(80,COMPONENTS_HEIGHT);
        jCheckBox.setText("Anzeigen");
        jCheckBox.setSelected(selected);
        jCheckBox.setLocation(x,y);
        if(selected) {
            initializeJSONValues((JSONObject) jsonObject.get("KameraBC"));
            //TODO init cam
        }
        return jCheckBox;
    }

    private JTextField initJTextField(int x, int y){
        JTextField jTextField = new JTextField();
        jTextField.setLocation(x,y);
        jTextField.setSize(100,COMPONENTS_HEIGHT);
        return jTextField;
    }

    private JTextPane initJTextPane(int x, int y, int width, String text){
        JTextPane jTextPane = new JTextPane();
        jTextPane.setText(text);
        jTextPane.setLocation(x,y);
        jTextPane.setSize(width,COMPONENTS_HEIGHT);
        StyledDocument doc = jTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center,StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0,doc.getLength(),center, false);
        return jTextPane;
    }

    private void addListener(JLabel jLabel){

        jLabel.addKeyListener(this);
        jLabel.addMouseListener(this);
    }

    private void initJFrame(){
        frame = new Mat();
        jFrame = new JFrame("Kalibrierungsmaske");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setAlwaysOnTop(true);
        jFrame.setResizable(false);
    }

    private void fillKameraList() {
        int number = 0;
        while (true) {
            VideoCapture cap = new VideoCapture(number);
            if (!cap.isOpened()) {
                break;
            }
            else {
                number++;
                cap.release();
            }
        }
        for(int i = 1; i <= number; i++){
            kameras.add("Kamera "+i);
        }
    }

    private void initializeJSONValues(JSONObject jsonObject){
        mLineYLeft = (int) (long) jsonObject.get("yLineL");
        mLineYRight = (int) (long) jsonObject.get("yLineR");
        mRectY = (int) (long) jsonObject.get("yRect");
        mRectHeight = (int) (long) jsonObject.get("yRectHeight");
    }

    private int getJSONKameraID(JSONObject jsonObject){
        return (int) (long) jsonObject.get("KameraID");

    }

    private void changeTextFieldValues(){
        textFieldY_LineLeft.setText(Integer.toString(mLineYLeft));
        textFieldY_LineRight.setText(Integer.toString(mLineYRight));
        textFieldRectHeight.setText(Integer.toString(mRectHeight));
    }

    //BC bottom camera | RC right camera
    public void show(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        fillKameraList();
        initJFrame();
        //IMAGE_VIEW vorher setzen
        initJLabel();
        addListener(jLabel);

        initializeContentPanel();

        contentPane.setFocusable(true);
        contentPane.requestFocusInWindow();
        contentPane.addKeyListener(this);

        jsonObject = JSONAccess.getJSON();

        //JButton buttonBC = initializeJButton(0,0,100,30,"Hello World");

        //Y-Abstand zwischen den zwei Reihen: 10 px

        //Textarea Daten: Breite: 110 px; Höhe: 30px
        jTextAreaBC = initJTextPane(0,0,110,"Untere Kamera:");
        jTextAreaRC = initJTextPane(0,40,110,"Rechte Kamera");

        //COMBO-Box Daten: Breite: 200 px; Höhe: 30px
        jComboBoxBC = initJComboBoxCameras(120,0,getJSONKameraID((JSONObject)jsonObject.get("KameraBC")));
        currentCamera = getJSONKameraID((JSONObject)jsonObject.get("KameraBC"));
        jComboBoxBC.addActionListener(e -> {
            if(jCheckBoxBC.isSelected())
                currentCamera=jComboBoxBC.getSelectedIndex();
        });
        jComboBoxRC = initJComboBoxCameras(120,40,getJSONKameraID((JSONObject)jsonObject.get("KameraRC")));
        jComboBoxRC.addActionListener(e -> {
            if(jCheckBoxRC.isSelected())
                currentCamera=jComboBoxRC.getSelectedIndex();
        });


        //Checkbox Daten: Breite: 50 px; Höhe: 30px
        jCheckBoxBC = initJCheckBox(330,0,true);
        jCheckBoxBC.addActionListener(e -> {
            //abfrage = false;
            jCheckBoxRC.setSelected(false);
            currentCamera = jComboBoxBC.getSelectedIndex();
            jsonObject = JSONAccess.getJSON();
            initializeJSONValues((JSONObject) jsonObject.get("KameraBC"));
            changeTextFieldValues();
            contentPane.requestFocusInWindow();
            //abfrage = true;
            //runCamera();

        });
        jCheckBoxRC = initJCheckBox(330, 40,false);
        jCheckBoxRC.addActionListener(e -> {
            //abfrage = false;
            jCheckBoxBC.setSelected(false);
            currentCamera = jComboBoxRC.getSelectedIndex();
            jsonObject = JSONAccess.getJSON();
            initializeJSONValues((JSONObject) jsonObject.get("KameraRC"));
            changeTextFieldValues();
            contentPane.requestFocusInWindow();
            //abfrage = true;
            //runCamera();

        });

        //Textarea Daten: Breite: width px; Höhe: 30px
        rectHeight = initJTextPane(430,0,120,"Rechteckshöhe:");
        lineYLeft = initJTextPane(660,0,120,"Linke yLinienhöhe:");
        lineYRight = initJTextPane(660,40,120,"Rechte yLinienhöhe:");

        //Textfield Daten: Breite: 100 px; Höhe: 30px
        textFieldRectHeight = initJTextField(550,0);
        textFieldY_LineLeft = initJTextField(790,0);
        textFieldY_LineRight = initJTextField(790,40);
        changeTextFieldValues();

        jButtonChange = initializeJButton(900,0,100,30,"Ändern");
        jButtonChange.addActionListener(e -> {
            if(isNumeric(textFieldRectHeight.getText()) && isNumeric(textFieldY_LineLeft.getText()) && isNumeric(textFieldY_LineRight.getText())){
                //TODO Prüfung ob Einagbe korrekt
                mRectHeight = Integer.parseInt(textFieldRectHeight.getText());
                mLineYLeft = Integer.parseInt(textFieldY_LineLeft.getText());
                mLineYRight = Integer.parseInt(textFieldY_LineRight.getText());
                contentPane.requestFocusInWindow();
            }

        });
        jButtonSave = initializeJButton(1275,40,100,30,"Speichern");
        jButtonSave.addActionListener(e -> {
            if(jCheckBoxBC.isSelected()){
                JSONObject jsonObjectBC = new JSONObject();
                jsonObjectBC.put("yLineL", mLineYLeft);
                mLineYLeft = (int) jsonObjectBC.get("yLineL");
                jsonObjectBC.put("yLineR",mLineYRight);
                jsonObjectBC.put("yRect",mRectY);
                jsonObjectBC.put("yRectHeight",mRectHeight);
                jsonObjectBC.put("KameraID",jComboBoxBC.getSelectedIndex());
                jsonObject.put("KameraBC",jsonObjectBC);
            }
            else{
                JSONObject jsonObjectRC = new JSONObject();
                jsonObjectRC.put("yLineL",mLineYLeft);
                jsonObjectRC.put("yLineR",mLineYRight);
                jsonObjectRC.put("yRect",mRectY);
                jsonObjectRC.put("yRectHeight",mRectHeight);
                jsonObjectRC.put("KameraID",jComboBoxRC.getSelectedIndex());
                jsonObject.put("KameraRC",jsonObjectRC);
            }
            contentPane.requestFocusInWindow();
            JSONAccess.storeJSON(jsonObject);
        });

        contentPane.add(jLabel);

        contentPane.add(jTextAreaBC);
        contentPane.add(jTextAreaRC);

        contentPane.add(jComboBoxBC);
        contentPane.add(jComboBoxRC);

        contentPane.add(jCheckBoxBC);
        contentPane.add(jCheckBoxRC);

        contentPane.add(rectHeight);
        contentPane.add(lineYLeft);
        contentPane.add(lineYRight);

        contentPane.add(textFieldRectHeight);
        contentPane.add(textFieldY_LineLeft);
        contentPane.add(textFieldY_LineRight);

        contentPane.add(jButtonChange);
        contentPane.add(jButtonSave);

        contentPane.setFocusable(true);

        jFrame.addKeyListener(this);
        jFrame.addWindowListener(this);

        //Fenster hat 15pt breite und 40 pt hoehe, neben gewuenschter flaeche
        jFrame.setContentPane(contentPane);
        jFrame.setSize(1385, 850);
        jFrame.setLocationByPlatform(true);
        jFrame.setVisible(true);
        jFrame.requestFocus();
        abfrage = true;
        runCamera();
    }
    private void runCamera(){
            while (abfrage) {
                VideoCapture videoCapture = videoCaptures.get(currentCamera);
                videoCapture.set(CV_CAP_PROP_FRAME_WIDTH, 1280);
                videoCapture.set(CV_CAP_PROP_FRAME_HEIGHT, 720);

                if (videoCapture.read(frame)) {
                    Imgproc.rectangle(frame, new Point(0, mRectY), new Point(SCREEN_WIDTH, mRectY + mRectHeight), new Scalar(255, 0, 0), mRectThickness);
                    Imgproc.line(frame, new Point(0, mLineYLeft), new Point(SCREEN_WIDTH, mLineYRight), new Scalar(0, 0, 255), mLineThickness);
                    Imgproc.line(frame,new Point(1280/2,mRectY),new Point(1280/2,mRectY+mRectHeight), new Scalar(50,244,255));
                    ImageIcon image = new ImageIcon(convertMatToBufferedImage(frame));
                    jLabel.setIcon(image);
                    jLabel.repaint();
                }
            }
            //videoCapture.release();
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

    private int getKameraID(String text){
        return Integer.parseInt(text.split(" ")[1]);
    }

    private boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getY());
        System.out.println(jFrame.getY());
        if(((mLineYLeft+mLineYRight)/2) - 20 < e.getY() && e.getY() < ((mLineYLeft+mLineYRight)/2)+20){
            mLineThickness = 5;
            mRectThickness = 1;
        }else {
            if(mRectY <= e.getY() && e.getY() <= + mRectY+mRectHeight){
                mLineThickness = 1;
                mRectThickness = 5;
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
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if(mRectThickness==5){
                if(mRectY >= + 1)
                mRectY--;
            }
            else{
                if(Math.min(mLineYLeft,mLineYRight) >= + 1) {
                    mLineYLeft--;
                    mLineYRight--;
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if(mRectThickness==5){
                if(mRectY + mRectHeight < 719)
                mRectY++;
            }
            else{
                if(Math.max(mLineYLeft,mLineYRight) < 719) {
                    mLineYLeft++;
                    mLineYRight++;
                }
            }
        }

        changeTextFieldValues();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        for(VideoCapture videoCapture : videoCaptures){
            videoCapture.release();
        }
        abfrage = false;
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}