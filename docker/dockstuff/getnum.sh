#/bin/bash -i
docker inspect --format '{{ .Name }}' $1 | awk -F'_' '{print $2}' 
