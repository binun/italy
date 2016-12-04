#/bin/bash -i
string=$(docker inspect --format '{{ .Name }}' $1)
echo ${string#?}
