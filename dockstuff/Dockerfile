FROM ubuntu

USER root
WORKDIR /root

RUN apt-get clean
RUN apt-get -y update

ADD getip.sh sbin/
ADD if* sbin/

EXPOSE 5555
