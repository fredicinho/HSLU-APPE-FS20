FROM openjdk:11.0.5-jre-slim
LABEL maintainer="roland.christen@hslu.ch"

# add jar to image
COPY ./target/service.jar service.jar

# Startup
CMD java ${JAVA_OPTS} -jar ./service.jar
