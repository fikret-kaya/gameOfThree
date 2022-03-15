FROM openjdk:11
LABEL MAINTAINER="<Fikret KAYA> fikret.ky93@gmail.com"

WORKDIR /app

COPY target/game-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]