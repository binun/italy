#!/bin/bash

filename=test.cql

if [ $1 == 'createDB' ]; then
   dbname=$2
   printf "CREATE KEYSPACE IF NOT EXISTS $dbname WITH replication = {\'class\':\'SimpleStrategy\', \'replication_factor\':1};" > $filename
fi

if [ $1 == 'createTable' ]; then
   dbname=$2
   tbname=$3
   printf "CREATE TABLE IF NOT EXISTS $dbname.$tbname(id INT PRIMARY KEY,name text);" > $filename
fi

if [ $1 == 'deleteTable' ]; then
   dbname=$2
   tbname=$3
   printf "DROP TABLE $dbname.$tbname;" > $filename
fi

if [ $1 == 'deleteDB' ]; then
   dbname=$2
   printf "DROP KEYSPACE $dbname;" > $filename
fi

if [ $1 == 'addTuple' ]; then
   dbname=$2
   tbname=$3
   key1=$4
   value1=$5
   key2=$6
   value2=$7
   printf "INSERT INTO $dbname.$tbname($key1,$key2) VALUES($value1,\'$value2\');" > $filename
fi

if [ $1 == 'updateTuple' ]; then
   dbname=$2
   tbname=$3
   key1=$4
   value1=$5
   key2=$6
   value2=$7
   printf "UPDATE $dbname.$tbname SET $key2:\'$value2\' WHERE $key1=$value1;" > $filename
fi

if [ $1 == 'rmTuple' ]; then
   dbname=$2
   tbname=$3
   key=$4
   value=$5
   printf "DELETE FROM $dbname.$tbname WHERE $key=$value;" > $filename
fi

if [ $1 == 'fetch' ]; then
   dbname=$2
   tbname=$3
   printf "SELECT * FROM $dbname.$tbname;" > $filename
fi

cqlsh -f $filename localhost > dbresult.txt
