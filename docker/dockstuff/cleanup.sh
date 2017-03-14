#!/bin/bash

echo "Cleaning VMS from old stuff"
docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do
docker exec -it $id rm -f /config/*.config
docker exec -it $id rm -f *.jar 
done)

echo "Stopping VMs"
docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do
docker stop $id 
docker rm -v $id
done)

rm -f hosts.config
rm -f endserver.txt
