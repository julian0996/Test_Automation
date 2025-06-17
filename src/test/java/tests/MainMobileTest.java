package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import mobileDriver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.DeviceLoader;
import utils.Json;
import utils.SuiteExecutionTraker;
import java.util.Optional;


import java.util.HashMap;

import static objects.BaseScreen.launchAppByPlatform;
import static objects.BaseScreen.terminateAppByPlatform;
import static utils.AutoTools.*;

public class MainMobileTest {
    private static ExtentReports extent;
    private static ExtentTest test;
    static String categoryReport = null, suiteName = null, pathReport = null, hostNameReport = "General store", env = "SBX";
    static String statusReport = null, statusEnabled  = "enabled", versionApp = null;
    private static volatile boolean isReportInitialized = false;
    private String testName = null;
    private String onlyScreentShotBug = null;
    public static int tcPass = 0, tcFail = 0, tcSkip = 0;
    static final Json devicesData = new Json("src/test/resources/devices.json");
    static String device;
    private static final Logger LOGGER = LogManager.getLogger(MainMobileTest.class);

    /**
     * Método ejecutado una sola vez antes de iniciar toda la suite de pruebas.
     * Se encarga de cargar configuración, iniciar Appium, configurar el entorno y preparar el reporte (si está habilitado).
     */
    @BeforeSuite // Para pruebas en paralelo manejar @BeforeTest
    @Parameters({"noReset"})
    public void configSetUp(ITestContext context, @org.testng.annotations.Optional("true") String noReset) {
        loadSetupValues(); // cargar variables de entorno
        startAppiumServer(); //arrancar appium
        SuiteExecutionTraker.setTotalSuites(Integer.parseInt(getSetupValue("suiteNumberConfig")));
        statusReport = getSetupValue("statusReport");
        device = devicesData.getString(getSetupValue("usedPhone")+".deviceName");
        if (statusReport.equals(statusEnabled)) {
            if(!isReportInitialized) {
                synchronized (MainMobileTest.class){
                    if(!isReportInitialized) {
                        initializeReport(context);
                        assignedEnvironment(noReset, device);
                        isReportInitialized = true;
                    }
                }
            }
        } else {
            assignedEnvironment(noReset, device);
        }
    }

    /**
     * Ejecutado una vez antes de cada clase de prueba. Lanza la app.
     */
    @BeforeClass
    public void launchApp() {
        launchAppByPlatform();
    }

    /**
     * Ejecutado antes de cada test definido en el XML.
     * Si el reporte está habilitado, crea una entrada en el reporte para este test.
     */
    @BeforeTest
    public void createTest(ITestContext context) {
        if(statusReport.equals(statusEnabled)) {
            testName = context.getCurrentXmlTest().getName();
            test = extent.createTest(testName);
            categoryReport = context.getCurrentXmlTest().getParameter("category");
            test.assignCategory(categoryReport);
        }
    }

    /**
     * Ejecutado una vez después de cada clase de prueba.
     * Finaliza la app según la plataforma.
     */
    @AfterClass
    public void tearDown() {
        terminateAppByPlatform();
    }

    /**
     * Ejecutado después de cada método de prueba (caso).
     * Registra los resultados en el reporte dependiendo del estado.
     */
    @AfterMethod
    public void result(ITestResult result, ITestContext context) {
        if (statusReport.equals(statusEnabled)) {
            switch (result.getStatus()) {
                case ITestResult.FAILURE:
                    test.log(Status.FAIL, "El caso de prueba ha fallado: " + result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshot()).build());
                    tcFail++;
                    break;

                case ITestResult.SKIP:
                    test.log(Status.SKIP, "La prueba ha sido saltada: " + result.getThrowable());
                    tcSkip++;
                    break;

                case ITestResult.STARTED:
                    test.log(Status.INFO, "La prueba ha comenzado pero no se completó.");
                    tcSkip++;
                    break;

                case ITestResult.SUCCESS:
                    onlyScreentShotBug = getSetupValue("onlyScreenShootBug");
                    if (onlyScreentShotBug.equals("enabled")) {
                        test.log(Status.PASS, "La prueba ha pasado exitosamente");
                    } else {
                        test.log(Status.PASS, "La prueba ha pasado exitosamente",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshot()).build());
                    }
                    tcPass++;
                    break;

                default:
                    test.log(Status.WARNING, "Estado de la prueba no reconocido.");
                    break;
            }
        }
    }

    /**
     * Ejecutado una vez al finalizar toda la suite.
     * Limpia el entorno, finaliza reporte y cierra Appium si corresponde.
     */
    @AfterSuite(alwaysRun = true)
    public void cleanUp(ITestContext context) {
        if(!SuiteExecutionTraker.shouldExecuteCleanUp()) {
            return;
        }
        synchronized (this) {
            try {
                if(statusReport.equals(statusEnabled)) {
                    extent.flush();
                    //Enviar correo
                }
                Optional.ofNullable(getDriver()).ifPresent(driver -> driver.quit());
                stopAppiumServer();
            } catch(Exception e) {
               LOGGER.error("Error inesperado {}", e.getMessage());
            }
        }
    }

    /**
     * Captura una captura de pantalla en formato base64 desde el dispositivo.
     *
     * @return Captura en base64
     */
    private String getScreenshot() {
        TakesScreenshot ts = getDriver();
        return ts.getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Inicializa el reporte con nombre de suite, tema, y datos del entorno.
     *
     * @param context Contexto de ejecución de TestNG
     */
    private void initializeReport(ITestContext context) {
        versionApp = getSetupValue("versionApp");
        String nameCycle = "Ciclo de pruebas: " + versionApp;

        suiteName = context.getSuite().getName();
        pathReport = "report/" + suiteName + "/" + suiteName + ".html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(pathReport);
        sparkReporter.config().setDocumentTitle(suiteName);
        sparkReporter.config().setReportName(suiteName);
        sparkReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.setSystemInfo("Host Name", hostNameReport);
        extent.setSystemInfo("Environment", env);
        extent.attachReporter(sparkReporter);
    }

    /**
     * Asigna el entorno de prueba configurando las DesiredCapabilities en base al dispositivo definido.
     *
     * @param noReset      Flag para indicar si se reinicia o no la app
     * @param deviceUsed   Nombre o UDID del dispositivo a utilizar
     */
    private void assignedEnvironment(String noReset, String deviceUsed) {
        boolean noResetBoolean = Boolean.parseBoolean(noReset);
        DesiredCapabilities dc = new DesiredCapabilities();
        String valueMaven = System.getProperty("device");

        if(valueMaven == null) {
            valueMaven = getSetupValue("usedPhone");
        }
        HashMap<String, String> mapDevice = DeviceLoader.devicesJsonReader(valueMaven);
        mapDevice.forEach(dc::setCapability);
        dc.setCapability("appium:noReset", noResetBoolean);
        dc.setCapability("appium:autoGrantPermissions", true);
        DriverFactory.setDriver(mapDevice.get("platformName"), dc, deviceUsed);
    }
}
