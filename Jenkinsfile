pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'emp_management'
        DOCKER_USERNAME = 'jayesh2026'
        GIT_REPO_URL = 'https://github.com/Jayesh2026/employee-management.git'
        EMAIL = 'jayesh.savle@bnt-soft.com'
    }
    
    triggers {
        githubPush() // This enables the GitHub webhook trigger
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                url: env.GIT_REPO_URL
            }
        }

        stage('Set gradle permission') {
            steps {
                sh 'chmod +x ./gradlew'
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
        
        stage('Docker Build') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
                sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest'
            }
        }
        
        stage('Docker Push') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([string(credentialsId: 'docker-credentials', variable: 'DOCKER_AUTH')]) {
                    sh 'echo $DOCKER_AUTH | docker login -u ${DOCKER_USERNAME} --password-stdin'
                    sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${BUILD_NUMBER}'
                    sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_USERNAME}/${DOCKER_IMAGE}:latest'
                    sh 'docker push ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${BUILD_NUMBER}'
                    sh 'docker push ${DOCKER_USERNAME}/${DOCKER_IMAGE}:latest'
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }
    
    post {
        always {
            sh 'docker system prune -f'
            cleanWs()
        }
        success {
            echo 'Pipeline executed successfully!'
            updateGitHubCommitStatus(name: 'Jenkins Build', state: 'SUCCESS', message: 'Build succeeded!')
            mail to: "${EMAIL}", 
                 subject: 'Pipeline Success', 
                 body: 'The pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline execution failed!'
            updateGitHubCommitStatus(name: 'Jenkins Build', state: 'FAILURE', message: 'Build failed!')
            mail to: "${EMAIL}", 
                 subject: 'Pipeline Failure', 
                 body: 'The pipeline build failed. Please check the Jenkins logs.'
        }
    }
}

// Helper function to update GitHub commit status
void updateGitHubCommitStatus(Map args) {
    step([
        $class: 'GitHubCommitStatusSetter',
        reposSource: [$class: 'ManuallyEnteredRepositorySource', url: env.GIT_REPO_URL],
        contextSource: [$class: 'ManuallyEnteredCommitContextSource', context: args.name],
        errorHandlers: [[$class: 'ChangingBuildStatusErrorHandler', result: 'UNSTABLE']],
        statusResultSource: [$class: 'ConditionalStatusResultSource', results: [[$class: 'AnyBuildResult', message: args.message, state: args.state]]]
    ])
}