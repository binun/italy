#!/bin/bash
source ./platforms.sh
./rmall.sh

for pi in "${!platforms[@]}"
do
   platform=${platforms[$pi]}
   subdir=${subdirs[$pi]}
   cd $platform
   echo $platform/$subdir
   docker build -t image-$platform $subdir
   cd ..
done
