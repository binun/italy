#!/bin/bash

dbms=172.17.0.4-10
#declare -a ports=(3306 3306 27017 7199)
#declare -a services=("mariadb" "mysql" "mongod" "cassandra")

declare -a ports=(3306 3306 27017) # first is mariadb
declare -a services=("mysql" "mysql" "mongod")
host=""

for index in "${!ports[@]}"
do
	port=${ports[$index]}
	service=${services[$index]}
	#echo $port
	#echo $service
	host=$(nmap -p $port $dbms -oG - | grep "open" | awk '{print $2;exit}')
	if [ -n "${host}" ]; then
		echo $host:$port
		#break
	fi
done
#nmap -p 27017 172.17.0.4-10 -oG - | grep -E 'open.*mongod' | awk '{print $2}'
