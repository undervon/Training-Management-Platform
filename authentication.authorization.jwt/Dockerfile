#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true

#
# Package stage
#
FROM adoptopenjdk/openjdk11
COPY target/authentication.authorization.jwt-0.0.1-SNAPSHOT.jar authentication.authorization.jwt-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/authentication.authorization.jwt-0.0.1-SNAPSHOT.jar"]