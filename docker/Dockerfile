### STAGE 1: Build war ###
FROM maven:3.8.1-jdk-8-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean package

### STAGE 2: Run on Tomcat ###
FROM tomcat:7-jdk8-openjdk-slim
COPY --from=build /workspace/target/*.war /usr/local/tomcat/webapps/achievements.war
COPY docker/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
EXPOSE 8080
ENTRYPOINT ["/wait-for-it.sh", "localhost:3306", "-t", "20", "--", "catalina.sh", "run"]