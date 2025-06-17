package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import objects.BaseScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class AutoTools {
    private static Properties properties = null;
    private static AppiumDriverLocalService server = null;
    private static URL appiumServerURL = null;
    private static AppiumDriver mobileDriver = null;
    private static Boolean isAndroid = null;
    static String platformName = null;
    private static final String PATH_CONFIG_PROPERTIES = "src/test/resources/Config.properties";
    private static final Logger LOGGER = LogManager.getLogger(AutoTools.class);

    /**
     * Carga los valores de configuración desde un archivo de propiedades ubicado en
     * "src/test/resources/Config.properties". Estos valores se almacenan en un objeto `Properties`
     * que puede ser accedido en otras partes de la aplicación.
     */
    public static void loadSetupValues() {
        try (InputStream input = new FileInputStream(PATH_CONFIG_PROPERTIES)) {
            properties = new Properties();
            properties.load(input);
            // Verifica que se hayan cargado claves
            LOGGER.info("Propiedades cargadas:");
            for (String key : properties.stringPropertyNames()) {
                LOGGER.info(key + " = " + properties.getProperty(key));
            }
        } catch (Exception exp) {
            LOGGER.error("Error al cargar propiedades: {}" , exp.getMessage());
        }
    }

    /**
     * Obtiene el valor asociado a una clave específica desde el archivo `Config.properties`.
     *
     * @param value Clave cuyo valor se desea obtener
     * @return Valor correspondiente a la clave proporcionada
     */
    public static String getSetupValue(String value) {
        return properties.getProperty(value);
    }

    /**
     * Inicia un servidor de Appium utilizando los valores definidos en el archivo de configuración.
     * Usa cualquier puerto libre disponible y sobreescribe sesiones existentes.
     */
    public static void startAppiumServer() {
        try {
            server = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(getSetupValue("nodePath"))).withAppiumJS(new File(getSetupValue("appiumPath")))
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE));
            server.start();
            server.clearOutPutStreams();
            appiumServerURL = server.getUrl();
        } catch (Exception e) {
            LOGGER.error("Error inesperado: {} ", e.getMessage());
        }
    }

    /**
     * Retorna la URL actual del servidor de Appium.
     *
     * @return URL del servidor Appium en ejecución
     */
    public static URL getAppiumServerURL() {
        return appiumServerURL;
    }

    /**
     * Detiene el servidor de Appium si se encuentra en ejecución.
     */
    public static void stopAppiumServer() {
        if (server.isRunning()) {
            server.stop();
        }
    }

    /**
     * Asigna el driver global para pruebas móviles, y configura el nombre de la plataforma.
     *
     * @param selectorDriver Instancia del driver de Appium (AndroidDriver o IOSDriver)
     */
    public static void setUpDriver(AppiumDriver selectorDriver){
        mobileDriver = selectorDriver;
        BaseScreen.getWait();
        platformName = getDriver().getCapabilities().getPlatformName().toString().toLowerCase();
        isAndroid = platformName.equals("android");
    }

    /**
     * Retorna el driver móvil que ha sido configurado (Android o iOS).
     *
     * @return Instancia de AppiumDriver
     */
    public static AppiumDriver getDriver() {
        return mobileDriver;
    }

    /**
     * Indica si la plataforma actual es Android.
     *
     * @return true si es Android, false si es iOS
     */
    public static Boolean getIsAndroid() {
        return isAndroid;
    }

    /**
     * Retorna el nombre de la plataforma actual en formato string.
     *
     * @return "android" o "ios"
     */
    public static String getPlatformName() {
        return getIsAndroid() ? "android":"ios";
    }

}
