FROM openjdk:8-jdk-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ARG JAR_FILE=target/*.jar
WORKDIR /usr/app/
COPY ${JAR_FILE} /usr/app/app.jar
COPY config/application.properties /usr/app/application.properties
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
CMD ["--spring.config.location=classpath:/usr/app/application.properties"]