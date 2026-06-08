# Script PowerShell para automatizar configuracion de Jenkins
# Ejecutar como Administrador en PowerShell

param(
    [switch]$Help,
    [switch]$CheckEnv,
    [switch]$SetupEnv,
    [switch]$CreateTask,
    [switch]$All
)

$ErrorActionPreference = "Stop"

# Funciones
function Write-Header {
    param([string]$Message)
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host " $Message" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
}

function Write-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Write-Error {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ $Message" -ForegroundColor Yellow
}

# Variables
$JAVA_HOME = "C:\Program Files\Java\jdk-21"
$JENKINS_HOME = "C:\Program Files\Jenkins"
$PROJECT_PATH = "D:\Development\cursoQA\SeleniumPomLab"
$JENKINS_PORT = 8080
$JENKINS_WORKSPACE = "D:\jenkins-workspace"

function Test-Admin {
    $admin = [Security.Principal.WindowsPrincipal]::new([Security.Principal.WindowsIdentity]::GetCurrent())
    return $admin.IsInRole([Security.Principal.WindowsBuiltinRole]::Administrator)
}

function Show-Help {
    Write-Host @"
JENKINS SETUP SCRIPT - Windows 11 QA Automation

Uso: .\setup_jenkins.ps1 [opciones]

Opciones:
  -Help           Mostrar esta ayuda
  -CheckEnv       Verificar variables de entorno requeridas
  -SetupEnv       Configurar variables de entorno
  -CreateTask     Crear tarea programada en Windows
  -All            Ejecutar todas las acciones

Ejemplos:
  .\setup_jenkins.ps1 -CheckEnv
  .\setup_jenkins.ps1 -All
  .\setup_jenkins.ps1 -SetupEnv -CreateTask

"@
}

function Check-Environment {
    Write-Header "Verificar Entorno"
    
    $errors = 0
    
    # Verificar Java
    if (Test-Path "$JAVA_HOME\bin\java.exe") {
        Write-Success "Java 21 encontrado: $JAVA_HOME"
        & "$JAVA_HOME\bin\java.exe" -version 2>&1 | ForEach-Object { Write-Info "  $_" }
    } else {
        Write-Error "Java 21 NO encontrado en: $JAVA_HOME"
        $errors++
    }
    
    # Verificar Gradle
    if (Test-Path "$PROJECT_PATH\build.gradle") {
        Write-Success "Proyecto Gradle encontrado: $PROJECT_PATH"
    } else {
        Write-Error "Proyecto Gradle NO encontrado: $PROJECT_PATH"
        $errors++
    }
    
    # Verificar Jenkinsfile
    if (Test-Path "$PROJECT_PATH\Jenkinsfile") {
        Write-Success "Jenkinsfile encontrado"
    } else {
        Write-Error "Jenkinsfile NO encontrado"
        $errors++
    }
    
    # Verificar Jenkins instalado
    if (Get-Service -Name "Jenkins" -ErrorAction SilentlyContinue) {
        Write-Success "Servicio Jenkins está registrado"
    } else {
        Write-Info "Servicio Jenkins no encontrado (será instalado después)"
    }
    
    if ($errors -eq 0) {
        Write-Host "`n✓ Todas las verificaciones pasaron" -ForegroundColor Green
    } else {
        Write-Host "`n✗ Se encontraron $errors errores" -ForegroundColor Red
        exit 1
    }
}

