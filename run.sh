#!/usr/bin/env bash
rm -rf output/
time mvn -X -q exec:java -Dexec.mainClass=com.company.NetflowV5ToAvro -Dexec.args="./input ./output"
