FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8083

CMD ["java", "-jar", "target/envios-service-0.0.1-SNAPSHOT.jar"]