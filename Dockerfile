FROM maven:3.9.7-eclipse-temurin-17 AS build
WORKDIR /bot
COPY src src
COPY pom.xml pom.xml
RUN mvn clean package
FROM bellsoft/liberica-openjdk-debian:17
#RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group spring-boot
#USER spring-boot:spring-boot-group
WORKDIR /application
VOLUME /tmp
ARG JAR_FILE=bot-exchange-0.0.1-SNAPSHOT.jar
ENV BOT_NAME=${BON_NAME}
ENV BOT_TOKEN=${BOT_TOKEN}
ENV BOT_DB_USERNAME=${MYSQLDB_USER}
ENV BOT_DB_PASSWORD=${MYSQLDB_ROOT_PASSWORD}
COPY --from=build /bot/target/${JAR_FILE} application.jar

# В случае с "толстым" JAR-архивом мы можем запускать приложение при помощи java -jar
# ENTRYPOINT exec java ${JAVA_OPTS} -jar application.jar ${0} ${@}
# ENTRYPOINT ["java", "-jar", "./application.jar"]

ENTRYPOINT ["java", "-Dlogging.level.org.hibernate.SQL=DEBUG", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-jar", "./application.jar", "-Djava.net.preferIPv4Stack=true"]
