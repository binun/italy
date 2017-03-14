#!/bin/bash
keyspaces=$(echo desc keyspaces | cqlsh | xargs -n1 echo | grep -v ^system)
for ks in $keyspaces; do
    echo Dropping $ks
    echo "drop keyspace if exists $ks;" | cqlsh
done
