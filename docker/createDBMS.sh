
#!/bin/bash

#docker rm -f $(docker ps -a -q)
source ./platforms.sh

#docker run --name vm-mysql --publish-all=true -d image-mysql
# mysql -u root -p , mysql -u root

#docker run --name vm-cassandra --publish-all=true -d image-cassandra
# cqlsh

#docker run --name vm-mongodb --publish-all=true -d image-mongodb
# mongo

#docker run --name vm-mariadb -p 5555:5555 --publish-all=true -e MYSQL_ROOT_PASSWORD=root -d image-mariadb
# mysql -u root -proot

#for platform in "${platforms[@]}"
#do
   #docker run --name vm-$platform --publish-all=true -d image-$platform
#done

docker run --name vm-mariadb --publish-all=true -d image-mariadb
docker run --name vm-mysql --publish-all=true -d image-mysql
docker run --name vm-mongodb --publish-all=true -d image-mongodb
docker run --name vm-cassandra --publish-all=true -d image-cassandra

