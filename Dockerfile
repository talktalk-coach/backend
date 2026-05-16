# 1단계: 빌드 스테이지 (Java 21 적용)
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
# 그래들 래퍼와 설정 파일들을 먼저 복사 (캐싱 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

#애저 api 의존성 복사
COPY libs libs

# 의존성 설치
RUN ./gradlew build -x test --parallel --continue > /dev/null 2>&1 || true

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew bootJar -x test

# 2단계: 실행 스테이지 (Java 21 적용)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# 빌드 스테이지에서 생성된 jar 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 컨테이너 실행 시 자바 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
# Spring Boot 기본 포트 노출
EXPOSE 8080