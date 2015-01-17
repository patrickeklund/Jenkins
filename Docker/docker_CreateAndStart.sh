#! /bin/bash

# Create the images
#docker build -t="patrick/eklund:jenkins_1.594" base/
#docker build -t="patrick/eklund:custom_jenkins_1.594" custom/
docker build -t="local:apache" centOs/

# Run the application in daemon mode
#docker run -d -p 8080:8080 --name Jenkins patrick/eklund:custom_jenkins_1.594
#docker run -d -p 8080:8080 --name Jenkins patrick/eklund:custom_jenkins_1.594
docker run -i -p 8181:8181 -p 8282:8282 --name centos local:apache /bin/bash

# list processes
docker ps -a
