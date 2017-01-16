#/bin/bash -i

docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 

	echo "Machine $id running server"
	gnome-terminal -x bash -c "docker exec -it $id bash -c \" cd / && java -jar udps.jar \""&
	#gnome-terminal -x bash -c "docker exec -it $id bash -c java" --window-with-profile=nyprof
	sleep 2;
done
)

echo "    Running client: $clientcommand"
#sleep 2;
#java -jar client.jar
