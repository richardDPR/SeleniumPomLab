# Configuración de Jenkins para SeleniumPomLab

Guía para instalar y configurar Jenkins en Windows 11 con ejecución automática de pruebas Demoblaze y generación de reportes Allure.

## Requisitos Previos

- Java 21 (JDK): `C:\Program Files\Java\jdk-21`
- Gradle instalado o usar el wrapper `gradlew.bat`
- Git (opcional, para clonar el repo)
- Windows 11 con permisos administrativos

## Instalación de Jenkins

### 1. Descargar Jenkins

1. Ir a https://www.jenkins.io/download/
2. Descargar **Jenkins for Windows** (archivo `.msi`)
3. O descargar **jenkins.war** para ejecutar con Java

### 2. Instalar Jenkins (opción .msi)

```powershell
# Ejecutar como administrador
msiexec.exe /i jenkins-latest.msi
```

Jenkins se instala como servicio Windows y corre en `http://localhost:8080`

### 3. Verificar Instalación

1. Abrir navegador: `http://localhost:8080`
2. Copiar la contraseña inicial de:
   - `C:\Program Files\Jenkins\secrets\initialAdminPassword`
   - O revisar el Output de la instalación
3. Crear usuario admin

### 4. Configurar Jenkins

#### Instalar Plugins Requeridos

1. Ir a **Manage Jenkins** → **Manage Plugins**
2. Buscar e instalar:
   - **Allure Plugin** (io.qameta.allure.jenkins)
   - **Log Parser Plugin** (opcional, para parsing de logs)
   - **Timestamper Plugin** (para timestamps en logs)
   - **JUnit Plugin** (ya incluido)

#### Configurar Herramientas

1. **Manage Jenkins** → **Global Tool Configuration**

   **JDK Installation:**
   - Name: `JDK-21`
   - JAVA_HOME: `C:\Program Files\Java\jdk-21`

   **Allure Commandline:**
   - Name: `Allure_2.42.0`
   - Installation directory: `C:\Program Files\Allure`
   
   (Si no tienes Allure instalado, Jenkins lo descargará automáticamente)

### 5. Configurar Agente Windows

1. **Manage Jenkins** → **Manage Nodes and Clouds**
2. Create a new node:
   - Name: `windows`
   - Type: Permanent Agent
   - Remote Root Directory: `D:\jenkins-workspace`
   - Launch method: **Launch agents via Windows Remote Management (WinRM)**
   - Labels: `windows`

## Crear Pipeline en Jenkins

### Opción A: Using Jenkinsfile from Git

1. Nueva Tarea → **Pipeline**
2. Name: `SeleniumPomLab-Tests`
3. Build Triggers:
   - ☑ **Poll SCM** → Schedule: `H 8 * * *` (diario a las 8 AM)
