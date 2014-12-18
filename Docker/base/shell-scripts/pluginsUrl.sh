#! /bin/bash

# Parse a support-core plugin -style txt file as specification for jenkins plugins to be installed
# in the reference directory, so user can define a derived Docker image with just :
# 
# FROM jenkins
# COPY pluginsUrl.txt /pluginsUrl.txt
# RUN /usr/share/jenkins/pluginsURL.sh /pluginsUrl.txt
# 

REF=/usr/share/jenkins/ref/plugins
mkdir -p $REF

while read spec; do
    plugin=(${spec//:/ });
   
    # Check if plugin is of type Zip, otherwise we expect it to be a HPI package
    if [[ ${plugin[0]}  =~ .*zip ]]
    then
        echo "Zip file (${plugin[0]})!"
        curl -L ${plugin[0]} -o $REF/${plugin[1]}.zip
	unzip -y ${plugin[1]}.zip -d $REF/
        rm $REF/${plugin[1]}.zip
    else
        echo "hpi file (${plugin[0]})!"  
        curl -L ${plugin[0]}/${plugin[1]}.hpi  -o $REF/${plugin[1]}.hpi;
    fi
done  < $1
