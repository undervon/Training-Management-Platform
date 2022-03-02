#
# Build target file whit .jar
#
# FROM maven:3.8.4-jdk-11 AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package

#
# Package image from .jar
#
FROM adoptopenjdk/openjdk11
# COPY --from=build /home/app/target/authentication.authorization.jwt-0.0.1-SNAPSHOT.jar /usr/local/lib/2auth.jwt.jar
# ENTRYPOINT ["java","-jar","/usr/local/lib/2auth.jwt.jar"]
COPY target/authentication.authorization.jwt-0.0.1-SNAPSHOT.jar 2auth.jwt.jar
ENTRYPOINT ["java","-jar","/2auth.jwt.jar"]