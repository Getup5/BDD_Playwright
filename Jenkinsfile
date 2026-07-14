pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    tools {
        maven 'MAVEN_HOME'
    }

    parameters {
        string(
            name:         'BASE_URL',
            defaultValue: 'http://localhost:4200',
            description:  'Shell app base URL (passed in by the main pipeline)'
        )
        string(
            name:         'BACKEND_URL',
            defaultValue: 'http://localhost:8080',
            description:  'API Gateway base URL (passed in by the main pipeline)'
        )
        string(
            name:         'TRIGGERED_BY',
            defaultValue: 'manual',
            description:  'Which pipeline/user triggered this run'
        )
    }

    environment {
        ALLURE_RESULTS = 'allure-results'
    }

    stages {

        stage('Checkout') {
            steps {
                cleanWs()
                echo '=========================== Checking out automation repo... ==========================='
                checkout scm
            }
        }

        stage('Wait for Frontend & Backend') {
            // This is the fix: never assume BASE_URL / BACKEND_URL are already up.
            // Fails fast with a clear message instead of letting every test fail with
            // "connection refused" one by one.
            steps {
                echo "Triggered by: ${params.TRIGGERED_BY}"
                echo "Waiting for frontend at ${params.BASE_URL} ..."
                bat """
                setlocal enabledelayedexpansion
                set RETRIES=20
                :WAIT_FE
                curl -s -o nul -w "%%{http_code}" "${params.BASE_URL}" > fe_status.txt
                set /p STATUS=<fe_status.txt
                if "!STATUS!"=="200" goto FE_READY
                set /a RETRIES-=1
                if !RETRIES! EQU 0 (
                    echo ERROR: Frontend at ${params.BASE_URL} is not reachable.
                    echo This pipeline does not start the frontend/backend itself —
                    echo it must be triggered by the main build/deploy pipeline, or the
                    echo apps must already be running before this job starts.
                    exit /b 1
                )
                ping -n 4 127.0.0.1 >nul
                goto WAIT_FE
                :FE_READY
                echo Frontend is reachable.
                endlocal
                """

                echo "Waiting for backend at ${params.BACKEND_URL}/actuator/health ..."
                bat """
                setlocal enabledelayedexpansion
                set RETRIES=20
                :WAIT_BE
                curl -s -o nul -w "%%{http_code}" "${params.BACKEND_URL}/actuator/health" > be_status.txt
                set /p STATUS=<be_status.txt
                if "!STATUS!"=="200" goto BE_READY
                set /a RETRIES-=1
                if !RETRIES! EQU 0 (
                    echo ERROR: Backend gateway at ${params.BACKEND_URL} is not reachable.
                    echo This pipeline does not start the frontend/backend itself —
                    echo it must be triggered by the main build/deploy pipeline, or the
                    echo apps must already be running before this job starts.
                    exit /b 1
                )
                ping -n 4 127.0.0.1 >nul
                goto WAIT_BE
                :BE_READY
                echo Backend is reachable.
                endlocal
                """
            }
        }

        stage('Run Automation Tests') {
            steps {
                echo '=========================== Running Cucumber tests... ==========================='
                // Forward the actual URLs into the test run instead of relying on
                // whatever default is hardcoded in the test config.
                bat """
                mvn clean test ^
                    -DBASE_URL=${params.BASE_URL} ^
                    -DBACKEND_URL=${params.BACKEND_URL}
                """
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure report...'
                script {
                    if (fileExists('allure-results')) {
                        try {
                            allure([
                                includeProperties: false,
                                jdk:              '',
                                commandline:      'allure',
                                results:          [[path: 'allure-results']]
                            ])
                        } catch (Exception ex) {
                            echo "Allure report generation failed: ${ex.getMessage()}"
                        }
                    } else {
                        echo 'No allure-results folder found — skipping report.'
                    }
                }
            }
        }
    }

    post {
        always {
            echo '--- Publishing Cucumber JSON Report ---'
            publishHTML([
                allowMissing:          true,
                alwaysLinkToLastBuild: true,
                keepAll:               true,
                reportDir:             'target/cucumber-reports',
                reportFiles:           'cucumber.json',
                reportName:            'Cucumber Report'
            ])
        }
        success {
            echo '=========================== AUTOMATION TESTS PASSED ✅ ==========================='
        }
        failure {
            echo '=========================== AUTOMATION TESTS FAILED ❌ ==========================='
        }
    }
}
