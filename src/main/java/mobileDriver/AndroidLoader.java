package mobileDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;

import static utils.AutoTools.getAppiumServerURL;
import static utils.AutoTools.setUpDriver;

public class AndroidLoader implements DriverLoaderInterface{

    private static final String PATH_APK = "src/test/resources/General-Store.apk";
    private static final String PLATFORM_DEVICE = "Android";
    /**
     * Configura e inicializa el driver de Appium para un dispositivo Android utilizando UiAutomator2.
     *
     * @param dc DesiredCapabilities (no se usa directamente en este método, pero podría usarse en futuras mejoras)
     * @param deviceUsed El nombre o UDID del dispositivo Android que se va a utilizar
     */
    public void loadDriver(DesiredCapabilities dc, String deviceUsed) {
        UiAutomator2Options options;
        options = new UiAutomator2Options()
                .setPlatformName(PLATFORM_DEVICE)
                .setDeviceName(deviceUsed)
                .setUdid(deviceUsed)
                .setApp(PATH_APK)
                .setNoReset(true)
                .setAutoGrantPermissions(true);
                options.setCapability("appium:autoAcceptAlerts", true);
                options.setNewCommandTimeout(Duration.ofSeconds(300));
        setUpDriver(new AndroidDriver(getAppiumServerURL(), options));
    }
}
