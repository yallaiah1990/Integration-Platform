FROM orion-team-docker-prod.mia.ulti.io/launch/launch-java:latest

COPY target/project-query.jar /app.jar
EXPOSE 8080

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]