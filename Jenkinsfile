pipeline {
    agent any
    
    
    environment {
        DOCKER_IMAGE = 'employee_management'
        DOCKER_USERNAME = 'devbntnp' 
        GIT_REPO_URL = 'https://github.com/Jayesh2026/employee-management.git'
    }

    triggers {
        githubPush()
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: env.GIT_REPO_URL
                sh 'git branch'
            }
        }
        
        stage('Setup') {
            steps {
                // Set permission with gradlew
                sh 'chmod +x ./gradlew'
                
                // Check Java version to confirm it's working
                sh 'java -version'
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }
        
       stage('Check Docker') {
            steps {
                script {
                    try {
                        sh 'docker --version'
                        echo "Docker is available"
                        env.DOCKER_AVAILABLE = 'true'
                    } catch (Exception e) {
                        echo "Docker is not available: ${e.message}"
                        env.DOCKER_AVAILABLE = 'false'
                    }
                }
            }
        }
                
        stage('Docker Build') {
            when {
                expression { return env.DOCKER_AVAILABLE == 'true' }
            }
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
                sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest'
            }
        }
        
        stage('Docker Push') {
            when {
                allOf {
                    expression { return env.DOCKER_AVAILABLE == 'true' }
                }
            }
            steps {
                withCredentials([string(credentialsId: 'docker-credentials', variable: 'DOCKER_AUTH')]) {
                    sh 'echo $DOCKER_AUTH | docker login -u ${DOCKER_USERNAME} --password-stdin'
                    sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${BUILD_NUMBER}'
                    sh 'docker push ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${BUILD_NUMBER}'
                }
            }
        }
        
        stage('Deploy') {
            when {
                allOf {
                    expression { return env.DOCKER_AVAILABLE == 'true' }
                }
            }
            steps {
                sh 'docker compose down || true'
                sh 'docker compose up -d'
            }
        }
    }
    
    post {
        always {
            sh 'docker system prune -f || true'
            cleanWs()
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline execution failed!'
        }
    }
}

