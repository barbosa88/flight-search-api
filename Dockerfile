FROM maven:3.6.3-adoptopenjdk-11-openj9 AS build
COPY src /usr/app/flightin/src
COPY pom.xml /usr/app/flightin
COPY lombok.config /usr/app/flightin
RUN mvn -f /usr/app/flightin/pom.xml clean package

FROM adoptopenjdk/openjdk11-openj9:alpine-jre
COPY --from=build /usr/app/flightin/target/flight-search-api-0.0.1-SNAPSHOT.jar /usr/app/flightin.jar
ENTRYPOINT ["java","-jar","/usr/app/flightin.jar"]