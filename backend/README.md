# Backend

Spring Boot 3, Java 21 backend for AI Production Intelligence.

Build locally:

  mvn -f backend/pom.xml clean package

Run locally (requires Java 21):

  java -jar backend/target/backend-0.0.1-SNAPSHOT.jar

Docker (build and run):

  docker build -t aiprod-backend ./backend
  docker run -p 8080:8080 -e CLICKHOUSE_HOST=clickhouse -e CLICKHOUSE_PORT=8123 aiprod-backend

Endpoints:
- Health (Actuator): /actuator/health
- Ping: /api/v1/ping

