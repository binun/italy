#!/bin/bash

filename=test.js
res=dbresult.txt

if [ $1 == 'createDB' ]; then
   dbname=$2
   printf "use $dbname" > $filename
fi

if [ $1 == 'createTable' ]; then
   dbname=$2
   tbname=$3
   printf "use $dbname\ndb.createCollection(\"$tbname\")" > $filename
fi

if [ $1 == 'deleteTable' ]; then
   dbname=$2
   tbname=$3
   printf "use $dbname\ndb.$tbname.drop()" > $filename
fi

if [ $1 == 'deleteDB' ]; then
   dbname=$2
   printf "use $dbname\ndb.dropDatabase()" > $filename
fi

if [ $1 == 'addTuple' ]; then
   dbname=$2
   tbname=$3
   key1=$4
   value1=$5
   key2=$6
   value2=$7
   printf "use $dbname\ndb.$tbname.insertOne({$key1:$value1,$key2:\"$value2\"})" > $filename
fi

if [ $1 == 'updateTuple' ]; then
   dbname=$2
   tbname=$3
   key1=$4
   value1=$5
   key2=$6
   value2=$7
   printf "use $dbname\ndb.$tbname.update({$key1:$value1},{$key1:$value1,$key2:\"$value2\"})" > $filename
fi

if [ $1 == 'rmTuple' ]; then
   dbname=$2
   tbname=$3
   key=$4
   value=$5
   printf "use $dbname\ndb.$tbname.deleteMany({$key:$value})" > $filename
fi

if [ $1 == 'fetch' ]; then
   dbname=$2
   tbname=$3
   printf "use $dbname\ndb.$tbname.find()" > $filename
fi

cat $filename | mongo > $res
