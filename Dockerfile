FROM openjdk:11.0-jdk-slim
COPY build/libs/*.jar app.jar

#ENV JAVA_OPT "-XX:+PrintFlagsFinal -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=150 -XX:MaxRAMPercentage=75.0"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]