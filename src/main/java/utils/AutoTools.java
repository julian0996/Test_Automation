package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class AutoTools {
    private static Properties properties = null;
    private static AppiumDriverLocalService server = null;
    private static URL appiumServerURL = null;
    private static AppiumDriver<MobileElement> mobileDriver = null;
    private static Boolean isAndroid = null;

    /*
     * cargar archivo config.properties
     */
    public static void loadSetupValues() {
        try {
            InputStream input = new FileInputStream("src/test/resources/Config.properties");
            properties = new Properties();
            properties.load(input);
        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    /*
     * obetener valores que vienen en config.properties
     */
    public static String getSetupValue(String value) {
        return properties.getProperty(value);
    }

    /*
     *
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
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     *
     */
    public static URL getAppiumServerURL() {
        return appiumServerURL;
    }

    /*
     *
     */
    public static void stopAppiumServer() {
        if (server.isRunning()) {
            server.stop();
        }
    }

    /*
     *
     */

}
