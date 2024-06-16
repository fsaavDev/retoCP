FROM adoptopenjdk/openjdk11:latest

EXPOSE 8080

ARG JARFILE=build/libs/retocp-1.0.0.jar
COPY ${JARFILE} app.jar

ENV DB_URL=jdbc:postgresql://dpg-cpmdp0mehbks73fnvdmg-a.oregon-postgres.render.com/retocp
ENV DB_USERNAME=retocp_user
ENV DB_PASSWORD=BjUAJzXmwv6kjw1aQNfEiyJbATQfyUrb
ENV JWT_SECRET=retocineplanet2024

ENTRYPOINT ["java","-jar","/app.jar"]