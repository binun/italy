#/bin/bash -i

endserverID=$(docker run -d --name end_server -P bft /bin/sh -c "while true; do echo hello; sleep 1; done")

docker inspect --format '{{ .NetworkSettings.IPAddress }}' end_server > endserver.txt
cat endserver.txt

docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 
echo "  Configure replica $id for endserver"
docker cp endserver.txt $id:/config
cp -f endserver.txt bft
docker cp bft/endserver.jar $id:/
done)

# echo "Running endserver:"
# gnome-terminal -x "docker exec -it $endserverID java -jar endserver.jar" --window-with-profile=nyprof
# sleep 5;
