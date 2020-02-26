FROM openjdk:8-jre-alpine
WORKDIR /
ADD target/capser-1.0-SNAPSHOT.jar capser.jar
EXPOSE 8888
EXPOSE $PORT
CMD ["java","-jar","capser.jar"]