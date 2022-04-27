#
# Package image from .jar file
#
FROM adoptopenjdk/openjdk11
COPY target/authentication.authorization.jwt-0.0.1-SNAPSHOT.jar 2auth.jwt.jar
ENTRYPOINT ["java", "-jar", "/2auth.jwt.jar"]

#
# Command:
#
# docker build -t 2auth-jwt-app .                    -> using for create a docker image with the app
#