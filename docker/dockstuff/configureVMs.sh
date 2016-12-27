#/bin/bash -i

localdir=$1
bftengine=$2.jar

docker ps -q | xargs -n 1 docker inspect --format '{{ .NetworkSettings.IPAddress }} 5555' | sed 's/ \// /' | awk '{print i++ " " $0}' > hosts.config
echo "  Generated hosts configuration"
cat hosts.config
cp -f hosts.config config
docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 
	echo "  Setting hosts configuration at $id"
	# cp -f hosts.config $localdir
	docker cp hosts.config $id:/config
	docker cp config/system.config $id:/config
	docker cp supervisord.conf $id:/
	docker cp test.txt $id:/
	# docker cp conf/yarn-site.xml $id:/conf
	docker exec $id service ssh start
	# docker exec $id bash -c "$HADOOP_HOME/sbin/start-yarn.sh"

	docker cp stabman/Makefile $id:/
	docker cp stabman/libvirt-proxy.c $id:/
	docker cp stabman/checksum.sh $id:/
	docker exec $id chmod 0777 /checksum.sh
	docker exec $id bash -c "cd / && make"
done)

docker ps -a | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do 

echo "  Updating BFT: $id "

docker cp ./$bftengine $id:/

done)
