package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Json {
    private JSONObject jsonObject;
    private String path;

    public Json(String locator){
        if (locator.charAt(0) == '{'){
            jsonObject = new JSONObject(locator);
        }else if (locator.charAt(0) == '<'){
            jsonObject = XML.toJSONObject(locator);;
        }else {
            String path = locator;
            jsonObject = loadJSON(locator);
        }
    }

    public Json(HashMap<String, Object> json){
        jsonObject= new JSONObject(json);
    }

    public static JSONObject loadJSON(String location) {
        try {
            JSONTokener jsonConvert = new JSONTokener(new FileReader(location));
            JSONObject jsonObject = new JSONObject(jsonConvert);
            return jsonObject;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getString(String dotNotationQuery){
        return getValueByDotNotation(dotNotationQuery);
    }
    public Double getDouble(String dotNotationQuery){
        return Double.parseDouble(getValueByDotNotation(dotNotationQuery));
    }

    public int getInt(String dotNotationQuery) {
        return Integer.parseInt(getValueByDotNotation(dotNotationQuery));
    }

    public String getValueByDotNotation(String dotNotationQuery){
        JSONObject json = jsonObject;
        List <String> nodes = new ArrayList<String>(Arrays.asList(dotNotationQuery.split("[.]")));
        String value;
        int contador=0;

        try{
            while (contador < nodes.size()) {
                if (contador < nodes.size() - 1) {
                    String currentNode = nodes.get(contador);

                    if (currentNode.contains("[")) {
                        List<String> key = new ArrayList<String>(Arrays.asList(currentNode.split("[\\[\\]]")));
                        JSONArray jsonArray = json.getJSONArray(key.get(0));
                        json = jsonArray.getJSONObject(Integer.parseInt(key.get(1)));
                    } else {
                        json = json.getJSONObject(currentNode);
                    }
                    contador++;
                } else {
                    value = json.get(nodes.get(contador)).toString();
                    return value;
                }
            }
        }catch (Exception e){
            return "";
        }
        return "";
    }

    public void put(String  key , Map<?,?> value){
        jsonObject.put(key, value);
    }
    public void put(String  key , String value){
        jsonObject.put(key, value);
    }
    public void put(String  key , int value){
        jsonObject.put(key, value);
    }
    public void put(String  key , long value){
        jsonObject.put(key, value);
    }

    public void put(String  key , Object value){
        jsonObject.put(key, value);
    }

    public JSONObject getJSONObject(String dotNotationQuery){
        JSONObject json = jsonObject;
        List <String> nodes = new ArrayList<String>(Arrays.asList(dotNotationQuery.split("[.]")));
        JSONObject object;
        int contador=0;
        String currentNode;

        while(contador < nodes.size()) {
            currentNode = nodes.get(contador);
            if(contador < nodes.size()-1) {
                if(currentNode.contains("[")){
                    List<String> key = new ArrayList<String>(Arrays.asList(currentNode.split("[\\[\\]]")));
                    JSONArray jsonArray = json.getJSONArray(key.get(0));
                    json = jsonArray.getJSONObject(Integer.parseInt(key.get(1)));
                }else {
                    json = json.getJSONObject(currentNode);
                }
                contador++;
            }else {
                if(currentNode.contains("[")){
                    List<String> key = new ArrayList<String>(Arrays.asList(currentNode.split("[\\[\\]]")));
                    System.out.println(key.get(1));
                    JSONArray jsonArray = json.getJSONArray(key.get(0));
                    json = jsonArray.getJSONObject(Integer.parseInt(key.get(1)));
                }else {
                    object = json.getJSONObject(nodes.get(contador));
                    return object;
                }
            }
        }
        return null;
    }

    public JSONArray getJSONArray(String dotNotationQuery){
        JSONObject json = jsonObject;
        List <String> nodes = new ArrayList<String>(Arrays.asList(dotNotationQuery.split("[.]")));
        JSONArray array;
        int contador=0;

        while(contador < nodes.size()) {
            if(contador < nodes.size()-1) {
                String currentNode = nodes.get(contador);

                if(currentNode.contains("[")){
                    List<String> key = new ArrayList<String>(Arrays.asList(currentNode.split("[\\[\\]]")));
                    JSONArray jsonArray = json.getJSONArray(key.get(0));
                    json = jsonArray.getJSONObject(Integer.parseInt(key.get(1)));
                }else {
                    json = json.getJSONObject(currentNode);
                }
                contador++;
            }else {
                array = json.getJSONArray(nodes.get(contador));
                return array;
            }
        }
        return null;
    }

    public String toString(){
        return jsonObject.toString(3);
    }
    public JSONObject toJSONObject(){
        return jsonObject;
    }

    public void save(){
        try (FileWriter file = new FileWriter(path)) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(jsonObject.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String formatStringToJson(String jsonString){
        JSONObject json = new JSONObject(jsonString);
        return json.toString(1);
    }
}