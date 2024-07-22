FROM maven:3.8.5-openjdk-22-slim
WORKDIR /app
COPY SWP/target/SWP-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java","-jar","/app/app.jar"]
