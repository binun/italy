#!/bin/bash

apt-get purge docker-engine
apt-get autoremove --purge docker-engine
apt-get autoclean
rm -rf /var/lib/docker
rm /etc/apparmor.d/docker
groupdel docker
