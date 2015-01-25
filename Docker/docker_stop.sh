#!/bin/bash

# describe usage

usage () {

    echo

    if   [ $1 -lt 1 ] ; then
        echo "$1 < 1 : To few arguments given"
    elif [ $1 -gt 1 ] ; then
        echo "$1 > 1 : To many arguments given"
    else
        echo "Invalid option"
    fi 

    echo
    echo "Usage:"
    echo "  -all     :  stop all docker images"
    echo "  -jbase   :  stop Jenkins Base docker image"
    echo "  -jCustom :  stop Jenkins Custmom docker image"
    echo "  -centos  :  stop centos docker image"
    exit
}


stop_dockerImage() {

    if [ $(docker ps -a | grep $1 | awk {'print $1'}) ] ; then
        echo Remove any [$1] running container forcefully, stopping it when needed
        docker rm -f $(docker ps -a | grep $1 | awk {'print $1'})
    else
        echo No matching containers [$1] running
    fi
}


stop_allDockerImage() {
    if [ $(docker ps -a | grep -v CONTAINER | awk {'print $1'}) ] ; then
       echo Remove all running containers forcefully, stopping them when needed
       docker rm -f $(docker ps -a -q)
    else
        echo No containers running
    fi
}


# Check that only 1 argument is given #
if [ $# -ne 1 ] ; then
    usage $#
fi

case "$1" in

-all)       stop_allDockerImage
            ;;
-jbase)     stop_dockerImage    local/jenkins:1.594
            ;;
-jcustom)   stop_dockerImage    local/jenkins:1.594_custom
            ;;
-centos)    stop_dockerImage    local/apache:tomcat
            ;;
*)          usage $#
            ;;
esac

