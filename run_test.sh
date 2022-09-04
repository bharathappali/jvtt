#!/bin/bash

if [[ $(which docker) && $(docker --version) ]]; then
    echo "Starting experiment with native threads."
    docker run --rm -it bharathappali/jvtt:latest
    echo "Starting experiment with virtual threads"
    docker run --rm -it -e USE_VIRTUAL="true" bharathappali/jvtt:latest
else
    echo "Docker not installed. Exiting"
    exit 1
fi
