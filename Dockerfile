FROM openjdk:19-jdk-oracle
ADD src/main/resources/application.properties src/main/resources/application.properties
ADD target/CloudApi-0.0.1-SNAPSHOT.jar CloudApi.jar
CMD ["java", "-jar", "CloudApi.jar"]