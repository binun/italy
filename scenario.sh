#!/bin/bash

# https://hub.docker.com/r/sameersbn/mysql/builds/bezzyb9ncjwpv3gnkyhwnxp/
docker build -t sameersbn/mysql github.com/sameersbn/docker-mysql
docker run --name mysql -d -e 'DB_USER=dbuser' -e 'DB_PASS=dbpass' -e 'DB_NAME=dbname' sameersbn/mysql:latest
docker exec -it mysql bash
# show databases;
# create database dbtest;


