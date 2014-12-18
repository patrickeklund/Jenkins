# Base iamge to start from 
FROM java:openjdk-7u65-jdk

# Package maintainer
MAINTAINER Patrick Eklund


# Environment variablers
ENV JENKINS_HOME /var/jenkins_home
ENV JENKINS_VERSION 1.594
ENV JENKINS_UC https://updates.jenkins-ci.org

#
# install the following packages, wget, git, curl, zip and unzip
#
RUN apt-get update && apt-get install -y wget git curl zip unzip && rm -rf /var/lib/apt/lists/*

# Jenkins is ran with user `jenkins`, uid = 1000
# If you bind mount a volume from host/vloume from a data container, 
# ensure you use same uid
RUN useradd -d "$JENKINS_HOME" -u 1000 -m -s /bin/bash jenkins

# Jenkins home directoy is a volume, so configuration and build history 
# can be persisted and survive image upgrades
VOLUME /var/jenkins_home

# `/usr/share/jenkins/ref/` contains all reference configuration we want 
# to set on a fresh new installation. Use it to bundle additional plugins 
# or config file with your custom jenkins Docker image.
RUN mkdir -p /usr/share/jenkins/ref/init.groovy.d
COPY groovy-scripts/init.groovy /usr/share/jenkins/ref/init.groovy.d/tcp-slave-angent-port.groovy

# could use ADD but this one does not check Last-Modified header 
# see https://github.com/docker/docker/issues/8331
#RUN curl -L http://mirrors.jenkins-ci.org/war/1.594/jenkins.war -o /usr/share/jenkins/jenkins.war
COPY war/jenkins.war /usr/share/jenkins/jenkins.war

# set proper user privilages on jenkins directories 
RUN chown -R jenkins "$JENKINS_HOME" /usr/share/jenkins/ref

# Jenkins startup script
COPY shell-scripts/jenkins.sh /usr/local/bin/jenkins.sh

# from a derived Dockerfile, can use `RUN plugin.sh plugin.txt` to setup /usr/share/jenkins/ref/plugins from a support bundle
COPY shell-scripts/plugins.sh /usr/local/bin/plugins.sh
COPY shell-scripts/plugins.sh /usr/local/bin/pluginsUrl.sh

# set proper privilages and exec rights on jenkins files
RUN chown jenkins /usr/local/bin/jenkins.sh
RUN chown jenkins /usr/local/bin/plugins.sh
RUN chown jenkins /usr/local/bin/pluginsUrl.sh
RUN chmod +x /usr/local/bin/jenkins.sh
RUN chmod +x /usr/local/bin/plugins.sh
RUN chmod +x /usr/local/bin/pluginsUrl.sh

# for main web interface:
EXPOSE 8080

# will be used by attached slave agents:
EXPOSE 50000

# set proper user
USER jenkins

ENTRYPOINT ["/usr/local/bin/jenkins.sh"]

