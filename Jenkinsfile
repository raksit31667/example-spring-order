def gitCommitId = null

pipeline {
    agent any
    environment {
        serviceName = "order"
        namespace = "raksit"
        replicaCount = 1
    }
    stages {
        stage("Clone") {
            steps {
                script {
                    git branch: "master",
                            url: "git@github.com:raksit31667/example-spring-order.git",
                            credentialsId: "github"

                    gitCommitId = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
                }
            }
        }
        stage("Check style & pmd") {
            steps {
                sh "./gradlew checkStyleMain -i"
                sh "./gradlew pmdMain -i"
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
        stage('OWASP Dependency Check') {
            sh 'gradle dependencyCheckAggregate'
        }
        stage("Sonar Check") {
            steps {
                sh "./gradlew sonarqube -Dsonar.host.url=https://sonarqube-raksit31667.1d35.starter-us-east-1.openshiftapps.com -Dsonar.projectVersion=${gitCommitId}"
            }
        }
        stage("Build") {
            steps {
                sh "./gradlew build -i"
                sh "tar cvf ${serviceName}.tar Dockefile api-service/build/libs/api-service-0.0.1.jar"
                stash name: "package", includes: "${serviceName}.tar"
            }
        }
        stage("Build Image and push it to ImageStream") {
            steps {
                unstash name: "package"
                sh "oc process -f ${serviceName}-build-and-imagestream.yaml -p JENKINS_BUILD_NUMBER=${env.BUILD_NUMBER} -p GIT_HASH=${gitCommitId} | oc apply --namespace='${namespace}' -f -"
                sh "oc start-build ${serviceName} --from-archive=${serviceName}.tar --follow --namespace='${namespace}'"
            }
        }
        stage("Tag Image") {
            steps {
                sh "oc tag ${namespace}/${serviceName}:latest ${namespace}/${serviceName}:${gitCommitId} --namespace='${namespace}'"
            }
        }
        stage("Deploy") {
            steps {
                script {
                    def dockerRegistryAndImageName = sh(
                            script: "oc get imagestream --namespace='${namespace}' ${serviceName} -o='jsonpath={.status.dockerImageRepository}'",
                            returnStdout: true).trim()

                    sh "oc process openshift/${serviceName}-deployment-config.yaml --ignore-unknown-parameters=true -p REPLICAS='${replicaCount}' DOCKER_REGISTRY='${dockerRegistryAndImageName}:${gitCommitId}' -p SPRING_PROFILES_ACTIVE='dev' | oc apply --namespace='${namespaceDev}' -f -"
                    openshiftDeploy depCfg: "${serviceName}-deployment", namespace: "${namespace}"
                    sh "oc rollout status dc ${serviceName}-deployment -w --namespace='${namespace}'"
                    sh "oc apply openshift/${serviceName}-service.yaml --namespace='${namespace}' -f -"
                }
            }
        }
        stage('Verify Deployment') {
            steps {
                script {
                    try {
                        verifyDeployment(namespace, serviceName, replicaCount)
                    } catch (Throwable e) {
                        sh "oc rollback ${serviceName} --namespace='${namespace}'"
                        throw e
                    }
                }
            }
        }
    }
}
