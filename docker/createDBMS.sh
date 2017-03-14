
#!/bin/bash

#docker rm -f $(docker ps -a -q)
source ./platforms.sh

#docker run --name vm-mysql --publish-all=true -d image-mysql
# mysql -u root -p , mysql -u root

#docker run --name vm-cassandra --publish-all=true -d image-cassandra
# cqlsh -e 'select release_version from system.local;'

#docker run --name vm-mongodb --publish-all=true -d image-mongodb
# mongo

#docker run --name vm-mariadb -p 5555:5555 --publish-all=true -e MYSQL_ROOT_PASSWORD=root -d image-mariadb
# mysql -u root -proot

for platform in "${platforms[@]}"
do
   dbfile=$platform.txt
   echo "Cleaning DB VM $dbfile"
   rm -f $dbfile
done


#rm -f mariadb.txt
#rm -f mongo.txt
#rm -f mysql.txt
#rm -f cassandra.txt

docker run --name vm-mariadb --publish-all=true -d image-mariadb > maria.txt
docker run --name vm-mysql --publish-all=true -d image-mysql > mysql.txt
docker run --name vm-mongodb --publish-all=true -d image-mongodb > mongo.txt
docker run --name vm-cassandra --publish-all=true -d image-cassandra > cassandra.txt
