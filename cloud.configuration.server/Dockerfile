#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
COPY target/cloud.configuration.server-0.0.1-SNAPSHOT.jar cloud.configuration.server.jar
ENTRYPOINT ["java", "-jar", "/cloud.configuration.server.jar"]

#
# Command:
#
# docker build -t cloud-configuration-server-app .                    -> using for create a docker image with the app
#