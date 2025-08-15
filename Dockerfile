# ===== Build =====
FROM openjdk:21-slim AS build
WORKDIR /workspace

COPY gradlew ./
COPY gradle gradle
RUN chmod +x gradlew
COPY settings.gradle* build.gradle* gradle.properties* ./
COPY . .
RUN ./gradlew --no-daemon :apps:commerce-api:bootJar -x test

# ===== Runtime =====
FROM openjdk:21-slim
WORKDIR /app
# 이제 여기에 있는 건 실행 가능한 Boot JAR 하나뿐
COPY --from=build /workspace/apps/commerce-api/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
