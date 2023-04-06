FROM openjdk:19
COPY ./target/ComixRest-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "ComixRest-0.0.1-SNAPSHOT.jar"]