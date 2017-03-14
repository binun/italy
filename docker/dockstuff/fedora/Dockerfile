FROM fedora

USER root
WORKDIR /root

RUN yum clean all
RUN yum -y update

ADD dictadd.sh sbin/
ADD dictget.sh sbin/
ADD getip.sh sbin/
ADD if* sbin/

EXPOSE 5555
