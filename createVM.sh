#!/bin/bash

# -----------------MySQL Ubuntu ----------------------------------------------------
echo "Building MySQL on Ubuntu"
cd ubuntu1
docker build -t "image-mysql-ubuntu" .
cd ..
echo "Creating container vm-mysql-ubuntu"
docker run --name vm-mysql-ubuntu -d image-mysql-ubuntu


# -------------------- Maria DB ---------------------------------------------------
docker pull mariadb
docker run --name vm-mariadb -e MYSQL_ROOT_PASSWORD=root -d mariadb

# -------------------- Mongo DB --------------------------------------------------

echo "Building MongoDB"
cd mongodb1
docker build -t "image-mongo" .
cd ..
echo "Creating container vm-mongo"
docker run --name vm-mongo -d image-mongo


