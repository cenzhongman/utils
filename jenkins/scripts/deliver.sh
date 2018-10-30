#!/usr/bin/env bash

set -x
NAME=`mvn help:evaluate -Dexpression=project.name | grep "^[^\[]"`
set +x
echo '获得项目名'

set -x
VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[^\[]"`
set +x
echo '获得项目版本'

echo '正在运行应用'
set -x
java -jar target/${NAME}-${VERSION}.jar
