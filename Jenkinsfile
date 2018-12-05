pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v maven-repository:/root/.m2 -p 19999:19999'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}