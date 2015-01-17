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
ENV TOMCAT_VERSION     7.0.57
ENV TOMCAT_FILE        apache-tomcat-${TOMCAT_VERSION}.tar.gz
ENV CATALINA_HOME      /opt/apache-tomcat-${TOMCAT_VERSION}


#
# Install base packages
#
RUN yum install -y wget curl zip unzip tar xinetd subversion


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



#
# Export ports
#  instance 1 = 8181
#  instance 2 = 8282
#
EXPOSE 8181
EXPOSE 8282

#
# Install apache tomcat base
#
WORKDIR /tmp
COPY bin/${TOMCAT_FILE} /tmp/${TOMCAT_FILE}
RUN tar -xvzf ${TOMCAT_FILE}
RUN mv apache-tomcat-${TOMCAT_VERSION} /opt/apache-tomcat-${TOMCAT_VERSION}

# create 2 instances of tomcat
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2

# change folder
WORKDIR /opt/apache-tomcat-${TOMCAT_VERSION}

# copy folders needed for the instances
RUN cp -rf conf logs temp webapps work /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1
RUN cp -rf conf logs temp webapps work /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2

# set ports in server.xml for instance-1
RUN sed -i 's/<Server port="8005"/<Server port="8105"/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/conf/server.xml
RUN sed -i 's/<Connector port="8080"/<Connector port="8181"/g'  /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/conf/server.xml
RUN sed -i 's/<Connector port="8009"/<Connector port="8109"/g'  /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/conf/server.xml

# set ports in server.xml for instance-2
RUN sed -i 's/<Server port="8005"/<Server port="8205"/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/conf/server.xml
RUN sed -i 's/<Connector port="8080"/<Connector port="8282"/g'  /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/conf/server.xml
RUN sed -i 's/<Connector port="8009"/<Connector port="8209"/g'  /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/conf/server.xml

# get start and stop template scripts
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/scripts
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/scripts
COPY shell-scripts/start_tomcat.sh.template     /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/scripts/start.sh
COPY shell-scripts/shutdown_tomcat.sh.template  /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/scripts/shutdown.sh
COPY shell-scripts/start_tomcat.sh.template     /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/scripts/start.sh
COPY shell-scripts/shutdown_tomcat.sh.template  /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/scripts/shutdown.sh

# set proper path in template files
RUN sed -i 's/@instance@/1/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/scripts/start.sh
RUN sed -i 's/@instance@/1/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/scripts/shutdown.sh
RUN sed -i 's/@instance@/2/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/scripts/start.sh
RUN sed -i 's/@instance@/2/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/scripts/shutdown.sh

# set exec rights on start scripts
RUN chmod +x /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/scripts/*
RUN chmod +x /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/scripts/*

# set startup both tomcat instances
COPY shell-scripts/start_tomcat.sh   /etc/profile.d/start_tomcat.sh
RUN chmod + x /etc/profile.d/start_tomcat.sh


WORKDIR /tmp