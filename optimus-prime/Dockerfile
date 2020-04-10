FROM openjdk:alpine

MAINTAINER ryan.wibawa@gmail.com

WORKDIR /maven
ADD ./dist/optimus-prime-0.0.1-SNAPSHOT.jar ./optimus-prime-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","optimus-prime-0.0.1-SNAPSHOT.jar"]
