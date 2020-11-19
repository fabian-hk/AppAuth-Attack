#!/bin/bash

docker run -p 127.0.0.1:8080:8080 \
 -v "$PWD/override.properties:/etc/c2id/override.properties" \
 c2id/c2id-server:10.0
