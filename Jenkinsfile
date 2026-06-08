pipeline {
    agent { label 'windows' }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21'
        GRADLE_USER_HOME = '${WORKSPACE}\\.gradle'
        PATH = "${env.JAVA_HOME}\\bin;${env.PATH}"
        PROJECT_PATH = '${WORKSPACE}'
    }

    stages {
        stage('Validar Entorno') {
            steps {
                echo '====== Validando entorno de ejecución ======'
                bat 'echo WORKSPACE: ${WORKSPACE}'
                bat 'echo JAVA_HOME: %JAVA_HOME%'
                bat 'java -version'
                bat 'dir "%PROJECT_PATH%"'
            }
        }

        stage('Limpiar Build') {
            steps {
                echo '====== Limpiando directorios de build ======'
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat clean'
            }
        }

        stage('Compilar Código') {
            steps {
                echo '====== Compilando código de pruebas ======'
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat compileTestJava'
            }
        }

        stage('Ejecutar Pruebas Demoblaze') {
            steps {
                echo '====== Ejecutando suite Demoblaze ======'
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat test --tests "com.demoblaze.runner.CucumberTestRunnerDemoblaze" -x :allureReport'
            }
        }

        stage('Generar Reporte Allure') {
            steps {
                echo '====== Generando reporte Allure ======'
                bat 'cd /d "%PROJECT_PATH%" && .\\gradlew.bat allureReport'
            }
        }

        stage('Publicar Reporte Allure') {
            steps {
                echo '====== Publicando resultados Allure ======'
                allure([
                    commandline: 'Allure_2.42.0',
                    results: [[path: 'build/allure-results']],
                    reportBuildPolicy: 'ALWAYS'
                ])
            }
        }
    }

    post {
        always {
            echo '====== Post-ejecución ======'
            archiveArtifacts artifacts: 'build/reports/allure-report/**,build/test-results/**,build/allure-results/**', 
                             fingerprint: true,
                             allowEmptyArchive: true
            
            junit testResults: 'build/test-results/**/*.xml', 
                  allowEmptyResults: true,
                  skipPublishingChecks: true
        }

        success {
            echo '✓ Pipeline completado exitosamente'
        }

        failure {
            echo '✗ Pipeline falló. Revisar logs y artifacts.'
        }

        unstable {
            echo '⚠ Pipeline inestable. Algunos tests pueden haber fallado.'
        }
    }
}
