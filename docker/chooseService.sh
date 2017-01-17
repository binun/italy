#!/bin/bash

dbms=172.17.0.4-10
#declare -a ports=(3306 3306 27017 7199)
#declare -a services=("mariadb" "mysql" "mongod" "cassandra")

declare -a ports=(27017)
declare -a services=("mongod")
host=""

for index in "${!ports[@]}"
do
	port=${ports[$index]}
	service=${services[$index]}
	#echo $port
	#echo $service
	host=$(nmap -p $port $dbms -oG - | grep -E "open.*$service" | awk '{print $2}')
	#echo $host
	if [ -n "${host}" ]; then
		echo "   $service is available"
		break
	fi
done

echo $host
#nmap -p 27017 172.17.0.4-10 -oG - | grep -E 'open.*mongod' | awk '{print $2}'
