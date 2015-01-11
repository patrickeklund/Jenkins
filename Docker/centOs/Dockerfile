# Base iamge to start from 
FROM centos:7.0.1406

# Package maintainer
MAINTAINER Patrick Eklund


#
# Install base packages
#  wget
#  curl
#
RUN yum install -y wget
RUN yum install -y curl
RUN yum install -y zip
RUN yum install -y unzip
RUN yum install -y subversion


#
# Install Java
# 
COPY bin/jdk-6u45-linux-x64.bin /tmp/jdk-6u45-linux-x64.bin
RUN chmod +x /tmp/jdk-6u45-linux-x64.bin
WORKDIR /opt
RUN /tmp/jdk-6u45-linux-x64.bin

#
# activate Java on startup
#
COPY shell-scripts/java.sh /etc/profile.d/java.sh
RUN chmod +x /etc/profile.d/java.sh

#
# remove unused files
#
RUN rm -rf /tmp/jdk-6u45-linux-x64.bin
RUN rm -rf /opt/jdk1.6.0_45/db/demo
RUN rm -rf /opt/jdk1.6.0_45/db/docs
RUN rm -rf /opt/jdk1.6.0_45/db/javadoc
RUN rm -rf /opt/jdk1.6.0_45/man
RUN rm -f /opt/jdk1.6.0_45/src.zip

WORKDIR /tmp