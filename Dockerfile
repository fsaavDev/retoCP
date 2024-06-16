FROM gradle:7.3.3-jdk11 AS build

WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src/
RUN gradle build --no-daemon

FROM adoptopenjdk/openjdk11:latest

EXPOSE 8080

COPY --from=build /app/build/libs/retocp-1.0.0.jar retocp-1.0.0.jar

ENV DB_URL=jdbc:postgresql://dpg-cpmdp0mehbks73fnvdmg-a.oregon-postgres.render.com/retocp
ENV DB_USERNAME=retocp_user
ENV DB_PASSWORD=BjUAJzXmwv6kjw1aQNfEiyJbATQfyUrb
ENV JWT_SECRET=retocineplanet2024

ENTRYPOINT ["java","-jar","/retocp-1.0.0.jar"]