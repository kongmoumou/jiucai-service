FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=./*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Shanghai
ENTRYPOINT ["java","-jar","/app.jar"]
