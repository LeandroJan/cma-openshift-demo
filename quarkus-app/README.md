# demo-quarkus

Quarkus-based replacement for the demo Spring Boot app using an in-memory H2 database and native image build (GraalVM).

## Prerequisites

- Java 17 SDK
- Maven 3.8+
- GraalVM native-image tool (can also use the Quarkus provided container image for native builds)
- (Optional) Docker if you want to containerize the native binary

## Build & Run in JVM mode (development)

```bash
./mvnw compile quarkus:dev
```

Endpoints available after startup:
- REST API: `http://localhost:8080/items`
- OpenAPI: `http://localhost:8080/q/swagger-ui`
- Metrics: `http://localhost:8080/metrics`
- Health: `http://localhost:8080/q/health/ready` and `/q/health/live`

## Build native image (requires GraalVM)

```bash
./mvnw clean package -Dquarkus.native.container-build=true
# or without container if you have GraalVM and native-image installed locally:
# ./mvnw clean package -Pnative
```

The resulting native binary will be in `./target/` named something like `demo-quarkus-1.0.0-SNAPSHOT-runner`.

Run it directly:

```bash
./target/demo-quarkus-1.0.0-SNAPSHOT-runner
```

## Dockerfile for native image

```dockerfile
# Stage 1: build (using Quarkus native build container)
FROM quay.io/quarkus/ubi-quarkus-native-image:23.3-java17 as build
WORKDIR /project
COPY . /project
RUN ./mvnw -B package -DskipTests -Dquarkus.native.container-build=true

# Stage 2: runtime
FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /work/
COPY --from=build /project/target/*-runner /work/application
RUN chmod 755 /work/application
EXPOSE 8080
CMD ["/work/application"]
```

## Notes

- H2 is configured in-memory; data is ephemeral per JVM/native process lifetime.
- Schema is dropped and recreated on each start; adjust `quarkus.hibernate-orm.database.generation` for other behaviors.
- Use environment variables to override datasource if needed in OpenShift deployment.
