#!/bin/bash
declare -a arr=("fedora" "ubuntu" "alpine" "mint")
declare -a soft=("getip.sh" "if*" "dictadd.sh" "dictget.sh" "dictionary")
# update

for osdir in "${arr[@]}"
do
   echo "$osdir"
   if [ -d "$osdir" ]; then
      for st in "${soft[@]}"
      do
         cp -f $st $osdir
      done
      
     
   fi 
done
