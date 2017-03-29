FROM fedora
MAINTAINER http://fedoraproject.org/wiki/Cloud

RUN dnf install -y java-1.8.0-openjdk
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64

RUN dnf clean all && dnf update -y
# Set environment variables.

# Final package install once everything is working.  Once everything is working, the intent is to use the MongoDB client from outside the container. You need either this line, or the previous package install line, but  not both.
RUN dnf -y install mongodb mongodb-server && dnf clean all
RUN mkdir -p /data/db
COPY runDB.sh /runDB.sh
RUN chmod 0777 /runDB.sh
EXPOSE 6666 27017
ENTRYPOINT ["/usr/bin/mongod"]

