FROM java:8

USER root
WORKDIR /root

ENV JAVA_HOME 	/usr/lib/jvm/java-8-openjdk-amd64
ENV PATH		$PATH:$JAVA_HOME/bin

RUN apt-get clean && apt-get -y update

#RUN cp /root/.ssh/id_rsa.pub /root/.ssh/authorized_keys
#COPY conf/ssh_config /root/.ssh/config

#COPY test.txt /root
#RUN mkdir /config
#ADD config /config/

EXPOSE 5555 6666 7199 7000 7001 9042 9160 3306 27017 
