package steps.login;

import objects.login.LoginScreen;
import utils.ErrorController;

import java.util.function.Supplier;

public class LoginMethods {

    static LoginScreen loginScreen = new LoginScreen();

    /**
     * Enum que define los distintos flujos de prueba para el proceso de login.
     *
     * - FLOW_LOGIN_OK: Flujo positivo donde se ingresan todos los datos correctamente.
     * - FLOW_LOGIN_WITH_OUT_NAME: Flujo negativo donde se omite el nombre del cliente y se espera un error.
     */
    public enum flowTestLogin {
        FLOW_LOGIN_OK {
            @Override
            boolean execute(String country, String gender, String name, LoginScreen screen) {
                return handleLoginOk(country, gender, name, screen::isVisibleLabelLoginOk);
            }
        },
        FLOW_LOGIN_WITH_OUT_NAME {
            @Override
            boolean execute(String country, String gender, String name, LoginScreen screen) {
                return handleLoginWithOutNameException(country, gender, screen::isVisibleToastException);
            }
        };
        abstract boolean execute(String country, String gender, String name, LoginScreen screen);
    }

    /**
     * Ejecuta el flujo de login según el tipo de prueba especificado.
     *
     * @param flowTest Tipo de flujo de prueba (por ejemplo, login exitoso o sin nombre).
     * @param countryCustomer País del cliente a seleccionar.
     * @param genderCustomer Género del cliente a seleccionar.
     * @param nameCustomer Nombre del cliente (puede ser omitido según el flujo).
     * @return true si el flujo se ejecuta correctamente según el criterio de validación, false en caso contrario.
     */
    public static boolean loginExecute(LoginMethods.flowTestLogin flowTest, String countryCustomer, String genderCustomer, String nameCustomer) {
        if (!ErrorController.validateNotNull(flowTest, "flujo de login")) return false;
        if (!ErrorController.validateRequiredField(countryCustomer, "país del cliente")) return false;
        if (!ErrorController.validateRequiredField(genderCustomer, "género del cliente")) return false;
        return flowTest.execute(countryCustomer, genderCustomer, nameCustomer, loginScreen);
    }

    /**
     * Maneja el flujo de login exitoso cuando el usuario completa todos los datos.
     *
     * @param country País a seleccionar.
     * @param gender Género a seleccionar.
     * @param name Nombre a ingresar.
     * @param isLabelVisible Función que valida si se muestra la etiqueta de login exitoso.
     * @return true si la etiqueta es visible después del login, false en caso contrario.
     */
    private static boolean handleLoginOk(String country, String gender, String name, Supplier<Boolean> isLabelVisible) {
        loginScreen.tapSelectorCountry();
        loginScreen.selectCountry(country);
        loginScreen.inputNameCustomer(name);
        loginScreen.selectGender(gender);
        loginScreen.tapButtonLogin();
        return isLabelVisible.get();
    }

    /**
     * Maneja el flujo donde no se ingresa nombre y se espera una excepción.
     *
     * @param country País a seleccionar.
     * @param gender Género a seleccionar.
     * @param isToastVisible Función que valida si aparece un mensaje toast indicando error.
     * @return true si el mensaje de error (toast) es visible, false en caso contrario.
     */
    private static boolean handleLoginWithOutNameException(String country, String gender, Supplier<Boolean> isToastVisible) {
        loginScreen.tapSelectorCountry();
        loginScreen.selectCountry(country);
        loginScreen.selectGender(gender);
        loginScreen.tapButtonLogin();
        return isToastVisible.get();
    }

}
