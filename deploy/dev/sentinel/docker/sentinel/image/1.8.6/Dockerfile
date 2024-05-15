FROM openjdk:18-jdk-alpine

ARG APP_PATH=./image/1.8.6/
ARG APP_FILE=sentinel-dashboard-1.8.6.jar

ARG JAR_PATH=/app/sentinel/
ENV JAR_FILE=$APP_FILE

ENV JAVA_OPTS="-Dserver.port=8858 -Dcsp.sentinel.dashboard.server=localhost:8858 -Dproject.name=sentinel-dashboard "

RUN mkdir -p $JAR_PATH
COPY $APP_PATH$APP_FILE $JAR_PATH$JAR_FILE
WORKDIR $JAR_PATH

EXPOSE 8858
ENTRYPOINT java $JAVA_OPTS -jar $JAR_FILE

#docker build -t app/sentinel:1.8.6 .
#docker run -it --rm -p8858:8858 --name sentinel app/sentinel:1.8.6
#docker rmi app/sentinel:1.8.6