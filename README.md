**Tecnologías Utilizadas**

* Java
* Appium
* TestNG
* Maven
* ExtentReports

**Estructura del Proyecto**

    src/
    ├── main/
    │   └── java/
    │       ├── mobileDrivers/                    # Configuración inicial de AndroidDriver y iOSDriver
    │       ├── objects/                          # Objetos principales divididos por producto y módulo
    │       │   └── product_X/
    │       │       └── module_X/
    │       ├── steps/                            # Definición de pasos de prueba por producto y módulo
    │       │   └── product_X/
    │       │       └── module_X/
    │       ├── utils/
    │       │   ├── data/                         # Generación y manipulación de datos
    │       │   ├── AutoTools.java                # Configuración de Appium y `config.properties`
    ├── test/
    │   ├── java/
    │   │   ├── product_X/                       # Casos de prueba organizados por producto y módulo
    │   │   ├── MainMobileTest.java              # Gestión de ejecución, Appium server y reportes
    │   └── resources/
    │       ├── data/                            # Datos de prueba divididos por producto
    │       ├── Image/                           # Recursos gráficos (ej. QR, íconos, etc.)
    │       ├── devices.json                     # Configuración de dispositivos
    │       └── config.properties                # Configuración general del proyecto
    suites/
    └── product_X/
    └── module_X/
    └── SuiteMaster.xml                  # Suite principal de ejecución
    
    📦 Nota: product_X y module_X representan placeholders genéricos. Sustitúyelos por los nombres reales de productos y módulos.

**Prerrequisitos**

_Asegúrate de tener instalado y configurado lo siguiente:_
    
    - JDK 21 o superior (ideal: JDK 24)
    - Maven
    - Android Studio
    - Xcode (solo para pruebas en iOS)
    - Appium Server v2
    - Plugins de Appium para Android e iOS
    - WebDriverAgent configurado para dispositivos iOS

**Instalación**

    Instalar Java JDK:
    - Descargar desde Oracle o usar un gestor como SDKMAN.

    Instalar Appium:
    - npm install -g appium
    - appium driver install uiautomator2
    - appium driver install xcuitest
    
    Instalar Android Studio:    
    - Asegúrate de instalar los SDKs necesarios desde el SDK Manager.

    Instalar Maven:
    Mac:
    - Desde https://maven.apache.org/download.cgi o usando Homebrew:
    - brew install maven
    
    Windows:
    - Ve al sitio oficial: https://maven.apache.org/download.cgi
    - Descarga el archivo .zip, por ejemplo:
        apache-maven-3.9.6-bin.zip (o la última versión estable).

    Instalar Xcode:
    - Disponible en la App Store para macOS, en caso de usar mac  [Se puede agregar, No disponible para este repo]

**Configuración Extra**

_Antes de ejecutar las pruebas, es importante configurar correctamente las rutas, el entorno y las opciones de ejecución. A continuación,
se detallan los pasos necesarios:_

    🔧 Rutas de Node.js y Appium
    Obtén las rutas absolutas de node y appium en tu máquina:
    Mac:
    - which node
    - which appium
    
    Windows:
    - where node
    - where appium

    🔁 Actualiza estas rutas en el archivo config.properties para que coincidan con tu sistema local:
    - nodePath=/tu/ruta/personalizada/a/node
    - appiumPath=/tu/ruta/personalizada/a/appium

    📱 Selección del Dispositivo
    En el archivo devices.json, especifica el dispositivo que se usará con la propiedad usedPhone.
    Ejemplo:

    {
        "usedPhone": "iPhone_13_Pro_Max",
         ...
    }

    ⚙️ Tipo de Ejecución [Se puede agregar, No disponible para este repo]
    Configura el tipo de ejecución en el archivo config.properties con la variable exe:
    exe=remote   # Para ejecuciones con proveedores como Kobiton, BrowserStack, etc.
    exe=local    # Para ejecuciones en el entorno local

    📊 Configuración de Reportes
    Personaliza la generación y envío de reportes de pruebas:
        statusReport=enabled         # Habilita generación de reportes por suite ejecutada
        statusReport=disabled        # Deshabilita reportes
        suiteNumberConfig=2          # Define el número de suites a ejecutar (⚠️ importante)
        onlyScreenShotBug=enabled    # Captura solo escenarios que fallan
        onlyScreenShotBug=disabled   # Captura todos los escenarios

**Ejecución de Suites de Pruebas**

_Puedes ejecutar una o varias suites de pruebas utilizando Maven y el archivo pom.xml configurado.
Las suites se encuentran en el directorio suites/, organizadas por producto y módulo._

    🧪 Comando general
    - mvn test -DsuiteXmlFile=suites/producto/modulo/SuiteArchivo.xml

    📌 Ejemplo:
    - mvn test -DsuiteXmlFile=suites/albo/login/SuiteLogin.xml
    Esto:
      - Inicia Appium.
      - Ejecuta las pruebas configuradas en la suite XML.
      - Genera el reporte (si está habilitado).
      - Envía resultados (Slack, Zephyr, etc., si están configurados).

**Tips adicionales**

    Asegúrate de tener configurado correctamente:
    - devices.json → Dispositivo a usar (usedPhone)
      - config.properties → Rutas de Appium y Node y preferencias de reporte
      Puedes crear y ejecutar suites maestras agregándolas en SuiteMaster.xml si deseas ejecutar múltiples módulos en un solo paso.
