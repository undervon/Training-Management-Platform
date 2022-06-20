#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
EXPOSE 8093
COPY target/assigned.courses.microservice-0.0.1-SNAPSHOT.jar assigned.courses.jar
ENTRYPOINT ["java", "-jar", "/assigned.courses.jar"]

#
# Command:
#
# docker build -t assigned-courses-app .                    -> using for create a docker image with the app
#