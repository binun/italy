#!/bin/bash
mysql -uroot -p -e "show databases" | grep -v Database | grep -v mysql | grep -v information_schema | grep -v test | grep -v OLD | gawk '{print "drop database " $1 ";select sleep(0.1);"}' | mysql -uroot -p
