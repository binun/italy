# Spotify Cassandra 2.0 Single Node Image
#
# VERSION               0.1
#
# Starts a Cassandra instance constituting a one node cluster. 

FROM spotify/cassandra:base

USER root

COPY runDB.sh /runDB.sh
RUN chmod 0777 /runDB.sh

# Place single-node startup-config script
ADD scripts/cassandra-singlenode.sh /usr/local/bin/cassandra-singlenode

EXPOSE 6666
# Start Cassandra
ENTRYPOINT ["cassandra-singlenode"]

