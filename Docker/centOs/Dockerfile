# Base iamge to start from 
FROM centos:7.0.1406

# Package maintainer
MAINTAINER Patrick Eklund


#
# Set Base Environment Variables
#
ENV JAVA_MAJOR_VERSION 1.6.0
ENV JAVA_MINOR_VERSION 45
ENV JAVA_HOME          /opt/jdk${JAVA_MAJOR_VERSION}_${JAVA_MINOR_VERSION}
ENV JAVA_FILE          jdk-6u45-linux-x64.bin
ENV PATH               ${JAVA_HOME}/bin:${PATH}

#
# Install base packages
#
RUN yum install -y wget curl zip unzip xinetd subversion


#
# Install Java
# 
COPY bin/${JAVA_FILE} /tmp/${JAVA_FILE}
RUN chmod +x /tmp/${JAVA_FILE}
WORKDIR /opt
RUN /tmp/${JAVA_FILE}

#
# activate Java on startup
#
#COPY shell-scripts/java.sh /etc/profile.d/java.sh
#RUN  chmod +x /etc/profile.d/java.sh

#
# remove unused files
#
RUN yum clean all
RUN rm -f  /tmp/${JAVA_FILE}
RUN rm -rf ${JAVA_HOME}/db/{demo,docs,javadoc}
RUN find ${JAVA_HOME}/ -name *.bat  | xargs rm -f 
RUN find ${JAVA_HOME}/ -name *.html | xargs rm -f 
RUN rm -rf ${JAVA_HOME}/man
RUN rm -f  ${JAVA_HOME}/src.zip

WORKDIR /tmp