#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
COPY target/send.email.microservice-0.0.1-SNAPSHOT.jar send.email.jar
ENTRYPOINT ["java", "-jar", "/send.email.jar"]

#
# Command:
#
# docker build -t send-email-app .                    -> using for create a docker image with the app
#