4. Pipeline:
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/tuuser/SeleniumPomLab.git`
   - Branch: `*/main` (o tu rama)
   - Script Path: `Jenkinsfile`
5. Save

### Opción B: Direct Jenkins Pipeline (sin Git)

1. Nueva Tarea → **Pipeline**
2. Name: `SeleniumPomLab-Tests`
3. Pipeline Script:

```groovy
pipeline {
    agent { label 'windows' }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21'
        PROJECT_PATH = 'D:\\Development\\cursoQA\\SeleniumPomLab'
    }

    stages {
        stage('Validar Entorno') {
            steps {
                echo '====== Validando entorno ======'
                bat 'java -version'
                bat 'dir "%PROJECT_PATH%"'
            }
        }

        stage('Limpiar Build') {
            steps {
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat clean'
            }
        }

        stage('Ejecutar Tests') {
            steps {
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat test --tests "com.demoblaze.runner.CucumberTestRunnerDemoblaze"'
            }
        }

        stage('Generar Allure') {
            steps {
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat allureReport'
            }
        }

        stage('Publicar Allure') {
            steps {
                allure([
                    commandline: 'Allure_2.42.0',
                    results: [[path: 'D:\\Development\\cursoQA\\SeleniumPomLab\\build\\allure-results']]
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'D:\\Development\\cursoQA\\SeleniumPomLab\\build\\reports\\allure-report/**', 
                           fingerprint: true,
                           allowEmptyArchive: true
            
            junit testResults: 'D:\\Development\\cursoQA\\SeleniumPomLab\\build\\test-results/**/*.xml', 
                  allowEmptyResults: true
        }

        success {
            echo '✓ Tests completados exitosamente'
        }

        failure {
            echo '✗ Tests fallaron'
        }
    }
}
```

## Configurar Ejecución Programada

### En Jenkins (Recomendado)

En la configuración del Job:

**Build Triggers:**
- ☑ **Poll SCM** → Schedule:
  - `H 8 * * *` → Diario a las 8 AM
  - `H 8 * * 1-5` → Lunes a Viernes a las 8 AM
  - `H 8,14 * * *` → Diario a las 8 AM y 2 PM
  - `0 2 * * 0` → Domingos a las 2 AM

### En Windows 11 (Tareas Programadas)

Si prefieres usar el Programador de Tareas de Windows:

1. Abrir **Programador de Tareas** (Task Scheduler)
2. Crear tarea básica:
   - Nombre: `Demoblaze-Tests-Scheduled`
   - Trigger: Diario, 8:00 AM
   - Acción: Iniciar programa
     - Programa: `C:\Program Files\Java\jdk-21\bin\java.exe`
     - Argumentos: `-jar C:\Program Files\Jenkins\jenkins.war`

O ejecutar directamente:

```powershell
# PowerShell (Admin)
$action = New-ScheduledTaskAction -Execute 'powershell.exe' `
  -Argument '-NoProfile -WindowStyle Hidden -Command "cd D:\Development\cursoQA\SeleniumPomLab; .\gradlew.bat test --tests \"com.demoblaze.runner.CucumberTestRunnerDemoblaze\"; .\gradlew.bat allureReport"'

$trigger = New-ScheduledTaskTrigger -Daily -At "08:00"

$principal = New-ScheduledTaskPrincipal -UserID "$env:USERNAME" -LogonType Interactive -RunLevel Highest

Register-ScheduledTask -TaskName "Demoblaze-Tests" -Action $action -Trigger $trigger -Principal $principal
```

## Ejecutar Pipeline Manualmente

1. En Jenkins, ir al job `SeleniumPomLab-Tests`
2. Click en **Build Now**
3. Ver logs en tiempo real: Click en el build → **Console Output**
4. Ver reporte Allure: Link **Allure Report** en la página del build

## Verificar Reportes

- **Reporte Allure:** `http://localhost:8080/job/SeleniumPomLab-Tests/allure/`
- **Test Results:** `http://localhost:8080/job/SeleniumPomLab-Tests/testReport/`
- **Build Artifacts:** `http://localhost:8080/job/SeleniumPomLab-Tests/lastSuccessfulBuild/artifact/`

## Troubleshooting

### Error: "java command not found"

- Verificar `JAVA_HOME` en Jenkins: **Manage Jenkins** → **Global Tool Configuration**
- Configurar variable de entorno en Windows:
  ```powershell
  setx JAVA_HOME "C:\Program Files\Java\jdk-21"
  ```

### Error: "gradlew.bat no encontrado"

- Verificar que el workspace esté correcto
- Clonar o copiar repo a la ruta del agente

### Allure Report no se genera

- Verificar que **Allure Plugin** esté instalado
- Verificar path de resultados: `build/allure-results`
- En logs, buscar: "Allure Report successfully generated"

### El Pipeline tarda mucho

- Primera ejecución descarga dependencias Gradle
- Considerarelimitar tests o usar Gradle cache
- Aumentar timeout en pipeline si es necesario

## Conclusión

Una vez configurado:
1. Jenkins ejecuta tests diariamente a las 8 AM
2. Genera reportes Allure automáticamente
3. Archiva resultados para historial
4. Notifica si tests fallan (con configuración adicional de emails)

¡Listo! Ya tienes CI/CD configurado.
