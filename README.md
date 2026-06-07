# SeleniumPomLab - Automatización de Pruebas Selenium + Cucumber + Allure

Proyecto de automatización E2E para [sauce-demo.myshopify.com](https://sauce-demo.myshopify.com) usando Java, Selenium WebDriver 4, Cucumber BDD y reportes Allure.

---

## 1. Prerrequisitos

### Java 21 (JDK)

1. Descarga el JDK desde [https://adoptium.net](https://adoptium.net) (Temurin JDK 21)
2. Instala el instalador `.msi` (Windows)
3. Configura la variable de entorno:
   - Abre **Panel de Control → Sistema → Configuración avanzada del sistema → Variables de entorno**
   - En **Variables del sistema**, clic en **Nueva**:
     - Nombre: `JAVA_HOME`
     - Valor: `C:\Program Files\Eclipse Adoptium\jdk-21.0.x.x-hotspot` *(ajusta según tu ruta de instalación)*
   - Busca la variable `Path` → **Editar** → **Nuevo** → agrega: `%JAVA_HOME%\bin`
4. Verifica en una terminal nueva:
   ```cmd
   java -version
   ```
   Debe mostrar: `openjdk version "21.x.x"`

---

### Gradle (via Wrapper — no requiere instalación manual)

El proyecto incluye `gradlew.bat` que descarga Gradle automáticamente al ejecutar por primera vez. No necesitas instalar Gradle globalmente.

Si quieres instalarlo globalmente de todas formas:
1. Descarga desde [https://gradle.org/releases](https://gradle.org/releases) → versión 8.7 (binary-only)
2. Descomprime en `C:\Gradle\gradle-8.7`
3. Agrega a variables de entorno:
   - Nueva variable del sistema: `GRADLE_HOME` = `C:\Gradle\gradle-8.7`
   - Agrega a `Path`: `%GRADLE_HOME%\bin`
4. Verifica:
   ```cmd
   gradle -v
   ```

---

### Google Chrome

- Descarga e instala la versión más reciente desde [https://www.google.com/chrome](https://www.google.com/chrome)
- **ChromeDriver se descarga automáticamente** por WebDriverManager — no requiere instalación manual

---

### Git (opcional, para clonar el repositorio)

- Descarga desde [https://git-scm.com](https://git-scm.com)
- Verifica: `git --version`

---

## 2. Instalación del Proyecto

```cmd
git clone <url-del-repositorio>
cd SeleniumPomLab
```

O descarga el ZIP y descomprímelo.

No se requiere ninguna instalación adicional de dependencias — Gradle las descarga automáticamente la primera vez.

---

## 3. Estructura del Proyecto

```
SeleniumPomLab/
│
├── build.gradle                          # Dependencias y configuración Gradle + Allure
├── settings.gradle                       # Nombre del proyecto
├── gradlew                               # Wrapper Gradle Linux/macOS
├── gradlew.bat                           # Wrapper Gradle Windows
├── README.md                             # Este archivo
├── COMO_AGREGAR_CASO.md                  # Guía paso a paso para agregar nuevos casos
│
└── src/
    └── test/
        ├── java/
        │   └── com/saucedemo/
        │       │
        │       ├── pages/                # Capa POM — Page Objects
        │       │   ├── ProductPage.java  # Interacciones con la página del producto
        │       │   └── CartPage.java     # Interacciones con la página del carrito
        │       │
        │       ├── steps/                # Capa BDD — Step Definitions
        │       │   └── AddToCartSteps.java  # Mapeo Gherkin → Java
        │       │
        │       ├── runner/               # Punto de entrada de Cucumber
        │       │   └── CucumberTestRunner.java
        │       │
        │       └── utils/               # Utilidades
        │           └── DriverFactory.java  # Gestión del ciclo de vida de ChromeDriver
        │
        └── resources/
            ├── features/                 # Escenarios en Gherkin
            │   └── agregar_articulo_carrito.feature
            └── junit-platform.properties # Configuración de Cucumber
```
..\SeleniumPomLab> gradle -v

------------------------------------------------------------
Gradle 9.5.1
------------------------------------------------------------

Build time:    2026-05-12 13:19:42 UTC
Revision:      fd78213f09782e62ca4957f9cfd3d90c6c3f1767

Kotlin:        2.3.20
Groovy:        4.0.29
Ant:           Apache Ant(TM) version 1.10.15 compiled on August 25 2024
Launcher JVM:  22.0.1 (Oracle Corporation 22.0.1+8-16)
Daemon JVM:    C:\Program Files\Java\jdk-22 (no Daemon JVM specified, using current Java home)
OS:            Windows 11 10.0 amd64

gradle wrapper

---

## 4. Ejecución de las Pruebas

Abre una terminal en la carpeta `SeleniumPomLab/` y ejecuta:

**Windows:**
```cmd
gradlew.bat test
.\gradlew.bat test
./gradlew test --tests "com.demoblaze.runner.CucumberTestRunnerDemoblaze"
```

**Linux / macOS:**
```bash
./gradlew test
```

La primera ejecución descarga las dependencias (~2 minutos). Las siguientes son más rápidas.

### Resultado esperado en consola:
```
> Task :test

CucumberTestRunner > Cucumber > Agregar articulo al carrito > Verificar titulo y precio del producto  PASSED
CucumberTestRunner > Cucumber > Agregar articulo al carrito > Agregar un articulo al carrito y verificarlo  PASSED

BUILD SUCCESSFUL in 15s
```

---

## 5. Ver Evidencias — Reporte Allure

### Generar y abrir el reporte:

**Windows:**
```cmd
gradlew.bat allureServe
```

**Linux / macOS:**
```bash
./gradlew allureServe
```

Esto abre automáticamente el reporte en el navegador con:
- ✅ Estado de cada escenario (passed / failed)
- 📋 Pasos detallados de cada escenario Gherkin
- 📊 Gráficas de resultados
- 🕐 Duración de cada prueba

### Generar reporte HTML estático (sin abrir navegador):
```cmd
gradlew.bat allureReport
.\gradlew.bat allureReport  
```

El reporte queda en:
```
build/reports/allure-report/index.html
```

Ábrelo directamente en Chrome con doble clic.

### Resultados raw de Allure:
```
build/allure-results/
```

---

## 6. Tecnologías Utilizadas

| Tecnología         | Versión  | Propósito                              |
|--------------------|----------|----------------------------------------|
| Java               | 21       | Lenguaje de programación               |
| Gradle             | 8.7      | Herramienta de construcción            |
| Selenium WebDriver | 4.18.1   | Automatización del navegador           |
| WebDriverManager   | 5.7.0    | Gestión automática de ChromeDriver     |
| Cucumber           | 7.15.0   | Framework BDD (archivos .feature)      |
| JUnit Jupiter      | 5.10.2   | Motor de ejecución de pruebas          |
| Allure             | 2.25.0   | Reportes HTML enriquecidos             |
| Google Chrome      | Última   | Navegador objetivo                     |

---

## 7. Notas

- Las pruebas usan **esperas explícitas** (`WebDriverWait`) — sin `Thread.sleep()`
- El patrón **Page Object Model** separa los selectores CSS de los step definitions
- Para ejecutar en modo **headless** (sin abrir ventana del navegador), edita `DriverFactory.java`:
  ```java
  options.addArguments("--headless");
  ```
- Para agregar nuevos casos de prueba, consulta `COMO_AGREGAR_CASO.md`

---

## 8. Trabajar con Visual Studio Code

### 8.1 Extensiones requeridas

Instala las siguientes extensiones desde el Marketplace de VS Code (`Ctrl+Shift+X`):

| Extensión | ID | Para qué sirve |
|---|---|---|
| **Extension Pack for Java** | `vscjava.vscode-java-pack` | Soporte completo Java (Language Server, Debugger, Maven/Gradle) |
| **Gradle for Java** | `vscjava.vscode-gradle` | Ejecutar tareas Gradle desde VS Code |
| **Cucumber (Gherkin) Full Support** | `alexkrechik.cucumberautocomplete` | Syntax highlighting y autocompletado en `.feature` |
| **Test Runner for Java** | `vscjava.vscode-java-test` | Ejecutar y depurar pruebas desde el panel de pruebas |

### 8.2 Abrir el proyecto correctamente

1. Abre VS Code
2. `File → Open Folder` → selecciona la carpeta `SeleniumPomLab/`
3. VS Code detecta automáticamente el `build.gradle` y descarga las dependencias
4. Espera que aparezca la notificación **"Java projects loaded"** en la barra inferior

> ⚠️ Abre directamente la carpeta `SeleniumPomLab/` que contiene el `build.gradle`

### 8.3 Configurar el reconocimiento del .feature

El archivo `.vscode/settings.json` ya está incluido en el proyecto con la configuración lista:

```json
{
  "cucumberautocomplete.steps": [
    "src/test/java/com/saucedemo/steps/*.java"
  ],
  "cucumberautocomplete.syncfeatures": "src/test/resources/features/*.feature",
  "cucumberautocomplete.strictGherkinCompletion": false,
  "java.configuration.updateBuildConfiguration": "automatic",
  "java.test.config": {
    "workingDirectory": "${workspaceFolder}"
  }
}
```

Esto activa:
- Autocompletado de pasos en los `.feature`
- Navegación entre el paso del `.feature` y su Step Definition con `Ctrl+Click`
- Recarga automática del proyecto cuando cambia `build.gradle`

### 8.4 Ejecutar las pruebas desde VS Code

**Opción 1 — Terminal integrada** (`Ctrl+ñ` o `View → Terminal`):
```cmd
gradlew.bat test
```

**Opción 2 — Panel de Gradle**:
1. Clic en el ícono de Gradle en la barra lateral izquierda (elefante)
2. Expande `SeleniumPomLab → Tasks → verification`
3. Doble clic en `test`

**Opción 3 — Panel de Testing** (`Ctrl+Shift+P` → "Testing: Focus on Test Explorer View"):
1. Expande el árbol de pruebas
2. Clic en el ícono ▶ junto al escenario o a la suite completa

### 8.5 Depurar una prueba (Debug)

1. Abre `AddToCartSteps.java`
2. Haz clic en el margen izquierdo de una línea para agregar un **breakpoint** (punto rojo)
3. En el panel de Testing, clic derecho sobre la prueba → **Debug Test**
4. VS Code abrirá Chrome y pausará la ejecución en el breakpoint
5. Usa `F10` (step over), `F11` (step into), `F5` (continuar)

### 8.6 Ver el reporte Allure desde VS Code

En la terminal integrada:
```cmd
gradlew.bat allureServe
```

Se abre automáticamente en el navegador. Para cerrar el servidor: `Ctrl+C` en la terminal.

### 8.7 Estructura de carpetas en VS Code

Después de cargar el proyecto verás en el Explorer:

```
SELENIUMpomlab
├── src/test/java
│   └── com.saucedemo
│       ├── pages
│       ├── runner
│       ├── steps
│       └── utils
├── src/test/resources
│   └── features
│       └── agregar_articulo_carrito.feature  ← ícono de pepino 🥒
├── build.gradle
└── settings.gradle
```

El archivo `.feature` aparece con ícono de pepino si la extensión Cucumber está instalada.
