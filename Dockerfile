FROM openjdk:21-oracle
COPY target/BlogifyHub-0.0.1-SNAPSHOT.jar blogifyhub.jar
EXPOSE 2000
ENTRYPOINT ["java", "-jar", "blogifyhub.jar"]