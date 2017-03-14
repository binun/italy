#/bin/bash -i

engine=$1.jar

docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 
	echo "  Updating engine: $id "
	docker cp ./$engine $id:/
done)
