#!/usr/bin/env bash

./mvnw -B  package -Dmaven.test.skip=true --file pom.xml

cp $(find . -name 'mall*.jar') app.jar

docker build --tag jiucai-service:${JIUCAI_TAG:-latest} .
