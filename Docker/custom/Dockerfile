# Base package to start from 
FROM local/jenkins:1.594

# Maintainer
MAINTAINER Patrick Eklund

# Copy plugins txt file to shared area
COPY plugin/plugins.txt /usr/share/jenkins/plugins.txt

# Copy plugins Url txt file to shared area
COPY plugin/pluginsUrl.txt /usr/share/jenkins/pluginsUrl.txt

# ensure we are jenkins user
USER jenkins

# Get plugins
RUN  /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

# Get plugins which are not part of normal Jenkins flow
RUN  /usr/local/bin/pluginsUrl.sh /usr/share/jenkins/pluginsUrl.txt
