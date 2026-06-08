@echo off
REM Script para automatizar configuracion de Jenkins en Windows 11
REM Ejecutar como Administrador

setlocal enabledelayedexpansion

echo.
echo ========================================
echo  CONFIGURADOR DE JENKINS - WINDOWS 11
echo ========================================
echo.

REM Configurar variables
set JAVA_HOME=C:\Program Files\Java\jdk-21
set JENKINS_HOME=C:\Program Files\Jenkins
set PROJECT_PATH=D:\Development\cursoQA\SeleniumPomLab
set JENKINS_PORT=8080

REM Verificar permisos Admin
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo ERROR: Este script debe ejecutarse como Administrador
    echo.
    pause
    exit /b 1
)

echo [1] Validar Java 21 instalado
if exist "%JAVA_HOME%\bin\java.exe" (
    echo  ✓ Java 21 encontrado en: %JAVA_HOME%
    "%JAVA_HOME%\bin\java.exe" -version
) else (
    echo  ✗ ERROR: Java 21 no encontrado en %JAVA_HOME%
    echo    Instalar desde: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo.
echo [2] Verificar proyecto Gradle
if exist "%PROJECT_PATH%\build.gradle" (
    echo  ✓ Proyecto encontrado en: %PROJECT_PATH%
) else (
    echo  ✗ ERROR: Proyecto no encontrado en %PROJECT_PATH%
    pause
    exit /b 1
)

echo.
echo [3] Configurar variables de entorno
setx JAVA_HOME "%JAVA_HOME%"
setx PROJECT_PATH "%PROJECT_PATH%"
echo  ✓ Variables configuradas globalmente

echo.
echo [4] Crear directorio de workspace para Jenkins
if not exist "D:\jenkins-workspace" (
    mkdir D:\jenkins-workspace
    echo  ✓ Directorio creado: D:\jenkins-workspace
) else (
    echo  ✓ Directorio ya existe: D:\jenkins-workspace
)

echo.
echo [5] Información para instalacion manual
echo.
echo  Descargar Jenkins:
echo  - Ir a: https://www.jenkins.io/download/
echo  - Descargar: Jenkins for Windows (jenkins-latest.msi)
echo  - O descargar: jenkins.war para ejecucion independiente
echo.
echo  Instalacion .msi:
echo  - Ejecutar como Admin el archivo .msi
echo  - Seleccionar puerto: %JENKINS_PORT%
echo  - Servicio se iniciara automaticamente
echo.
echo  Instalacion .war:
echo  - java -jar jenkins.war --httpPort=%JENKINS_PORT%
echo.

echo.
echo [6] Proximos pasos despues de instalar Jenkins
echo.
echo  1. Abrir navegador: http://localhost:%JENKINS_PORT%
echo  2. Obtener contraseña inicial:
echo     - En Windows: C:\Program Files\Jenkins\secrets\initialAdminPassword
echo     - O ejecutar: more C:\Program Files\Jenkins\secrets\initialAdminPassword
echo  3. Completar wizard de instalacion
echo  4. Instalar Plugins:
echo     - Allure Plugin
echo     - Timestamper Plugin
echo  5. Configurar Herramientas:
echo     - Manage Jenkins ^> Global Tool Configuration
echo     - JDK: C:\Program Files\Java\jdk-21
echo     - Allure: Auto-download o C:\Program Files\Allure
echo  6. Crear Pipeline Job
echo  7. Clonar Jenkinsfile del proyecto

echo.
echo [7] Ver instrucciones detalladas
echo  Abrir archivo: %PROJECT_PATH%\JENKINS_SETUP.md
echo.

echo ========================================
echo  Configuracion preliminar completada!
echo ========================================
echo.

pause
endlocal
