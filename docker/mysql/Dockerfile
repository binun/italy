FROM sameersbn/ubuntu:14.04.20161014
MAINTAINER sameer@damagehead.com

ENV MYSQL_USER=mysql \
    MYSQL_DATA_DIR=/var/lib/mysql \
    MYSQL_RUN_DIR=/run/mysqld \
    MYSQL_LOG_DIR=/var/log/mysql

RUN apt-get -y update --fix-missing && apt-get install -y openjdk-7-jdk
# Define working directory.
WORKDIR /data

ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64

RUN apt-get update \
 && DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server \
 && rm -rf ${MYSQL_DATA_DIR} \
 && rm -rf /var/lib/apt/lists/*

COPY entrypoint.sh /sbin/entrypoint.sh
#COPY clearState.sh /clearState.sh
RUN chmod 0777 /sbin/entrypoint.sh
#RUN chmod 0777 /clearState.sh

EXPOSE 3306 5555
VOLUME ["${MYSQL_DATA_DIR}", "${MYSQL_RUN_DIR}"]
ENTRYPOINT ["/sbin/entrypoint.sh"]
CMD ["/usr/bin/mysqld_safe"]
