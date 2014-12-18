#! /bin/bash

# Create the images
docker build -t="patrick/eklund:jenkins_1.594" base/
docker build -t="patrick/eklund:custom_jenkins_1.594" custom/

# Run the application in daemon mode
docker run -d -p 8080:8080 --name Jenkins patrick/eklund:custom_jenkins_1.594

# list processes
docker ps -a
