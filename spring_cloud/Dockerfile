FROM openjdk:latest
FROM gradle:latest
RUN ls
RUN pwd
COPY ./ /home/gradle
RUN gradle build
RUN mkdir /app && cp /home/gradle/build/libs/cloud-0.0.1-beta.jar /app/app.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
EXPOSE 8080
WORKDIR /app
CMD java -jar -Dspring.profiles.active=main app.jar
