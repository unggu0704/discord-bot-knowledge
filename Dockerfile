# 1. Gradle 8.2 + JDK 17을 포함한 빌드 환경
FROM gradle:8.2-jdk17 AS builder

# 2. 작업 디렉토리 설정 및 코드 복사
WORKDIR /app
COPY . .

# 3. Gradle 빌드 실행 (빌드 실패 시 로그 확인)
RUN gradle build --no-daemon && ls -al build/libs/

# 4. JDK 17 실행 환경
FROM eclipse-temurin:17-jre-jammy

# 5. 작업 디렉토리 설정
WORKDIR /app

# 6. 빌드된 애플리케이션 복사
COPY --from=builder /app/build/libs/DisordBot-1.0-SNAPSHOT.jar discordBot.jar

# 7. 애플리케이션 실행 명령
CMD ["java", "-jar", "discordBot.jar"]
