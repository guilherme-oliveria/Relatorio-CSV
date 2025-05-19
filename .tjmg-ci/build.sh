#!/bin/bash 

cd .. \
&& cp ./.tjmg-ci/application.properties ./src/main/resources/application.properties \
&& ./gradlew assemble
