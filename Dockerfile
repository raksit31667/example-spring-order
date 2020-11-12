FROM redhat-openjdk-18/openjdk18-openshift
COPY ./api-service/build/libs/api-service-0.0.1.jar /app/

# Define variables for actuator endpoint /info
ARG JENKINS_BUILD_NUMBER
ARG GIT_HASH
ENV BUILD_NUMBER $JENKINS_BUILD_NUMBER
ENV GIT_HASH $GIT_HASH

WORKDIR /app
RUN curl -k https://repository.sonatype.org/service/local/repositories/central-proxy/content/com/datadoghq/dd-java-agent/0.46.0/dd-java-agent-0.46.0.jar --output dd-java-agent.jar
EXPOSE 8080
ENTRYPOINT java -javaagent:'dd-java-agent.jar' -Xmx1024m -XX:MaxMetaspaceSize=100m -jar api-service-0.0.1.jar