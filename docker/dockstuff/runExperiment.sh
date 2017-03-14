#/bin/bash -i
localdir=$1
bftengine=$2.jar
bftclient=$3.jar
endservercommand="java -jar endserver.jar"
clientcommand="java -jar $bftclient 10"

#./setEndserver.sh

docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | 
(i=0; 
while read id; do 
	vmname=$(./getname.sh $id)
	echo "Machine $vmname"
	if test "$vmname" == "end_server"
	then
		echo "Omitting endserver"
	else	
		echo "Running replica: gnome-terminal -x $command --window-with-profile=nyprof"
		gnome-terminal -x bash -c "docker exec -it $id bash -c \" cd / && /usr/bin/supervisord -c supervisord.conf && java -jar $bftengine $i\""&
		i=$(($i + 1));
		sleep 4;
	fi
done
)



#cd $localdir
echo "    Running client: $clientcommand"
sleep 5;
$clientcommand
