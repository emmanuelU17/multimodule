FROM eclipse-temurin:21 as builder

WORKDIR /build

COPY . /build

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=builder /build/target/webserver-exec.jar /app

EXPOSE 8080

CMD ["java", "-jar", "/app/webserver-exec.jar"]