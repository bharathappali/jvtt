#!/bin/bash

if [[ $(which docker) && $(docker --version) ]]; then
    echo "Starting experiment with native threads."
    docker run --rm -it -e NUM_NATIVE_THREAD=100 -e NUM_JOBS=10000 bharathappali/jvtt:latest
    echo "Starting experiment with virtual threads"
    docker run --rm -it -e USE_VIRTUAL="true" -e NUM_NATIVE_THREAD=100 -e NUM_JOBS=10000 bharathappali/jvtt:latest
else
    echo "Docker not installed. Exiting"
    exit 1
fi
