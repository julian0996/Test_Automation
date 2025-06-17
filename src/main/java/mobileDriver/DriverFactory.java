package mobileDriver;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    /**
     * Este método se encarga de instanciar y cargar un driver específico (por ahora solo Android),
     * utilizando reflexión para cargar dinámicamente la clase correspondiente según el tipo de driver.
     *
     * @param driverType Tipo de driver (por ejemplo, "android")
     * @param dc DesiredCapabilities que se usarán para configurar el driver
     * @param deviceUsed Identificador del dispositivo que se utilizará
     */
    public static void setDriver(String driverType, DesiredCapabilities dc, String deviceUsed) {
        Map<String, String> map = new HashMap<>();
        map.put("android", "mobileDriver.AndroidLoader");
        try {
            Class<DriverLoaderInterface> theClass = (Class<DriverLoaderInterface>) Class.forName(map.get(driverType.toLowerCase()));
            DriverLoaderInterface driverLoader = theClass.getDeclaredConstructor().newInstance();
            driverLoader.loadDriver(dc, deviceUsed);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
