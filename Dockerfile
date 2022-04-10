FROM library/maven
WORKDIR /solver
COPY SwapSolver .
RUN mvn install
RUN mvn compile
EXPOSE 4567
#RUN mvn exec:java
