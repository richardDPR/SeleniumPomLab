pipeline {
    agent { label 'windows' }

    stages {
        stage('Checkout') {
            steps {
                // Si tu Jenkins está conectado a Git, puedes hacer checkout aquí
                // checkout scm
                echo 'Código fuente preparado'
            }
        }

        stage('Ejecutar Pruebas') {
            steps {
                dir('D:\\Development\\cursoQA\\SeleniumPomLab') {
                    bat '.\\gradlew.bat clean test'
                }
            }
        }

        stage('Generar Reporte Allure') {
            steps {
                dir('D:\\Development\\cursoQA\\SeleniumPomLab') {
                    bat '.\\gradlew.bat allureReport'
                }
            }
        }

        stage('Publicar Reporte Allure') {
            steps {
                dir('D:\\Development\\cursoQA\\SeleniumPomLab') {
                    allure([
                        commandline: 'Allure_2.42.0',   // mismo binario que usaste en Cypress
                        includeProperties: false,
                        jdk: '',
                        resultPolicy: 'LEAVE_AS_IS',
                        results: [[path: 'build/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado. Logs y reportes disponibles.'
        }
    }
}
