#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
COPY target/registry.service-0.0.1-SNAPSHOT.jar registry.service.jar
ENTRYPOINT ["java", "-jar", "/registry.service.jar"]

#
# Command:
#
# docker build -t registry-service-app .                    -> using for create a docker image with the app
#