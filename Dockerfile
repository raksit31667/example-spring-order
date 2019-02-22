FROM redhat-openjdk-18/openjdk18-openshift
COPY ./api-service/build/libs/api-service-0.0.1.jar /app/

# Define variables for actuator endpoint /info
ARG JENKINS_BUILD_NUMBER
ARG GIT_HASH
ENV BUILD_NUMBER $JENKINS_BUILD_NUMBER
ENV GIT_HASH $GIT_HASH

WORKDIR /app
EXPOSE 8080
ENTRYPOINT java -Xmx1024m -XX:MaxMetaspaceSize=100m -jar api-service-0.0.1.jar