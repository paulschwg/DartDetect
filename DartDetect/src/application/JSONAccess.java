package application;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONAccess {
    public static void storeJSON(JSONObject obj){//später JSON
        try (FileWriter file = new FileWriter("jsonCalibration")) {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public static JSONObject getJSON() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("jsonCalibration"));
            return (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
            createDefaultJSONObject();
            return getJSON();
        }
    }
    private static void createDefaultJSONObject(){
        JSONObject jsonObject = new JSONObject();

        JSONObject jsonObjectBC = new JSONObject();
        jsonObjectBC.put("yLineL",500);
        jsonObjectBC.put("yLineR",500);
        jsonObjectBC.put("yRect",300);
        jsonObjectBC.put("yRectHeight",150);
        jsonObjectBC.put("KameraID",0);

        JSONObject jsonObjectRC = new JSONObject();
        jsonObjectRC.put("yLineL",500);
        jsonObjectRC.put("yLineR",500);
        jsonObjectRC.put("yRect",300);
        jsonObjectRC.put("yRectHeight",150);
        jsonObjectRC.put("KameraID",0);

        jsonObject.put("KameraBC",jsonObjectBC);
        jsonObject.put("KameraRC",jsonObjectRC);
        JSONAccess.storeJSON(jsonObject);
    }
}
