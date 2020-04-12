FROM openjdk:alpine

MAINTAINER ryan.wibawa@gmail.com

WORKDIR /maven
ADD ./dist/gears-0.0.1-SNAPSHOT.jar ./gears-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT exec java ${JAVA_OPTS} -jar gears-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","$JAVA_OPTS","-jar","gears-0.0.1-SNAPSHOT.jar"]
