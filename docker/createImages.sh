#!/bin/bash

./rmall.sh

cd mysql && docker build -t "image-mysql" . && cd ..

cd mariadb && docker build -t "image-mariadb" . && cd ..

cd mongodb && docker build -t "image-mongodb" . && cd ..

cd cassandr && docker build -t "image-cassandra" cassandra && cd ..

cd java8 && docker build -t "image-java" . && cd ..
