#!/bin/bash

# https://hub.docker.com/r/sameersbn/mysql/builds/bezzyb9ncjwpv3gnkyhwnxp/

DBNAME=mydb
TBNAME=mytbl

#docker build -t mysql-ubuntu-image github.com/sameersbn/docker-mysql
#docker run --name mysql-ubuntu -d mysql-ubuntu-image

declare -a sqls=(
	create database mydb
	"show databases"
	"use mydb;create table mytable(taskid INT(4),subject INT(4))"
        "use mydb;show tables"
	"use mydb; INSERT INTO mytable VALUES (1111,1110)"
	"use mydb; INSERT INTO mytable VALUES (2222,2220)"
	"use mydb; INSERT INTO mytable VALUES (3333,3330)"
	"use mydb; INSERT INTO mytable VALUES (4444,4440)"
	"use mydb; SELECT taskid,subject FROM mytable"
)

for sql in "${sqls[@]}"
do
   sqlstr="$sql"
   echo $sqlstr
   docker exec -it mysql-ubuntu bash -c "mysql -u debian-sys-maint -e \" $sqlstr \" "
   sleep 1
done

# show databases;
# create database dbtest;
# mysql -u dbuser -pdbpass "dbname" -e "show databases"

