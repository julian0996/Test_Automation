package tests.login;

import org.testng.asserts.SoftAssert;
import steps.login.LoginMethods;
import utils.Json;

import static utils.DataManager.*;

public class HelperTestLogin {

    static final Json loginCustomer = new Json("src/test/resources/data/Customer.json");
    static String countryCustomer = null, gerdenCustomer = null;

    /**
     * Método auxiliar que ejecuta una prueba de login basada en el nombre del flujo recibido como String.
     * Convierte el string a su valor correspondiente en el enum 'flowTestLogin' y ejecuta la prueba.
     *
     * @param flowTestLogin Nombre del flujo de prueba (debe coincidir con un valor del enum 'flowTestLogin').
     */
    public static void testHelperLogin(String flowTestLogin) {
        inicializarVariables();
        SoftAssert softAssert = new SoftAssert();
        LoginMethods.flowTestLogin flowTestEnum;
        flowTestEnum = convertEnum(LoginMethods.flowTestLogin.class, flowTestLogin);
        softAssert.assertTrue(LoginMethods.loginExecute(flowTestEnum, countryCustomer, getGenderRandom(), getRandomNameCustomer()));
        softAssert.assertAll();
    }

    /**
     * Inicializa las variables requeridas para el flujo de login desde un objeto de configuración.
     * Extrae el país y el género del cliente desde un archivo o fuente de datos externa tambien se puede generar los datos random pero como demostración se hace así.
     */
    private static void inicializarVariables() {
        countryCustomer = loginCustomer.getString("dataCustomer.countryCustomer");
       // gerdenCustomer = loginCustomer.getString("dataCustomer.gardenCustomer");
    }
}
