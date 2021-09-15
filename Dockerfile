
FROM adoptopenjdk/openjdk14:jdk-14.0.2_12-alpine-slim

COPY src /opt/functional-tests/src
COPY build.gradle.kts /opt/functional-tests/build.gradle.kts
COPY settings.gradle.kts /opt/functional-tests/settings.gradle.kts
COPY gradle /opt/functional-tests/gradle
COPY gradlew /opt/functional-tests/gradlew

WORKDIR /opt/functional-tests

CMD ["./gradlew", "cleanTest", "test", "-i", "-Djunit.jupiter.extensions.autodetection.enabled=true"]