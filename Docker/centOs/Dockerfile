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

# Tomcat Environment Variables
ENV TOMCAT_VERSION     7.0.57
ENV TOMCAT_FILE        apache-tomcat-${TOMCAT_VERSION}.tar.gz
ENV CATALINA_HOME      /opt/apache-tomcat-${TOMCAT_VERSION}
ENV LD_LIBRARY_PATH    /usr/local/apr/lib

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
COPY shell-scripts/java.sh /etc/profile.d/java.sh
RUN  chmod +x /etc/profile.d/java.sh

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
# Install apache tomcat = CATALINA_HOME
#
WORKDIR /tmp
COPY bin/${TOMCAT_FILE} /tmp/${TOMCAT_FILE}
RUN tar -xvzf ${TOMCAT_FILE}
RUN mv apache-tomcat-${TOMCAT_VERSION} /opt/apache-tomcat-${TOMCAT_VERSION}

#
# create 2 instances of tomca
#  Each instance uses the CATALINA_HOME binaries
#  Each are instantiated with different CATALINA_BASE paths
#  Each instance have their own conf logs temp webapps work
#
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2

# change folder
WORKDIR /opt/apache-tomcat-${TOMCAT_VERSION}

# copy folders needed for both instances to work
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

# get start and stop template scripts for instance-1 
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/bin
COPY shell-scripts/startup.sh.tomcat.template    /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/bin/startup.sh
COPY shell-scripts/shutdown.sh.tomcat.template   /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/bin/shutdown.sh

# get start and stop template scripts for instance-2
RUN mkdir /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/bin
COPY shell-scripts/startup.sh.tomcat.template    /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/bin/startup.sh
COPY shell-scripts/shutdown.sh.tomcat.template   /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/bin/shutdown.sh

# set proper instance in template files instance-1
RUN sed -i 's/@instance@/1/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/bin/startup.sh
RUN sed -i 's/@instance@/1/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/bin/shutdown.sh

# set proper instance in template files instance-2
RUN sed -i 's/@instance@/2/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/bin/startup.sh
RUN sed -i 's/@instance@/2/g'        /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/bin/shutdown.sh

# set exec rights on start and stop scripts instance-1
RUN chmod +x /opt/apache-tomcat-${TOMCAT_VERSION}-instance-1/bin/*

# set exec rights on start and stop scripts instance-2
RUN chmod +x /opt/apache-tomcat-${TOMCAT_VERSION}-instance-2/bin/*


# set startup both tomcat instances
COPY shell-scripts/start_tomcat.sh   /etc/profile.d/start_tomcat.sh
RUN chmod +x /etc/profile.d/start_tomcat.sh



#
# Install apache tomcat Native Library
#   native library set for tomcat with environment variable LD_LIBRARY_PATH
#   http://www.cyberciti.biz/faq/centos-rhel-7-redhat-linux-install-gcc-compiler-development-tools/
#   http://blogoless.blogspot.se/2012/08/how-to-install-apache-tomcat-native.html
#

# install Development tools needed to perform make
RUN yum group install -y "Development Tools"

# install apr-devel
RUN yum install -y apr-devel openssl-devel

#get native src
WORKDIR /tmp
RUN wget http://apache.mirrors.spacedump.net/tomcat/tomcat-connectors/native/1.1.32/source/tomcat-native-1.1.32-src.tar.gz
RUN tar -xvzf tomcat-native-1.1.32-src.tar.gz

# create binaries
WORKDIR /tmp/tomcat-native-1.1.32-src/jni/native
RUN ./configure --with-apr=/usr/bin/apr-1-config && make && make install


#
# cleanup
#
RUN yum clean all
RUN rm -rf /tmp/*


#
# Set image workdir
#
WORKDIR /tmp