FROM docker.io/library/eclipse-temurin:25-jre

RUN useradd --system --home-dir /opt/game24 --create-home --user-group game24
USER game24
WORKDIR /opt/game24
COPY --chown=game24:game24 --chmod=0644 ./build/libs/game24-0.0.1-SNAPSHOT.jar game24.jar
ENTRYPOINT ["java", "-jar", "game24.jar"]
