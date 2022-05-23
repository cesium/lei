FROM library/maven

WORKDIR /solver

COPY src src

COPY pom.xml ./

RUN mvn install

RUN mvn compile

EXPOSE 4567
