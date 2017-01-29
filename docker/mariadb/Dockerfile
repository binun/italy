FROM mariadb
ENV MYSQL_ROOT_PASSWORD root
RUN apt-get -y update --fix-missing
COPY clearState.sh /clearState.sh
RUN chmod 0777 /clearState.sh
# Define working directory.
WORKDIR /data

EXPOSE 3306
