server=./udps.jar

rm -f ./hosts.config
docker ps -q | xargs -n 1 docker inspect --format '{{ .NetworkSettings.IPAddress }} 5555' | sed 's/ \// /' | awk '{print i++ " " $0}' > ./hosts.config
echo "  Generated hosts configuration"
cat ./hosts.config

docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 

	echo "  Updating BFT: $id "
	docker cp $server $id:/
	docker cp ./chooseDBMS.sh $id:/
done)
