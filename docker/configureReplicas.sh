
source ./platforms.sh
server=./udps.jar
interceptor=./interceptor.jar

rm -f ./hosts.config
docker ps -f 'name=replica_' | awk '{print $1}' | grep -v 'CONTAINER' |  (while read id; do echo $(./docker-ip.sh $id) >> hosts.config; done)
echo "  Generated hosts configuration"
cat ./hosts.config

docker ps -f 'name=replica_' | awk '{print $1}' | grep -v 'CONTAINER' | (while read id; do
	echo "  Updating Replica: $id "
	docker cp $server $id:/
done)

for platform in "${platforms[@]}"
do
   dbfile=$platform.txt
   echo "DBMS $platform $dbfile"
   iddb=$(cat $dbfile)
   echo $iddb
   docker cp $interceptor $iddb:/
done


#idmysql=$(cat mysql.txt)
#idmaria=$(cat mariadb.txt)
#idcas=$(cat cassandra.txt)
#idmongo=$(cat mongo.txt)

#docker cp $interceptor $idmysql:/
#docker cp $interceptor $idmaria:/
#docker cp $interceptor $idcassandra:/
#docker cp $interceptor $idmongo:/

