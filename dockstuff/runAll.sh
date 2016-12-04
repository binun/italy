#/bin/bash -i

./cleanup.sh
./createVMs.sh
./configureVMs.sh bft bftserver
./infect.sh bft &
./runExperiment.sh bft bftserver bftclient

