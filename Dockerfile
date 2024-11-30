FROM eclipse-temurin:21
EXPOSE 7000
RUN mkdir /opt/app
COPY build/comeon-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/comeon-0.0.1-SNAPSHOT.jar"]
