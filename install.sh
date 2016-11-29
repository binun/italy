apt-get -y update
apt-get -y install curl apt-transport-https ca-certificates
apt-key adv --keyserver hkp://ha.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D

apt-get -y install linux-image-extra-$(uname -r) linux-image-extra-virtual
curl -sSL https://get.docker.com/ | sh

cp -f docker /etc/default
cp -f ufw /etc/default
ufw enable
ufw allow 2375/tcp
ufw allow 2376/tcp
ufw reload


