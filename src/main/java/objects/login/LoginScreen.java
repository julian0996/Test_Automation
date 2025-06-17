package objects.login;

import io.appium.java_client.pagefactory.AndroidFindBy;
import objects.BaseScreen;
import org.openqa.selenium.WebElement;

public class LoginScreen extends BaseScreen {

    @AndroidFindBy(id = "com.androidsample.generalstore:id/spinnerCountry")
    WebElement selectorCountry;

    @AndroidFindBy(id = "com.androidsample.generalstore:id/nameField")
    WebElement inputNameCustomer;

    @AndroidFindBy(id = "com.androidsample.generalstore:id/btnLetsShop")
    WebElement buttonLogin;

    @AndroidFindBy(id = "com.androidsample.generalstore:id/toolbar_title")
    WebElement labelLoginOk;

   @AndroidFindBy(id = "com.androidsample.generalstore:id/btnLetsShop")
    WebElement toastException;


    /*
     * acciones
     */
    public void tapSelectorCountry() {
        tapElements(selectorCountry);
    }

    public void selectGerden(String text) {
        String xpathDynamic = "//android.widget.RadioGroup/android.widget.RadioButton[@text = 'gender']".replaceAll("gender", text);
        tapDynamic(xpathDynamic);
    }

    public void selectCountry(String text) {
        scrollSelectDynamic(text);
    }

    public void inputNameCustomer(String text) {
        enter(inputNameCustomer, text);
    }

    public void tapButtonLogin() {
        tapElements(buttonLogin);
    }

    public boolean isVisibleLabelLoginOk() {
       return isDisplayed(labelLoginOk);
    }

    public boolean isVisibleToastException() {
        return isDisplayed(toastException);
    }
}
