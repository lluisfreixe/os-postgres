FROM tomcat:7-jdk8-openjdk
USER root
COPY ./target/os-postgres-1.0.war /usr/local/tomcat/webapps/
