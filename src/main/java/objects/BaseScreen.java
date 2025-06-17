package objects;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static utils.AutoTools.getDriver;
import static utils.AutoTools.getIsAndroid;

public class BaseScreen {

    private static final long WAITING_TIME = 15;

    public BaseScreen() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    private static final Logger LOGGER = LogManager.getLogger(BaseScreen.class);

    /*
     * Retorna una instancia de FluentWait configurada para evitar errores por elementos no presentes o inestables.
     */
    public static Wait getWait() {
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(50))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }


    /*
     * Espera explícita hasta que el elemento sea visible en pantalla.
     */
    protected static void waitToBeVisibility(WebElement element) {
        getWait().until(ExpectedConditions.visibilityOf(element));
    }

    /*
     * Espera explícita hasta que el elemento sea clickeable.
     * Maneja posibles errores con logs.
     */
    protected static void waitToBeClickable(WebElement element) {
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException ex) {
            LOGGER.error("TimeOut: El elemento no fue clickable a tiempo {} ", ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Error inesperado al esperar el elemento fuera clickable {}", ex.getMessage());
        }
    }

    /*
     * Realiza un tap (click) sobre un elemento tras esperar que sea visible y clickeable.
     */
    protected static void tapElements(WebElement element) {
        try {
            waitToBeVisibility(element);
            waitToBeClickable(element);
            element.click();
        } catch (TimeoutException ex) {
            LOGGER.error("TimeOut: El elemento no fue clickable a tiempo {} ", ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Error inesperado al esperar el elemento fuera clickable {}", ex.getMessage());
        }
    }

    /*
     * Envía texto a un campo de texto asegurando que sea clickeable y limpio. Oculta teclado al final.
     */
    protected  void enter(WebElement element, String text) {
        try {
            waitToBeClickable(element);
            element.click();
            element.clear();
            element.sendKeys(text);
            getDriver().executeScript("mobile:hideKeyboard");
        } catch (TimeoutException ex) {
            LOGGER.error("TimeOut: El elemento no fue clickable a tiempo {} ", ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Error inesperado al esperar el elemento fuera clickable {}", ex.getMessage());
        }
    }

    /*
     * Lanza la app de prueba según la plataforma.
     * En Android usa `mobile: activateApp`.
     */
    public static void launchAppByPlatform() {
        terminateAppByPlatform();
        getDriver().executeScript("mobile: activateApp", Map.of("appId", "com.androidsample.generalstore"));
    }

    /*
     * Cierra la app según plataforma.
     * En Android, se usa el appId.
     */
    public static void terminateAppByPlatform() {
        Map<String, Object> terminateArgs = new HashMap<>();
        terminateArgs.put("appId", "com.androidsample.generalstore");  // Use appId for Android
        getDriver().executeScript("mobile: terminateApp", terminateArgs);
    }

    /*
     * Realiza scroll hasta encontrar un texto en pantalla y hace clic.
     * Solo aplica para Android (UiScrollable).
     */
    protected static void scrollSelectDynamic(String text) {
        By selector = AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                        ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"))"
        );
        WebElement element = getDriver().findElement(selector);
        element.click();
    }

    /*
     * Realiza un tap sobre un elemento localizado por XPath dinámico.
     */
    protected static void tapDynamic(String xpathDynamic) {
        WebElement checkboxGender = getDriver().findElement(By.xpath(xpathDynamic));
        tapElements(checkboxGender);
    }

    /*
     * Retorna si un elemento está visible en pantalla.
     * Retorna false si lanza NoSuchElementException.
     */
    protected static boolean isDisplayed(WebElement element) {
        try {
            waitToBeVisibility(element);
            return element.isDisplayed();
        } catch (NoSuchElementException ex) {
            LOGGER.error("No se detecto elemento {}" , ex.getMessage());
            return false;
        }
    }

}
