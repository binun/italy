FROM ubuntu
MAINTAINER Ian Blenke <ian@blenke.com>

# install necessary stuff; avahi, and ssh such that we can log in and control avahi
RUN apt-get -y update -y
RUN DEBIAN_FRONTEND=noninteractive apt-get -qq install -y avahi-daemon avahi-utils
RUN apt-get -qq -y autoclean
RUN apt-get -qq -y autoremove
RUN apt-get -qq -y clean

ADD avahi-daemon.conf /etc/avahi/avahi-daemon.conf

# workaround to get dbus working, required for avahi to talk to dbus. This will be mounted
RUN mkdir -p /var/run/dbus
VOLUME /var/run/dbus

RUN avahi-daemon
