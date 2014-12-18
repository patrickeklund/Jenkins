#! /bin/bash

# create base image.
docker build -t="patrick/eklund:jenkins_1.594" base/

# create and run jenkins custom iamge
fig -p jenkins up
