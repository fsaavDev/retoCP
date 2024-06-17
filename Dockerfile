FROM adoptopenjdk/openjdk11:latest

EXPOSE 8080

<<<<<<< Updated upstream
ARG JARFILE=build/libs/retocp-1.0.0.jar
COPY ${JARFILE} app.jar
=======
COPY --from=build /app/build/libs/retocp-1.1.0.jar retocp-1.1.0.jar
>>>>>>> Stashed changes

ENV DB_URL=jdbc:postgresql://dpg-cpmdp0mehbks73fnvdmg-a.oregon-postgres.render.com/retocp
ENV DB_USERNAME=retocp_user
ENV DB_PASSWORD=BjUAJzXmwv6kjw1aQNfEiyJbATQfyUrb
ENV JWT_SECRET=retocineplanet2024

<<<<<<< Updated upstream
ENTRYPOINT ["java","-jar","/app.jar"]
=======
ENTRYPOINT ["java","-jar","/retocp-1.1.0.jar"]
>>>>>>> Stashed changes
