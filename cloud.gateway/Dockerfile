#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
EXPOSE 9000
COPY target/cloud.gateway-0.0.1-SNAPSHOT.jar cloud.gateway.jar
ENTRYPOINT ["java", "-jar", "/cloud.gateway.jar"]

#
# Command:
#
# docker build -t cloud-gateway-app .                    -> using for create a docker image with the app
#