
printf "use mydb" 
printf "use mydb\ndb.createCollection('mytb')" > test.js
printf "use mydb\ndb.mydb.insert({id:1,name:'myname'})" > test.js
printf "use mydb\ndb.mydb.update({id:1},{id:1,name:'myname2'})" > test.js
printf "use mydb\ndb.mydb.find()"
printf "use mydb\ndb.mydb.drop()" > test.js 
printf "use mydb\ndb.dropDatabase()" > test.js
