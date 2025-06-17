package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class DeviceLoader {

    public static HashMap<String, String> devicesJsonReader(String selectedDevice){
        Object ob = null;

        try {
            ob = new JSONParser().parse(new FileReader("./src/test/resources/devices.json"));
        } catch (Exception e){
            System.out.println("Error al leer o parsear el archivo JSON:");
            e.printStackTrace();
            return new HashMap<>(); // Retorna un mapa vacío para evitar NPE
        }

        if (!(ob instanceof JSONObject)) {
            System.out.println("El archivo JSON no tiene el formato correcto.");
            return new HashMap<>();
        }

        JSONObject js = (JSONObject) ob;
        JSONObject detail = (JSONObject) js.get(selectedDevice);

        if (detail == null) {
            System.out.println("No se encontró el dispositivo: " + selectedDevice);
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();
        detail.forEach((a, b) -> map.put((String) a, (String) b));

        return (HashMap<String, String>) map;
    }

}