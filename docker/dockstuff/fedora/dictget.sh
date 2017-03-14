#/bin/bash -i
cat dictionary | grep $1 | awk '{ print $2}'
