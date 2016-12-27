#!/bin/bash

docker rm -f $(docker ps -a -q)

# -----------------MySQL Ubuntu ----------------------------------------------------
echo "Creating container vm-mysql-ubuntu"
#docker run --name vm-mysql-ubuntu -d image-mysql-ubuntu

# -------------------- Maria DB ---------------------------------------------------
docker run --name vm-mariadb -e MYSQL_ROOT_PASSWORD=root -d mariadb

# -------------------- Mongo DB --------------------------------------------------

echo "Creating container vm-mongo"
docker run --name vm-mongo -d mongo

# -------------------- Cassandra --------------------------------------------------

echo "Creating container vm-mongo"
docker run --name vm-cassandra -d cassandra
