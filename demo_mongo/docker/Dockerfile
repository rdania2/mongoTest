FROM gradle:jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew build --no-daemon

FROM openjdk:11.0.3-jre-slim-stretch

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/demo.jar"]