FROM openjdk:11.0.6-jre-slim
LABEL maintainer="roland.gisler@hslu.ch"

# add jar to image
COPY ./target/service.jar service.jar

# Startup
EXPOSE 8090/tcp
CMD java ${JAVA_OPTS} -jar ./service.jar
