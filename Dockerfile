FROM maven:3.8-openjdk-18-slim

WORKDIR /solver

COPY src src

COPY pom.xml ./

RUN mvn install

RUN mvn compile

EXPOSE 4567
