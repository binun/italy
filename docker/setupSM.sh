
#!/bin/bash

docker rm -f $(docker ps -a -q)

./createReplicas.sh
./createDBMS.sh
./configureReplicas.sh
./runReplicas.sh
sleep 5
echo "Run client"
java -jar udpc.jar
