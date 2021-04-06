# example-spring-order

![Order CI](https://github.com/raksit31667/example-spring-order/workflows/Order%20CI/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=raksit31667_example-spring-order&metric=alert_status)](https://sonarcloud.io/dashboard?id=raksit31667_example-spring-order)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=raksit31667_example-spring-order&metric=coverage)](https://sonarcloud.io/dashboard?id=raksit31667_example-spring-order)

Example RESTful Order API powered by Spring Boot

## Directory structure

Here is the basic skeleton for your app repo that each of the starter templates conforms to:

```bash
├── .github
├── api-service
│   ├── src
│   │   ├── integration-test
│       ├── main
│       ├── test
│       ├── build.gradle # Define app dependencies here
├── config
├── gradle
│   ├── wrapper
├── helm
├── openshift
├── performance-test
├── docker-compose.yml
├── Dockerfile
├── build.gradle # Define global dependencies here
```

- Your app's source code is nested beneath the `api-service` directory.
  It composes of submodule for main logic, unit testing, and integration testing.
  Note that for app-specific dependencies, it is recommended to configure in `build.gradle` within this directory.

- You can configure coding standards, dependency vulnerability checks in `config` directory.
  I already provided **checkStyle** with simple Java rules.

- For compatibility with Kubernetes, Simple Helm chart is located in `helm` directory.

- Your app's performance testing scripts, written in Scala, is nested beneath the `perfomance-test` directory.
  Please follow the Gatling DSL guideline [here](https://gatling.io/docs/current/).

- It is also recommended updating `docker-compose.yml` if your app requires external components,
  such as database, and message queue to test locally.

- The `build.gradle` in root path is used for defining global configuration. In this case, I provided
  simple SonarQube and Docker build & publish plugins.
  
- This app is also aimed to be deployed in [RedHat OpenShift](https://www.openshift.com/). 
  The deployment script, partially used in `Jenkinsfile`, is located in `openshift` directory.

## Getting started
Let's get your app up and running on your local machine. It should only take a few minutes.

### Build
```shell
./gradlew clean build
```

### Run
Before starting app, make sure that core dependencies are up and running. 
You can also run all of them from scratch by running `docker-compose` file:

```shell
docker-compose up -d
```

Then, run Spring app (if no environment specified, it will be `local` by default):
```shell
./gradlew bootRun
```

## Scope
- RESTful CRUD API with Spring Boot
- Multi-modules configuration with Gradle  
- Accessing data with JPA
- Using MapStruct with Project Lombok
- API testing with RestAssured
- Layered architecture pattern
- CICD with OpenShift and Jenkins
- Test doubles with Mockito
- Global exception handler in Spring
- API documentation with Swagger v2
- Static code analysis with SonarQube
- Coding standards with [checkStyle](https://checkstyle.org/) and [PMD](https://pmd.github.io/)
- Database migration with Flyway
- Integration testing with embedded database
- Spring for Apache Kafka
- Spring security with OAuth2 and Microsoft Azure
- CICD with GitHub Actions
- App observability with Datadog and Prometheus
- Spring for Kubernetes and Helm
- Spring for Redis