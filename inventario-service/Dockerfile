FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/inventario-service-0.0.1-SNAPSHOT.jar"]