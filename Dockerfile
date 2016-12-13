FROM mariadb
# install necessary stuff; avahi, and ssh such that we can log in and control avahi
RUN yum -y update

