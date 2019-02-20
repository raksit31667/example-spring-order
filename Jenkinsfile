pipeline {
    agent any
    environment {
        serviceName = "order"
    }
    stages {
        stage("Clone") {
            steps {
                git branch: "master"
                    url: "git@github.com:raksit31667/example-spring-order.git",
                    credentialsId: "github"
            }
        }
        stage("Unit test") {
            steps {
                sh "./gradlew test -i"
            }
        }
        stage("Isolation test") {
            steps {
                sh "./gradlew isolationTest -i"
            }
        }
        stage("Build") {
            steps {
                sh "./gradlew build -i"
            }
        }
    }
}
