#/bin/bash -i
localdir=$1

echo " Infecting ... "

while true
do
	nblines=$(grep -c '' hosts.config)
	n=$((RANDOM%$nblines))
	rn=replica_$n
	docker cp eicar.com.txt $rn:/config
	echo " Infecting $rn"
	sleep 10
done
