#!/usr/bin/env bash

mvn -q exec:java -Dexec.mainClass=com.company.AvroToJson -Dexec.args="$1"
