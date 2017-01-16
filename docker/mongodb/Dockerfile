FROM fedora
MAINTAINER http://fedoraproject.org/wiki/Cloud

RUN dnf clean all && dnf update -y
# Set environment variables.

# Final package install once everything is working.  Once everything is working, the intent is to use the MongoDB client from outside the container. You need either this line, or the previous package install line, but  not both.
RUN dnf -y install mongodb mongodb-server && dnf clean all
RUN mkdir -p /data/db

EXPOSE 27017
ENTRYPOINT ["/usr/bin/mongod"]

