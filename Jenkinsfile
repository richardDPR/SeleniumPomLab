pipeline {
    agent { label 'windows-agent' }

    stages {
        stage('Check Java') {
            steps {
                bat '''
                set JAVA_HOME=C:\\Program Files\\Java\\jdk-21
                java -version
                '''
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
                        commandline: 'Allure_2.42.0',
                        includeProperties: false,
                        jdk: '',
                        resultPolicy: 'LEAVE_AS_IS',
                        results: [[path: 'build/allure-results']]
                    ])
                }
            }
        }
    }
}
