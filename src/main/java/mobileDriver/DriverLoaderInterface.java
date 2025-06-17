package mobileDriver;

import org.openqa.selenium.remote.DesiredCapabilities;

public interface DriverLoaderInterface {
    void loadDriver(DesiredCapabilities dc, String deviceUsed);
}
