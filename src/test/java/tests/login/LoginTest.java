package tests.login;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import tests.MainMobileTest;

import static tests.login.HelperTestLogin.testHelperLogin;

public class LoginTest extends MainMobileTest {

    /**
     * Test de login parametrizado utilizando TestNG.
     * Ejecuta el flujo de login según el valor del parámetro 'flowTestLogin'.
     *
     * Este método permite ejecutar múltiples escenarios pasando distintos valores del enum 'flowTestLogin',
     * como por ejemplo: FLOW_LOGIN_OK, FLOW_LOGIN_WITH_OUT_NAME, etc.
     *
     * @param flowTestLogin Parámetro opcional que indica el flujo de prueba a ejecutar.
     *                      Por defecto se usa "FlowLoginWithOutName" si no se pasa otro valor.
     */
    @Parameters({"flowTestLogin"})
    @Test
    public void testLogin(@Optional("FlowLoginWithOutName") String flowTestLogin) {
        testHelperLogin(flowTestLogin);
    }

}
