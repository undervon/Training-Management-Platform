#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
COPY target/courses.microservice-0.0.1-SNAPSHOT.jar courses.jar
ENTRYPOINT ["java", "-jar", "/courses.jar"]

#
# Command:
#
# docker build -t courses-app .                    -> using for create a docker image with the app
#