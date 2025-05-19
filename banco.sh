#!bin/bash

red=`tput setaf 1`
reset=`tput sgr0`

docker-compose -f ./../compose/docker-compose.yml stop back-db
docker-compose -f ./../compose/docker-compose.yml up -d --force-recreate back-db

export COMPOSE_INTERACTIVE_NO_CLI=1

PSQL="docker-compose -f ./../compose/docker-compose.yml exec back-db psql -h localhost -p 5432 -U windson"

until $PSQL -c "select 1"
do
    echo "${red}waiting for postgres container...${reset}"
    sleep 2
done

docker-compose -f ./../compose/docker-compose.yml exec back-db psql postgres -U windson -c 'drop database windson;' -c 'create database windson;'


./gradlew update -Pcontexts=developer