function Setup-Environment {
    Write-Header "Configurar Variables de Entorno"
    
    try {
        # JAVA_HOME
        [Environment]::SetEnvironmentVariable("JAVA_HOME", $JAVA_HOME, "Machine")
        Write-Success "JAVA_HOME configurado globalmente"
        
        # PROJECT_PATH
        [Environment]::SetEnvironmentVariable("PROJECT_PATH", $PROJECT_PATH, "Machine")
        Write-Success "PROJECT_PATH configurado globalmente"
        
        # Crear directorios
        if (-not (Test-Path $JENKINS_WORKSPACE)) {
            New-Item -ItemType Directory -Path $JENKINS_WORKSPACE -Force | Out-Null
            Write-Success "Directorio de workspace creado: $JENKINS_WORKSPACE"
        }
        
        # Actualizar PATH si es necesario
        $currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
        if (-not $currentPath.Contains($JAVA_HOME)) {
            $newPath = "$currentPath;$JAVA_HOME\bin"
            [Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
            Write-Success "JAVA_HOME añadido al PATH"
        }
        
        Write-Host "`nℹ Variables de entorno configuradas. Reinicia PowerShell para aplicar cambios." -ForegroundColor Yellow
    }
    catch {
        Write-Error "Error al configurar variables: $_"
        exit 1
    }
}

function Create-ScheduledTask {
    Write-Header "Crear Tarea Programada"
    
    try {
        # Definir accion
        $actionScript = @"
            cd '$PROJECT_PATH'
            .\gradlew.bat test --tests "com.demoblaze.runner.CucumberTestRunnerDemoblaze"
            .\gradlew.bat allureReport
            Start-Sleep -Seconds 10
"@
        
        $action = New-ScheduledTaskAction -Execute "powershell.exe" `
            -Argument "-NoProfile -WindowStyle Hidden -ExecutionPolicy Bypass -Command `"$actionScript`""
        
        # Definir trigger (diario a las 8 AM)
        $trigger = New-ScheduledTaskTrigger -Daily -At "08:00AM"
        
        # Configuracion adicional
        $settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries `
            -DontStopIfGoingOnBatteries -StartWhenAvailable -RunOnlyIfNetworkAvailable
        
        # Principal (usuario actual con privilegios elevados)
        $principal = New-ScheduledTaskPrincipal -UserID "$env:USERNAME" `
            -LogonType Interactive -RunLevel Highest
        
        # Registrar tarea
        $taskName = "DemoblazeCucumberTests"
        
        if (Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue) {
            Write-Info "Tarea ya existe. Actualizando..."
            Set-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Settings $settings
        } else {
            Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger `
                -Settings $settings -Principal $principal -Description "Ejecuta tests Demoblaze diariamente a las 8 AM" | Out-Null
            Write-Success "Tarea programada creada: $taskName"
        }
        
        Write-Info "Programacion: Diario a las 08:00 AM"
        Write-Info "Para modificar: Abrir 'Programador de Tareas' (Task Scheduler)"
    }
    catch {
        Write-Error "Error al crear tarea programada: $_"
        exit 1
    }
}

function Show-NextSteps {
    Write-Header "Proximos Pasos"
    
    Write-Host @"
1. DESCARGAR JENKINS
   - Ir a: https://www.jenkins.io/download/
   - Descargar: Jenkins for Windows (.msi)
   - O jenkins.war para ejecucion en JAR

2. INSTALAR JENKINS
   - Ejecutar installer como Admin
   - Puerto: 8080 (default)
   - Username: (tu usuario)
   - Password: (contraseña)
   - Service: "Start Jenkins at system startup"

3. INICIALIZAR JENKINS
   - Abrir: http://localhost:8080
   - Copiar contraseña inicial de:
     C:\Program Files\Jenkins\secrets\initialAdminPassword
   - Completar wizard

4. INSTALAR PLUGINS
   - Manage Jenkins > Manage Plugins
   - Instalar:
     * Allure Plugin
     * Timestamper Plugin
     * Pipeline plugin

5. CONFIGURAR HERRAMIENTAS
   - Manage Jenkins > Global Tool Configuration
   - JDK Installation:
     Name: JDK-21
     JAVA_HOME: $JAVA_HOME
   - Allure Commandline:
     Name: Allure_2.42.0
     (Auto-download)

6. CREAR JOB PIPELINE
   - New Item > Pipeline
   - Name: SeleniumPomLab-Tests
   - Pipeline > Definition: Pipeline script from SCM
   - SCM: Git
   - Repository: (tu repo)
   - Script Path: Jenkinsfile

7. CONFIGURAR BUILD TRIGGERS
   - Poll SCM: H 8 * * * (Diario 8 AM)
   - Webhook (opcional)

8. EJECUTAR PRIMERA BUILD
   - Click: Build Now
   - Ver: Console Output
   - Ver reporte: http://localhost:8080/job/SeleniumPomLab-Tests/allure/

"@
}

# Main
if (-not (Test-Admin)) {
    Write-Error "Este script requiere permisos de Administrador"
    Write-Host "Ejecutar PowerShell como Administrador" -ForegroundColor Yellow
    exit 1
}

if ($Help) {
    Show-Help
    exit 0
}

if ($All -or $CheckEnv) {
    Check-Environment
}

if ($All -or $SetupEnv) {
    Setup-Environment
}

if ($All -or $CreateTask) {
    Create-ScheduledTask
}

if ($All -or $CheckEnv -or $SetupEnv -or $CreateTask) {
    Show-NextSteps
}

if (-not ($Help -or $CheckEnv -or $SetupEnv -or $CreateTask -or $All)) {
    Write-Host "Uso: .\setup_jenkins.ps1 -Help" -ForegroundColor Yellow
    Show-Help
}
