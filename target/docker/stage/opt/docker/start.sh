#!/bin/bash

if [ -z $HTTP_PORT ]; then
  read -p 'No HTTP_PORT environment variable defined, provide a port number or hit [ENTER] to accept default [9000]: ' HTTP_PORT
  export HTTP_PORT
  HZ_PORT=$((HTTP_PORT + 501))
  echo "Configuring port $HZ_PORT for Hazelcast"
  export HZ_PORT
fi

bin/hazel-test 
-Dhttp.port=$HTTP_PORT \
-Dpidfile.path=/dev/null \
-Dlogger.file=conf/logback.xml \
-Dhz.port=${HZ_PORT}