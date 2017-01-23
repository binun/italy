#!/bin/bash

#source ./platforms.sh
./rmall.sh

#for pi in "${!platforms[@]}"
#do
   #platform=${platforms[$pi]}
   #subdir=${subdirs[$pi]}
   #cd $platform
   #echo $platform/$subdir
   #docker build -t image-$platform $subdir
   #cd ..
#done

cd mysql && docker build -t "image-mysql" . && cd ..

cd mariadb && docker build -t "image-mariadb" . && cd ..

cd mongodb && docker build -t "image-mongodb" . && cd ..

cd cassandr && docker build -t "image-cassandra" cassandra && cd ..

cd java8 && docker build -t "image-java" . && cd ..
