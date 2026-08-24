FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/

RUN chmod +x mvnw && ./mvnw package -DskipTests \
	&& packaged_jar=$(find target -maxdepth 1 -type f -name '*.jar' ! -name '*.original' | head -n 1) \
	&& cp "$packaged_jar" target/app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]