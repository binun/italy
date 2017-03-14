FROM mariadb
ENV MYSQL_ROOT_PASSWORD root
RUN apt-get -y update --fix-missing && apt-get install -y openjdk-7-jdk
COPY clearState.sh /clearState.sh
RUN chmod 0777 /clearState.sh
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64
# Define working directory.
WORKDIR /data

EXPOSE 3306 6666
