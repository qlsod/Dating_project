FROM openjdk:11
ARG JAR_FILE=build/libs/dating-0.0.1-SNAPSHOT.jar.jar
COPY ${JAR_FILE} ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]