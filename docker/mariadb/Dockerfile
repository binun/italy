FROM mariadb
ENV MYSQL_ROOT_PASSWORD root
RUN apt-get -y update --fix-missing && apt-get install -y openjdk-7-jdk
# Define working directory.
WORKDIR /data

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64

EXPOSE 3306 5555
