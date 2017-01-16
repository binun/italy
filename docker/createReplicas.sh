#/bin/bash -i

echo "Launching replicas:"
for replica in replica_0 replica_1 replica_2 replica_3
do
	docker run --privileged -d --name $replica --publish-all=true $1 /bin/sh -c "while true; do echo hello; sleep 1; done"
done
