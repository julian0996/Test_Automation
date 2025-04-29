package objects;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.status.StatusLogger;
import org.openqa.selenium.support.PageFactory;

public class BaseScreen {

    private static final long WAITING_TIME = 15;
    public static StatusLogger log = StatusLogger.getLogger();
    public BaseScreen() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

}
