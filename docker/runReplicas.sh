#/bin/bash -i

source ./platforms.sh

for platform in "${platforms[@]}"
do
   dbidfile=$platform.txt
   dbid=$(cat $dbidfile)
   echo "Running intercepting agent $dbidfile"
   docker exec $dbid sh -c "java -jar /interceptor.jar $platform" &
done

#sleep 1;
docker ps -f 'name=replica_' | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 

	echo "Replica $id is being launched"
	gnome-terminal -x bash -c "docker exec -it $id bash -c \" cd / && java -jar udps.jar \""&
	#gnome-terminal -x bash -c "docker exec -it $id bash -c java" --window-with-profile=nyprof
	sleep 1;
done
)
