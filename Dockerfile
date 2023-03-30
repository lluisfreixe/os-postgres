FROM tomcat:7-jdk8-openjdk
USER root
COPY ./target/ROOT.war /usr/local/tomcat/webapps/
ENTRYPOINT ["sh", "/taula_postgres.sh"]