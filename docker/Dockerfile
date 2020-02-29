FROM openjdk:8-jdk-alpine
WORKDIR /
ADD target/capser-1.0-SNAPSHOT.jar capser.jar
EXPOSE 8080
EXPOSE $PORT
CMD ["java","-jar","capser.jar"]