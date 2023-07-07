FROM openjdk:17-jdk-alpine

WORKDIR /opt/chi-academy-test

ADD target/chi_academy_test-0.0.1-SNAPSHOT.jar  chi-academy-test.jar
EXPOSE 8080

CMD java -jar chi-academy-test.jar
