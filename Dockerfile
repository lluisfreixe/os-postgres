FROM tomcat:7-jdk8-openjdk
USER root
COPY ./target/ROOT.war /usr/local/tomcat/webapps/
# script  per crear la latula de postgres
# COPY taula.sh taula.sh
# ENTRYPOINT ["sh", "taula.sh"]