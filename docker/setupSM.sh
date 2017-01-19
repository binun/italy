#!/bin/bash

docker rm -f $(docker ps -a -q)

./createReplicas.sh
./configureReplicas.sh
./createDBMS.sh
./runReplicas.sh

#java -jar udpc.jar
